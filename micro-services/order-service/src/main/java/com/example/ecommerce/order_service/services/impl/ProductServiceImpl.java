package com.example.ecommerce.order_service.services.impl;

import com.example.ecommerce.order_service.exceptions.BusinessException;
import com.example.ecommerce.order_service.models.ProductPurchaseRequestDTO;
import com.example.ecommerce.order_service.models.ProductPurchaseResponseDTO;
import com.example.ecommerce.order_service.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final RestTemplate restTemplate;

    @Value("${application.config.product-url}")
    private String productUrl;

    @Override
    public List<ProductPurchaseResponseDTO> executePurchaseProducts(List<ProductPurchaseRequestDTO> requestBody) {
        var headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<List<ProductPurchaseRequestDTO>> requestEntity = new HttpEntity<>(requestBody, headers);
        ParameterizedTypeReference<List<ProductPurchaseResponseDTO>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<ProductPurchaseResponseDTO>> responseEntity = restTemplate
                .exchange(
                        productUrl + "/purchase",
                        HttpMethod.POST,
                        requestEntity,
                        responseType
                );

        if (responseEntity.getStatusCode().isError()) {
            throw new BusinessException("An error occurred while processing the product purchase request: " + responseEntity.getStatusCode());
        }

        return responseEntity.getBody();
    }
}
