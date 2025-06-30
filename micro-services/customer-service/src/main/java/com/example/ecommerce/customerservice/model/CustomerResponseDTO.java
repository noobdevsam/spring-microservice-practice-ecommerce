package com.example.ecommerce.customerservice.model;

import com.example.ecommerce.customerservice.entities.Address;

public record CustomerResponseDTO(
        String id,
        String firstName,
        String lastName,
        String email,
        Address address
) {
}
