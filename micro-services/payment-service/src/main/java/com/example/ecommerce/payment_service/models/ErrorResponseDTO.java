package com.example.ecommerce.payment_service.models;

import java.util.Map;

public record ErrorResponseDTO(
        Map<String, String> errors
) {
}
