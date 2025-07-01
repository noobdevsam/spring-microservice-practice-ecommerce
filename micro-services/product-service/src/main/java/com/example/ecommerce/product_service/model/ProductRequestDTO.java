package com.example.ecommerce.product_service.model;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequestDTO(
        Integer id,

        @NotNull(message = "Product name is required")
        String name,

        @NotNull(message = "Product description is required")
        String description,

        @NotNull(message = "Available quantity should be positive")
        double availableQuantity,

        @NotNull(message = "Product price should be positive")
        BigDecimal price,

        @NotNull(message = "Product category ID is required")
        Integer categoryId
) {
}
