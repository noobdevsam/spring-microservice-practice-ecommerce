package com.example.ecommerce.notification_service.models;

import java.math.BigDecimal;

public record ProductDTO(
        Integer productId,
        String name,
        String description,
        BigDecimal price,
        Double quantity
) {
}
