package com.example.ecommerce.order_service.models;

public record CustomerResponseDTO(
        String id,
        String firstName,
        String lastName,
        String email
) {
}
