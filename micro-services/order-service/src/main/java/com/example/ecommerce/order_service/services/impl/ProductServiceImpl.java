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

/**
 * Implementation of the ProductService interface.
 * Handles product purchase operations by communicating with an external product service.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final RestTemplate restTemplate;

    /**
     * The base URL of the external product service.
     * Configured via application properties.
     */
    @Value("${application.config.product-url}")
    private String productUrl;

    /**
     * Executes the purchase of products by sending a POST request to the external product service.
     *
     * @param requestBody A list of ProductPurchaseRequestDTO objects containing the purchase details.
     * @return A list of ProductPurchaseResponseDTO objects containing the response from the product service.
     * @throws BusinessException If an error occurs during the request or the response indicates a failure.
     */
    @Override
    public List<ProductPurchaseResponseDTO> executePurchaseProducts(List<ProductPurchaseRequestDTO> requestBody) {
        // Create HTTP headers and set the content type to JSON
        var headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Wrap the request body and headers into an HttpEntity
        HttpEntity<List<ProductPurchaseRequestDTO>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Define the expected response type as a list of ProductPurchaseResponseDTO
        ParameterizedTypeReference<List<ProductPurchaseResponseDTO>> responseType = new ParameterizedTypeReference<>() {
        };

        // Send the POST request to the external product service
        ResponseEntity<List<ProductPurchaseResponseDTO>> responseEntity = restTemplate
                .exchange(
                        productUrl + "/purchase",
                        HttpMethod.POST,
                        requestEntity,
                        responseType
                );

        // Check if the response indicates an error and throw a BusinessException if necessary
        if (responseEntity.getStatusCode().isError()) {
            throw new BusinessException("An error occurred while processing the product purchase request: " + responseEntity.getStatusCode());
        }

        // Return the response body containing the purchase results
        return responseEntity.getBody();
    }
}
