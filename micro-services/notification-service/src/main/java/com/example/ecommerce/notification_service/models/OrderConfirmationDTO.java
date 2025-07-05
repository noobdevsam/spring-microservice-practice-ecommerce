package com.example.ecommerce.notification_service.models;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmationDTO(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerDTO customerDTO,
        List<ProductDTO> productDTOs
) {
}
