package com.example.ecommerce.product_service.services;

import com.example.ecommerce.product_service.entities.Category;
import com.example.ecommerce.product_service.entities.Product;
import com.example.ecommerce.product_service.exceptions.ProductPurchaseException;
import com.example.ecommerce.product_service.mapper.ProductMapper;
import com.example.ecommerce.product_service.model.ProductPurchaseRequestDTO;
import com.example.ecommerce.product_service.model.ProductPurchaseResponseDTO;
import com.example.ecommerce.product_service.model.ProductRequestDTO;
import com.example.ecommerce.product_service.model.ProductResponseDTO;
import com.example.ecommerce.product_service.repository.ProductRepository;
import com.example.ecommerce.product_service.services.impl.ProductServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return product by id")
    void getProductById_found() {
        var product = Product.builder()
                .id(1)
                .name("Test")
                .description("desc")
                .availableQuantity(10)
                .price(BigDecimal.TEN)
                .category(
                        Category.builder()
                                .id(2)
                                .name("cat")
                                .description("catdesc")
                                .build()
                ).build();
        var dto = new ProductResponseDTO(1, "Test", "desc", 10, BigDecimal.TEN, 2, "cat", "catdesc");

        when(
                productRepository.findById(1)
        ).thenReturn(
                Optional.of(product)
        );
        when(
                productMapper.productToProductResponseDTO(product)
        ).thenReturn(dto);

        var result = productService.getProductById(1);

        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals("Test", result.name());
    }

    @Test
    @DisplayName("Should throw when product not found by id")
    void getProductById_notFound() {
        when(
                productRepository.findById(99)
        ).thenReturn(
                Optional.empty()
        );

        assertThrows(
                EntityNotFoundException.class, () -> productService.getProductById(99)
        );
    }

    @Test
    @DisplayName("Should return all products")
    void getAllProducts() {
        var p1 = Product.builder()
                .id(1)
                .name("A")
                .description("d")
                .availableQuantity(1)
                .price(BigDecimal.ONE)
                .category(
                        Category.builder()
                                .id(1)
                                .name("c")
                                .description("cd")
                                .build()
                ).build();
        var p2 = Product.builder()
                .id(2)
                .name("B")
                .description("d2")
                .availableQuantity(2)
                .price(BigDecimal.TEN)
                .category(
                        Category.builder()
                                .id(2)
                                .name("c2")
                                .description("cd2")
                                .build()
                ).build();
        var products = Arrays.asList(p1, p2);

        when(
                productRepository.findAll()
        ).thenReturn(products);
        when(
                productMapper.productToProductResponseDTO(any(Product.class))
        ).thenReturn(
                new ProductResponseDTO(0, "", "", 0, BigDecimal.ZERO, 0, "", "")
        );

        var result = productService.getAllProducts();

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should create product and return id")
    void createProduct() {
        var req = new ProductRequestDTO(1, "n", "d", 5, BigDecimal.ONE, 2);
        var product = Product.builder()
                .id(10)
                .build();

        when(
                productMapper.productRequestDTOtoProduct(req)
        ).thenReturn(product);
        when(
                productRepository.save(product)
        ).thenReturn(product);

        assertEquals(10, productService.createProduct(req));
    }

    @Test
    @DisplayName("Should perform purchase for valid products")
    void performPurchaseProducts_success() {
        var req = new ProductPurchaseRequestDTO(1, 2);
        var product = Product.builder()
                .id(1)
                .name("n")
                .description("d")
                .availableQuantity(5)
                .price(BigDecimal.ONE)
                .category(
                        Category.builder()
                                .id(1).build()
                ).build();
        var products = List.of(product);

        when(
                productRepository.findAllByIdInOrderById(List.of(1))
        ).thenReturn(products);
        when(
                productRepository.saveAll(products)
        ).thenReturn(products);

        var resp = new ProductPurchaseResponseDTO(1, "n", "d", BigDecimal.ONE, 2);

        when(
                productMapper.productToProductPurchaseResponseDTO(product, 2)
        ).thenReturn(resp);

        var result = productService.performPurchaseProducts(List.of(req));

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).productId());
        assertEquals(2, result.get(0).quantity());
    }

    @Test
    @DisplayName("Should throw if any product not found")
    void performPurchaseProducts_productNotFound() {
        var req = new ProductPurchaseRequestDTO(99, 1);

        when(
                productRepository.findAllByIdInOrderById(List.of(99))
        ).thenReturn(
                Collections.emptyList()
        );

        assertThrows(
                ProductPurchaseException.class, () -> productService.performPurchaseProducts(List.of(req))
        );
    }

    @Test
    @DisplayName("Should throw if insufficient quantity")
    void performPurchaseProducts_insufficientQuantity() {
        var req = new ProductPurchaseRequestDTO(1, 10);
        var product = Product.builder()
                .id(1)
                .availableQuantity(5)
                .build();

        when(
                productRepository.findAllByIdInOrderById(List.of(1))
        ).thenReturn(
                List.of(product)
        );

        assertThrows(
                ProductPurchaseException.class, () -> productService.performPurchaseProducts(List.of(req))
        );
    }

    @Test
    @DisplayName("Should handle empty purchase request list")
    void performPurchaseProducts_emptyList() {
        var result = productService.performPurchaseProducts(
                Collections.emptyList()
        );

        assertTrue(result.isEmpty());
    }

}
