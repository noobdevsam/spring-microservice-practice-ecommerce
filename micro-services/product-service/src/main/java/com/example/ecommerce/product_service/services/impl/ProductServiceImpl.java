package com.example.ecommerce.product_service.services.impl;

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
import java.util.Comparator;
import java.util.List;
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
        // Extract product IDs from the purchase request DTOs
        var productIds = productRequestDTOs.stream()
                .map(ProductPurchaseRequestDTO::productId)
                .toList();

        // Retrieve products from the repository based on the provided IDs
        var storedProducts = productRepository.findAllByIdInOrderById(productIds);

        // Check if all requested products are found
        if (productIds.size() != storedProducts.size()) {
            throw new ProductPurchaseException("Some products not found for purchase");
        }

        // Sort the purchase requests by product ID for consistent processing
        var storedRequests = productRequestDTOs
                .stream()
                .sorted(
                        Comparator.comparing(ProductPurchaseRequestDTO::productId)
                )
                .toList();

        // Initialize a list to store the response DTOs for purchased products
        var purchasedProducts = new ArrayList<ProductPurchaseResponseDTO>();

        // Process each product and its corresponding purchase request
        for (int i = 0; i < storedProducts.size(); i++) {
            var storedProduct = storedProducts.get(i);
            var productRequest = storedRequests.get(i);

            // Check if there is enough stock for the requested quantity
            if (storedProduct.getAvailableQuantity() < productRequest.quantity()) {
                throw new ProductPurchaseException(
                        "Not enough stock for product ID: " + productRequest.productId()
                );
            }

            // Update the available quantity of the product
            var newAvailableQuantity = storedProduct.getAvailableQuantity() - productRequest.quantity();
            storedProduct.setAvailableQuantity(newAvailableQuantity);

            // Save the updated product back to the repository
            productRepository.save(storedProduct);

            // Add the purchase details to the response list
            purchasedProducts.add(
                    productMapper.productToProductPurchaseResponseDTO(
                            storedProduct,
                            productRequest.quantity()
                    )
            );
        }

        // Return the list of purchased product details
        return purchasedProducts;
    }
}
