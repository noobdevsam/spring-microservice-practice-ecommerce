package com.example.ecommerce.order_service.mappers;

import com.example.ecommerce.order_service.entities.CustomerOrder;
import com.example.ecommerce.order_service.models.OrderRequestDTO;
import com.example.ecommerce.order_service.models.OrderResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public CustomerOrder orderRequestDTOToOrder(OrderRequestDTO orderRequestDTO) {
        if (orderRequestDTO == null) {
            return null;
        }

        return CustomerOrder.builder()
                .id(orderRequestDTO.id())
                .reference(orderRequestDTO.reference())
                .paymentMethod(orderRequestDTO.paymentMethod())
                .customerId(orderRequestDTO.customerId())
                .build();
    }

    public OrderResponseDTO orderToOrderResponseDTO(CustomerOrder order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getReference(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                order.getCustomerId()
        );
    }

}
