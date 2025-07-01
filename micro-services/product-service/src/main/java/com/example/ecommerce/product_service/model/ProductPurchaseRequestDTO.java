package com.example.ecommerce.product_service.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductPurchaseRequestDTO(
        @NotNull(message = "Product ID is mandatory")
        Integer productId,

        @Positive(message = "Quantity is mandatory and must be greater than zero")
        double quantity
) {
}
