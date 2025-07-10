package com.example.ecommerce.order_service.controllers;

import com.example.ecommerce.order_service.models.OrderRequestDTO;
import com.example.ecommerce.order_service.models.OrderResponseDTO;
import com.example.ecommerce.order_service.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing orders in the e-commerce application.
 * Provides endpoints for creating, retrieving, and managing orders.
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService; // Service layer for handling order-related operations

    /**
     * Creates a new order based on the provided order request data.
     *
     * @param orderRequestDTO The DTO containing order request details.
     * @return A ResponseEntity containing the ID of the created order.
     */
    @PostMapping
    public ResponseEntity<Integer> createOrder(
            @RequestBody @Valid OrderRequestDTO orderRequestDTO
    ) {
        return ResponseEntity.ok(orderService.createOrder(orderRequestDTO));
    }

    /**
     * Retrieves all orders in the system.
     *
     * @return A ResponseEntity containing a list of OrderResponseDTOs.
     */
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAllOrders());
    }

    /**
     * Retrieves a specific order by its ID.
     *
     * @param orderId The ID of the order to retrieve.
     * @return A ResponseEntity containing the OrderResponseDTO for the specified order.
     */
    @GetMapping("/{order-id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(
            @PathVariable("order-id") Integer orderId
    ) {
        return ResponseEntity.ok(orderService.findOrderById(orderId));
    }

}