package com.example.ecommerce.notification_service.models;

import java.math.BigDecimal;

public record PaymentConfirmationDTO(
        String orderReference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String customerFirstName,
        String customerLastName,
        String customerEmail
) {
}
