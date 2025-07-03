package com.example.ecommerce.order_service.feign_client;

import com.example.ecommerce.order_service.models.CustomerResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
        name = "customer-service",
        url = "${application.config.customer-url}"
)
public interface CustomerFeignClient {

    @GetMapping("/{customer-id}")
    Optional<CustomerResponseDTO> findCustomerById(
            @PathVariable("customer-id") String customerId
    );

}

// Starting with Spring Framework 6.1 and Spring Boot 3.2, you can use the @HttpExchange, @GetExchange and HTTP interfaces as an alternative
// to Feign clients. This approach uses the built-in RestClient and does not require the OpenFeign dependency.
