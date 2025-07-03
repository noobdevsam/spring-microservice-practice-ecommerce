package com.example.ecommerce.order_service.feign_client;

import com.example.ecommerce.order_service.models.PaymentRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "payment-service",
        url = "${application.config.payment-url}")
public interface PaymentFeignClient {

    @PostMapping
    Integer requestOrderPayment(
            @RequestBody PaymentRequestDTO paymentRequestDTO
    );
}
