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

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAllProducts() {
        return ResponseEntity.ok(
                productService.getAllProducts()
        );
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductResponseDTO> findProductById(
            @PathVariable("product-id") Integer productId
    ) {
        return ResponseEntity.ok(
                productService.getProductById(productId)
        );
    }

    @PostMapping
    public ResponseEntity<Integer> createProduct(
            @RequestBody @Valid ProductRequestDTO productRequestDTO
    ) {
        return ResponseEntity.ok(
                productService.createProduct(productRequestDTO)
        );
    }

    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponseDTO>> purchaseProducts(
            @RequestBody List<@Valid ProductPurchaseRequestDTO> productPurchaseRequestDTOs
    ) {
        return ResponseEntity.ok(
                productService.performPurchaseProducts(productPurchaseRequestDTOs)
        );
    }
}
