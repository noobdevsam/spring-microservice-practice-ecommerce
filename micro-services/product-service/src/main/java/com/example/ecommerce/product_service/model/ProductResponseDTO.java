package com.example.ecommerce.product_service.model;

import java.math.BigDecimal;

public record ProductResponseDTO(
        Integer id,
        String name,
        String description,
        double availableQuantity,
        BigDecimal price,
        Integer categoryId,
        String categoryName,
        String categoryDescription
) {
}
