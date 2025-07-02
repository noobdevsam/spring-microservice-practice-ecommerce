package com.example.ecommerce.order_service.models;

import java.math.BigDecimal;

public record ProductPurchaseResponseDTO(
        Integer productId,
        String name,
        String description,
        BigDecimal price,
        double quantity
) {
}
