package com.example.ecommerce.order_service.mappers;

import com.example.ecommerce.order_service.entities.CustomerOrder;
import com.example.ecommerce.order_service.entities.OrderLine;
import com.example.ecommerce.order_service.models.OrderLineRequestDTO;
import com.example.ecommerce.order_service.models.OrderLineResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class OrderLineMapper {

    public OrderLine orderRequestDTOToOrderLine(OrderLineRequestDTO orderLineRequestDTO) {
        return OrderLine.builder()
                .id(orderLineRequestDTO.id())
                .productId(orderLineRequestDTO.productId())
                .order(
                        CustomerOrder.builder()
                                .id(orderLineRequestDTO.orderId())
                                .build()
                )
                .quantity(orderLineRequestDTO.quantity())
                .build();
    }

    public OrderLineResponseDTO orderLineToOrderLineResponseDTO(OrderLine orderLine) {
        return new OrderLineResponseDTO(
                orderLine.getId(),
                orderLine.getQuantity()
        );
    }

}
