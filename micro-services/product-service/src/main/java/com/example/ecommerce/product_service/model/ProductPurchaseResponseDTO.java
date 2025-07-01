package com.example.ecommerce.product_service.model;

import java.math.BigDecimal;

public record ProductPurchaseResponseDTO(
        Integer productId,
        String name,
        String description,
        BigDecimal price,
        double quantity
) {
}
