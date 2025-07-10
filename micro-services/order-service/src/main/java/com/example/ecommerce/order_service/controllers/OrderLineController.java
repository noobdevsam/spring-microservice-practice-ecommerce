package com.example.ecommerce.order_service.controllers;

import com.example.ecommerce.order_service.models.OrderLineResponseDTO;
import com.example.ecommerce.order_service.services.OrderLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing order lines in the e-commerce application.
 * Provides endpoints for retrieving order line details.
 */
@RestController
@RequestMapping("/api/v1/order-lines")
@RequiredArgsConstructor
public class OrderLineController {

    private final OrderLineService orderLineService; // Service layer for handling order line-related operations

    /**
     * Retrieves all order lines associated with a specific order ID.
     *
     * @param orderId The ID of the order whose order lines are to be retrieved.
     * @return A ResponseEntity containing a list of OrderLineResponseDTOs.
     */
    @GetMapping("/order/{order-id}")
    public ResponseEntity<List<OrderLineResponseDTO>> findByOrderId(
            @PathVariable("order-id") Integer orderId
    ) {
        return ResponseEntity.ok(orderLineService.findAllByOrderId(orderId));
    }
}
