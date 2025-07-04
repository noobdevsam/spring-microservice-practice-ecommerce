package com.example.ecommerce.customerservice.model;

import com.example.ecommerce.customerservice.entities.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerRequestDTO(
        String id,

        @NotNull(message = "Customer firstname is required")
        String firstName,

        @NotNull(message = "Customer lastname is required")
        String lastName,

        @NotNull(message = "Customer email is required")
        @Email(message = "Customer email is not a valid email address")
        String email,

        Address address
) {
}
