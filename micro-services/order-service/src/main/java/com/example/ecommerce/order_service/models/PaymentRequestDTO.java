package com.example.ecommerce.order_service.models;

import java.math.BigDecimal;

public record PaymentRequestDTO(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponseDTO customer
) {
}
