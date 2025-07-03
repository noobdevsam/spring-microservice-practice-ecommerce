package com.example.ecommerce.order_service.services;

import com.example.ecommerce.order_service.models.OrderLineRequestDTO;
import com.example.ecommerce.order_service.models.OrderLineResponseDTO;

import java.util.List;

public interface OrderLineService {

    Integer saveOrderLine(OrderLineRequestDTO orderLineRequestDTO);

    List<OrderLineResponseDTO> findAllByOrderId(Integer orderId);

}
