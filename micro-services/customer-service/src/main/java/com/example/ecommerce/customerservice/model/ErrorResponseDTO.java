package com.example.ecommerce.customerservice.model;

import java.util.Map;

public record ErrorResponseDTO(
        Map<String, String> errors
) {
}
