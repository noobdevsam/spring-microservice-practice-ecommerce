package com.example.ecommerce.customerservice.model;

import java.util.Map;
import java.util.Collections;

public record ErrorResponseDTO(
        Map<String, String> errors
) {
    public ErrorResponseDTO(Map<String, String> errors) {
        this.errors = Collections.unmodifiableMap(errors);
    }
}
