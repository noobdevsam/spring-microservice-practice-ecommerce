package com.example.ecommerce.order_service.feign_client;

import com.example.ecommerce.order_service.models.CustomerResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * Feign client interface for interacting with the customer service.
 * Provides methods for retrieving customer information via REST API calls.
 */
@FeignClient(
        name = "customer-service", // Specifies the name of the customer service.
        url = "${application.config.customer-url}" // Configures the base URL for the customer service.
)
public interface CustomerFeignClient {

    /**
     * Retrieves customer details by customer ID.
     *
     * @param customerId The ID of the customer to retrieve.
     * @return An Optional containing the CustomerResponseDTO if the customer exists, or empty if not found.
     */
    @GetMapping("/{customer-id}")
    Optional<CustomerResponseDTO> findCustomerById(
            @PathVariable("customer-id") String customerId // Maps the customer ID path variable.
    );

}

// Starting with Spring Framework 6.1 and Spring Boot 3.2, you can use the @HttpExchange, @GetExchange and HTTP interfaces as an alternative
// to Feign clients. This approach uses the built-in RestClient and does not require the OpenFeign dependency.
