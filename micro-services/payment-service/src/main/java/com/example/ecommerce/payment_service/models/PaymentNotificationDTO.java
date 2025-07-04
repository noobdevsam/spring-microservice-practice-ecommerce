package com.example.ecommerce.payment_service.models;

import java.math.BigDecimal;

public record PaymentNotificationDTO(
        String orderReference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String customerFirstName,
        String customerLastName,
        String customerEmail
) {
}
