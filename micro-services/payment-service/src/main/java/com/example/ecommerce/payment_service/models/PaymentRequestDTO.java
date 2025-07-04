package com.example.ecommerce.payment_service.models;

import java.math.BigDecimal;

public record PaymentRequestDTO(
        Integer id,
        BigDecimal amount,
        Integer orderId,
        PaymentMethod paymentMethod,
        String orderReference,
        CustomerDTO customerDTO
) {
}
