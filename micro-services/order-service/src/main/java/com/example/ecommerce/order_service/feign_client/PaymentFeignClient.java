package com.example.ecommerce.order_service.feign_client;

import com.example.ecommerce.order_service.models.PaymentRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client interface for interacting with the payment service.
 * Provides methods for initiating payment requests via REST API calls.
 */
@FeignClient(
        name = "payment-service", // Specifies the name of the payment service.
        url = "${application.config.payment-url}" // Configures the base URL for the payment service.
)
public interface PaymentFeignClient {

    /**
     * Sends a payment request for an order to the payment service.
     *
     * @param paymentRequestDTO The DTO containing payment request details.
     * @return The ID of the payment transaction as an Integer.
     */
    @PostMapping
    Integer requestOrderPayment(
            @RequestBody PaymentRequestDTO paymentRequestDTO // Maps the payment request body.
    );
}
