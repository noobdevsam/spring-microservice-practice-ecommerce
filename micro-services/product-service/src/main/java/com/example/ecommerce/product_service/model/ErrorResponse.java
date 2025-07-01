package com.example.ecommerce.product_service.model;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
) {
}
