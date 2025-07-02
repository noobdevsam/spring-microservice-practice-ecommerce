package com.example.ecommerce.order_service.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderRequestDTO(
        Integer id,
        String reference,

        @Positive(message = "Total amount must be positive")
        BigDecimal amount,

        @NotNull(message = "Payment method should be precised")
        PaymentMethod paymentMethod,

        @NotNull(message = "Customer ID must not be null")
        @NotEmpty(message = "Customer ID cannot be empty")
        @NotBlank(message = "Customer ID cannot be blank")
        String customerId,

        @NotEmpty(message = "You should at least purchase one product")
        List<ProductPurchaseRequestDTO> purchases
) {
}
