package com.example.ecommerce.payment_service.models;

import java.math.BigDecimal;

public record PaymentNotificationRequestDTO(
        String orderReference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String customerFirstName,
        String customerLastName,
        String customerEmail
) {
}
