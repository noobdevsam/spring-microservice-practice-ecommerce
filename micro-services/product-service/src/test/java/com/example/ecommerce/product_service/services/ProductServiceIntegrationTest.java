package com.example.ecommerce.product_service.services;

import com.example.ecommerce.product_service.entities.Category;
import com.example.ecommerce.product_service.exceptions.ProductPurchaseException;
import com.example.ecommerce.product_service.model.ProductPurchaseRequestDTO;
import com.example.ecommerce.product_service.model.ProductRequestDTO;
import com.example.ecommerce.product_service.repository.CategoryRepository;
import com.example.ecommerce.product_service.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        category = categoryRepository.save(
                Category.builder()
                        .name("cat")
                        .description("desc")
                        .build()
        );
    }

    @Test
    @DisplayName("Should create and fetch product by id")
    void createAndGetProductById() {
        var req = new ProductRequestDTO(null, "prod", "desc", 10, BigDecimal.TEN, category.getId());
        var id = productService.createProduct(req);
        var dto = productService.getProductById(id);

        assertEquals("prod", dto.name());
        assertEquals(10, dto.availableQuantity());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException for missing product")
    void getProductById_notFound() {
        assertThrows(
                EntityNotFoundException.class, () -> productService.getProductById(9999)
        );
    }

    @Test
    @DisplayName("Should return all products")
    void getAllProducts() {
        productService.createProduct(
                new ProductRequestDTO(null, "p1", "d1", 5, BigDecimal.ONE, category.getId())
        );
        productService.createProduct(
                new ProductRequestDTO(null, "p2", "d2", 7, BigDecimal.TEN, category.getId())
        );

        var all = productService.getAllProducts();

        assertTrue(all.size() >= 2);
    }

    @Test
    @DisplayName("Should perform purchase and update quantity")
    void performPurchaseProducts_success() {
        var id = productService.createProduct(new ProductRequestDTO(null, "p1", "d1", 5, BigDecimal.ONE, category.getId()));

        productService.performPurchaseProducts(
                List.of(new ProductPurchaseRequestDTO(id, 2))
        );

        var p = productRepository.findById(id)
                .orElseThrow();

        assertEquals(3, p.getAvailableQuantity());
    }

    @Test
    @DisplayName("Should throw ProductPurchaseException if product not found for purchase")
    void performPurchaseProducts_productNotFound() {
        assertThrows(
                ProductPurchaseException.class,
                () -> productService.performPurchaseProducts(
                        List.of(new ProductPurchaseRequestDTO(9999, 1))
                )
        );
    }

    @Test
    @DisplayName("Should throw ProductPurchaseException if insufficient quantity")
    void performPurchaseProducts_insufficientQuantity() {
        var id = productService.createProduct(
                new ProductRequestDTO(null, "p1", "d1", 1, BigDecimal.ONE, category.getId())
        );

        assertThrows(
                ProductPurchaseException.class,
                () -> productService.performPurchaseProducts(
                        List.of(new ProductPurchaseRequestDTO(id, 2))
                )
        );
    }

    @Test
    @DisplayName("Should handle empty purchase request list")
    void performPurchaseProducts_emptyList() {
        assertThrows(
                ProductPurchaseException.class,
                () -> productService.performPurchaseProducts(List.of())
        );
    }

    @Test
    @DisplayName("Should allow purchase of all available quantity (edge case)")
    void performPurchaseProducts_exactQuantity() {
        var id = productService.createProduct(
                new ProductRequestDTO(null, "p1", "d1", 3, BigDecimal.ONE, category.getId())
        );

        productService.performPurchaseProducts(
                List.of(new ProductPurchaseRequestDTO(id, 3))
        );

        var p = productRepository.findById(id)
                .orElseThrow();

        assertEquals(0, p.getAvailableQuantity());
    }

    @Test
    @DisplayName("Should not allow negative or zero quantity purchase (edge case)")
    void performPurchaseProducts_negativeOrZeroQuantity() {
        var id = productService.createProduct(
                new ProductRequestDTO(null, "p1", "d1", 3, BigDecimal.ONE, category.getId())
        );

        assertThrows(
                ProductPurchaseException.class,
                () -> productService.performPurchaseProducts(
                        List.of(new ProductPurchaseRequestDTO(id, 0))
                )
        );
        assertThrows(
                ProductPurchaseException.class,
                () -> productService.performPurchaseProducts(
                        List.of(new ProductPurchaseRequestDTO(id, -1))
                )
        );
    }

}