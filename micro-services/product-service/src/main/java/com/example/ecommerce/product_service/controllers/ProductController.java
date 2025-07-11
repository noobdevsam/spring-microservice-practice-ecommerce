package com.example.ecommerce.product_service.controllers;

import com.example.ecommerce.product_service.model.ProductPurchaseRequestDTO;
import com.example.ecommerce.product_service.model.ProductPurchaseResponseDTO;
import com.example.ecommerce.product_service.model.ProductRequestDTO;
import com.example.ecommerce.product_service.model.ProductResponseDTO;
import com.example.ecommerce.product_service.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing product-related operations.
 * Provides endpoints for retrieving, creating, and purchasing products.
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Retrieves all products.
     *
     * @return ResponseEntity containing a list of ProductResponseDTO objects.
     */
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAllProducts() {
        return ResponseEntity.ok(
                productService.getAllProducts()
        );
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param productId The ID of the product to retrieve.
     * @return ResponseEntity containing the ProductResponseDTO object.
     */
    @GetMapping("/{product-id}")
    public ResponseEntity<ProductResponseDTO> findProductById(
            @PathVariable("product-id") Integer productId
    ) {
        return ResponseEntity.ok(
                productService.getProductById(productId)
        );
    }

    /**
     * Creates a new product.
     *
     * @param productRequestDTO The request body containing product details.
     * @return ResponseEntity containing the ID of the created product.
     */
    @PostMapping
    public ResponseEntity<Integer> createProduct(
            @RequestBody @Valid ProductRequestDTO productRequestDTO
    ) {
        return ResponseEntity.ok(
                productService.createProduct(productRequestDTO)
        );
    }

    /**
     * Purchases multiple products.
     *
     * @param productPurchaseRequestDTOs A list of ProductPurchaseRequestDTO objects representing the products to purchase.
     * @return ResponseEntity containing a list of ProductPurchaseResponseDTO objects.
     */
    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponseDTO>> purchaseProducts(
            @Valid @RequestBody List<@Valid ProductPurchaseRequestDTO> productPurchaseRequestDTOs
    ) {
        return ResponseEntity.ok(
                productService.performPurchaseProducts(productPurchaseRequestDTOs)
        );
    }
}
