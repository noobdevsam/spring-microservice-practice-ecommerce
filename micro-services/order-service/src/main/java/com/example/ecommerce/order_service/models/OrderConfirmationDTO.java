package com.example.ecommerce.order_service.models;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmationDTO(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponseDTO customer,
        List<ProductPurchaseResponseDTO> purchases
) {
}
