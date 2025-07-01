package com.example.ecommerce.product_service.controllers;

import com.example.ecommerce.product_service.exceptions.ProductPurchaseException;
import com.example.ecommerce.product_service.model.ProductPurchaseRequestDTO;
import com.example.ecommerce.product_service.model.ProductPurchaseResponseDTO;
import com.example.ecommerce.product_service.model.ProductRequestDTO;
import com.example.ecommerce.product_service.model.ProductResponseDTO;
import com.example.ecommerce.product_service.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductResponseDTO responseDTO;
    private ProductRequestDTO requestDTO;
    private ProductPurchaseRequestDTO purchaseRequestDTO;
    private ProductPurchaseResponseDTO purchaseResponseDTO;

    @BeforeEach
    void setUp() {
        responseDTO = new ProductResponseDTO(
                1, "Test Product", "Description", 10, BigDecimal.TEN, 1, "Category", "CatDesc"
        );

        requestDTO = new ProductRequestDTO(
                1, "Test Product", "Description", 10, BigDecimal.TEN, 1
        );

        purchaseRequestDTO = new ProductPurchaseRequestDTO(1, 2);

        purchaseResponseDTO = new ProductPurchaseResponseDTO(
                1, "Test Product", "sdjfsksl", BigDecimal.TEN, 2);
    }

    @Test
    @DisplayName("GET /api/v1/products/{product-id} - Success")
    void getProductById_success() throws Exception {
        Mockito.when(
                productService.getProductById(responseDTO.id())
        ).thenReturn(responseDTO);

        mockMvc.perform(
                        get("/api/v1/products/{product-id}", responseDTO.id())
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/products/{product-id} - Not Found")
    void getProductById_notFound() throws Exception {
        int productId = 999;

        Mockito.when(
                productService.getProductById(productId)
        ).thenThrow(
                new EntityNotFoundException("Product not found")
        );

        mockMvc.perform(
                        get("/api/v1/products/{product-id}", productId)
                )
                .andExpect(status().isBadRequest());// Expect 404 if handled by GlobalExceptionHandler
    }

    @Test
    @DisplayName("POST /api/v1/products - Success")
    void createProduct_success() throws Exception {
        Mockito.when(
                productService.createProduct(any(ProductRequestDTO.class))
        ).thenReturn(1);

        mockMvc.perform(
                        post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("POST /api/v1/products - Invalid Input")
    void createProduct_invalidInput() throws Exception {
        // All fields null to trigger validation error
        var dto = new ProductRequestDTO(null, null, null, 0, null, null);

        mockMvc.perform(
                        post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/products/purchase - Success")
    void purchaseProducts_success() throws Exception {
        var requestList = List.of(
                new ProductPurchaseRequestDTO(1, 2.0)
        );

        var responseList = Collections.singletonList(purchaseResponseDTO);

        Mockito.when(
                productService.performPurchaseProducts(any(List.class))
        ).thenReturn(responseList);

        mockMvc.perform(
                        post("/api/v1/products/purchase")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestList))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @DisplayName("POST /api/v1/products/purchase - Empty List")
    void purchaseProducts_emptyList() throws Exception {
        Mockito.when(
                productService.performPurchaseProducts(any(List.class))
        ).thenThrow(new ProductPurchaseException("Product purchase request list cannot be null or empty"));

        mockMvc.perform(
                        post("/api/v1/products/purchase")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(Collections.emptyList()))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/products/purchase - Invalid Input")
    void purchaseProducts_invalidInput() throws Exception {
        mockMvc.perform(
                        post("/api/v1/products/purchase")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("[{}]")
                )
                .andExpect(status().isBadRequest());
    }

}
