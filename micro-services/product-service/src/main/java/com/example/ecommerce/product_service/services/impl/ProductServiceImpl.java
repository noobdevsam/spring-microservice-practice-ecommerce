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

/**
 * Implementation of the ProductService interface.
 * Provides business logic for managing products and handling product purchases.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return ProductResponseDTO containing the product details.
     * @throws EntityNotFoundException If the product with the given ID is not found.
     */
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

    /**
     * Retrieves all products.
     *
     * @return A list of ProductResponseDTO objects containing details of all products.
     */
    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository
                .findAll()
                .stream()
                .map(productMapper::productToProductResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new product.
     *
     * @param productRequestDTO The ProductRequestDTO containing the details of the product to create.
     * @return The ID of the newly created product.
     */
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

        if (productRequestDTOs == null || productRequestDTOs.isEmpty()) {
            throw new ProductPurchaseException("Product purchase request list cannot be null or empty");
        }

        // Validate purchase quantities
        for (ProductPurchaseRequestDTO dto : productRequestDTOs) {
            if (dto.quantity() <= 0) {
                throw new ProductPurchaseException("Purchase quantity must be positive for product ID: " + dto.productId());
            }
        }

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
    }
}
