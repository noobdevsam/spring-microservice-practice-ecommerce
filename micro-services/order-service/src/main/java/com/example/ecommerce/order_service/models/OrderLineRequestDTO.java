package com.example.ecommerce.order_service.models;

public record OrderLineRequestDTO(
        Integer id,
        Integer orderId,
        Integer productId,
        Double quantity
) {
}
