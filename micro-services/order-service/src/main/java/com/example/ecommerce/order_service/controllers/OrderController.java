package com.example.ecommerce.order_service.controllers;

import com.example.ecommerce.order_service.models.OrderRequestDTO;
import com.example.ecommerce.order_service.models.OrderResponseDTO;
import com.example.ecommerce.order_service.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Integer> createOrder(
            @RequestBody @Valid OrderRequestDTO orderRequestDTO
    ) {
        return ResponseEntity.ok(orderService.createOrder(orderRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAllOrders());
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(
            @PathVariable("order-id") Integer orderId
    ) {
        return ResponseEntity.ok(orderService.findOrderById(orderId));
    }

}
