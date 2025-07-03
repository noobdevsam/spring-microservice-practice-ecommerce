package com.example.ecommerce.order_service.services;

import com.example.ecommerce.order_service.models.OrderRequestDTO;
import com.example.ecommerce.order_service.models.OrderResponseDTO;

import java.util.List;

public interface OrderService {

    Integer createOrder(OrderRequestDTO orderRequestDTO);

    List<OrderResponseDTO> findAllOrders();

    OrderResponseDTO findOrderById(Integer id);

}
