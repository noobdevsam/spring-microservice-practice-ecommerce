package com.example.ecommerce.product_service.controllers;

import com.example.ecommerce.product_service.model.ProductPurchaseRequestDTO;
import com.example.ecommerce.product_service.model.ProductRequestDTO;
import com.example.ecommerce.product_service.model.ProductResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProductControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    private ProductRequestDTO validProductRequest;

    @BeforeEach
    void setup() {
        validProductRequest = new ProductRequestDTO(
                null, "Integration Product", "Desc", 20, BigDecimal.valueOf(99.99), 1
        );
    }

    @Test
    @DisplayName("Create and fetch product")
    void createAndFetchProduct() {
        ResponseEntity<Integer> createResp = restTemplate.postForEntity(
                "/api/v1/products", validProductRequest, Integer.class);

        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        Integer id = createResp.getBody();

        assertThat(id).isNotNull();

        ResponseEntity<ProductResponseDTO> getResp = restTemplate.getForEntity(
                "/api/v1/products/" + id, ProductResponseDTO.class);

        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResp.getBody()).isNotNull();
        assertThat(getResp.getBody().name()).isEqualTo("Integration Product");
    }

    @Test
    @DisplayName("Get all products returns list")
    void getAllProducts() {
        ResponseEntity<List<ProductResponseDTO>> resp = restTemplate.exchange(
                "/api/v1/products", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isInstanceOf(List.class);
    }

    @Test
    @DisplayName("Get product by invalid id returns 400")
    void getProductByInvalidId() {
        ResponseEntity<String> resp = restTemplate.getForEntity(
                "/api/v1/products/999999", String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).contains("not found");
    }

    @Test
    @DisplayName("Create product with invalid data returns 400")
    void createProductInvalid() {
        ProductRequestDTO invalid = new ProductRequestDTO(null, "", "", -1, null, null);
        ResponseEntity<String> resp = restTemplate.postForEntity(
                "/api/v1/products", invalid, String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).contains("errors");
    }

    @Test
    @DisplayName("Purchase products with valid and invalid data")
    void purchaseProducts() {
        // Create a product first
        Integer id = restTemplate.postForEntity(
                "/api/v1/products", validProductRequest, Integer.class).getBody();
        ProductPurchaseRequestDTO validPurchase = new ProductPurchaseRequestDTO(id, 2);
        // Valid purchase
        ResponseEntity<String> resp = restTemplate.postForEntity(
                "/api/v1/products/purchase", List.of(validPurchase), String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Invalid purchase (invalid product id)
        ProductPurchaseRequestDTO invalidPurchase = new ProductPurchaseRequestDTO(999999, 1);
        ResponseEntity<String> resp2 = restTemplate.postForEntity(
                "/api/v1/products/purchase", List.of(invalidPurchase), String.class);
        assertThat(resp2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Purchase with empty list returns 400")
    void purchaseWithEmptyList() {
        ResponseEntity<String> resp = restTemplate.postForEntity(
                "/api/v1/products/purchase", Collections.emptyList(), String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}

