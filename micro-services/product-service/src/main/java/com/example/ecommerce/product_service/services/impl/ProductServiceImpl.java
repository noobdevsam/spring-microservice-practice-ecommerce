package com.example.ecommerce.product_service.services.impl;

import com.example.ecommerce.product_service.entities.Product;
import com.example.ecommerce.product_service.exceptions.ProductPurchaseException;
import com.example.ecommerce.product_service.mapper.ProductMapper;
import com.example.ecommerce.product_service.model.ProductPurchaseRequestDTO;
import com.example.ecommerce.product_service.model.ProductPurchaseResponseDTO;
import com.example.ecommerce.product_service.model.ProductRequestDTO;
import com.example.ecommerce.product_service.model.ProductResponseDTO;
import com.example.ecommerce.product_service.repository.ProductRepository;
import com.example.ecommerce.product_service.services.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDTO getProductById(Integer id) {
        return productRepository
                .findById(id)
                .map(
                        productMapper::productToProductResponseDTO
                )
                .orElseThrow(
                        () -> new EntityNotFoundException("Product with ID: " + id + " not found")
                );
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository
                .findAll()
                .stream()
                .map(productMapper::productToProductResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Integer createProduct(ProductRequestDTO productRequestDTO) {
        return productRepository
                .save(
                        productMapper.productRequestDTOtoProduct(productRequestDTO)
                )
                .getId();
    }

    /**
     * Handles the purchase of multiple products by updating their stock quantities and returning the purchase details.
     * This method is transactional and will roll back if a `ProductPurchaseException` occurs.
     *
     * @param productRequestDTOs A list of `ProductPurchaseRequestDTO` objects containing product IDs and quantities to purchase.
     * @return A list of `ProductPurchaseResponseDTO` objects containing details of the purchased products.
     * @throws ProductPurchaseException If any product is not found or if there is insufficient stock for a product.
     */
    @Transactional(rollbackFor = ProductPurchaseException.class)
    @Override
    public List<ProductPurchaseResponseDTO> performPurchaseProducts(
            List<ProductPurchaseRequestDTO> productRequestDTOs
    ) {
        // Map productId to request for quick lookup
        Map<Integer, ProductPurchaseRequestDTO> requestMap = productRequestDTOs
                .stream()
                .collect(
                        Collectors.toMap(ProductPurchaseRequestDTO::productId, dto -> dto)
                );

        // Fetch all products in a single query
        List<Integer> productIds = new ArrayList<>(requestMap.keySet());
        var storedProducts = productRepository.findAllByIdInOrderById(productIds);

        // Validate all products are found
        if (storedProducts.size() != productIds.size()) {
            throw new ProductPurchaseException("Some products not found for purchase");
        }

        // Validate stock and update quantities
        for (Product product : storedProducts) {
            ProductPurchaseRequestDTO request = requestMap.get(product.getId());
            if (product.getAvailableQuantity() < request.quantity()) {
                throw new ProductPurchaseException(
                        "Not enough stock for product ID: " + product.getId()
                );
            }
            product.setAvailableQuantity(product.getAvailableQuantity() - request.quantity());
        }

        // Batch save all updated products
        productRepository.saveAll(storedProducts);

        // Map to response DTOs
        return storedProducts.stream()
                .map(product -> {
                    var quantity = requestMap.get(product.getId()).quantity();
                    return productMapper.productToProductPurchaseResponseDTO(product, quantity);
                })
                .collect(Collectors.toList());

        /*
        The updated `performPurchaseProducts` method is more efficient and maintainable due to these reasons:

        - Map for Fast Lookup: It uses a `Map` to associate product IDs with their purchase requests, allowing O(1) access
            when matching products to requests.
        - Batch Database Operations: It fetches all products in a single query and saves all updates in one batch,
            reducing database round-trips.
        - No Sorting Needed: By using a map, it avoids sorting lists, which saves processing time.
        - Streamlined Logic: The code is more concise and easier to read, making maintenance simpler.

        Procedure Explanation:
        ----------------------
        1. Map Requests: Converts the list of purchase requests into a `Map` keyed by product ID for quick access.
        2. Fetch Products: Retrieves all products matching the requested IDs in a single database call.
        3. Validation: Checks if all requested products exist. If not, throws an exception.
        4. Stock Check & Update: Iterates through each product, checks if enough stock is available, and updates the
            quantity. If stock is insufficient, throws an exception.
        5. Batch Save: Saves all updated products back to the database in one operation.
        6. Prepare Response: Maps each updated product to a response DTO, including the purchased quantity,
            and returns the list.

        This approach minimizes database interactions and improves performance, especially when handling multiple products in a single purchase.
        * */
    }
}
