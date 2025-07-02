package com.example.ecommerce.order_service.models;

import java.util.Map;

public record ErrorResponseDTO(
        Map<String, String> errors
) {
}
