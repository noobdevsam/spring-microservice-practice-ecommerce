package com.example.ecommerce.product_service.model;

import java.util.Map;

public record ErrorResponseDTO(
        Map<String, String> errors
) {
}
