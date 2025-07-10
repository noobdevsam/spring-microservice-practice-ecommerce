package com.example.ecommerce.order_service.services.impl;

import com.example.ecommerce.order_service.exceptions.BusinessException;
import com.example.ecommerce.order_service.feign_client.CustomerFeignClient;
import com.example.ecommerce.order_service.feign_client.PaymentFeignClient;
import com.example.ecommerce.order_service.mappers.OrderMapper;
import com.example.ecommerce.order_service.models.*;
import com.example.ecommerce.order_service.repositories.OrderRepository;
import com.example.ecommerce.order_service.services.OrderLineService;
import com.example.ecommerce.order_service.services.OrderProducerService;
import com.example.ecommerce.order_service.services.OrderService;
import com.example.ecommerce.order_service.services.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the OrderService interface.
 * Provides methods for creating and retrieving orders.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    // Mapper for converting between Order DTOs and entity objects.
    private final OrderMapper orderMapper;

    // Repository for accessing order data from the database.
    private final OrderRepository orderRepository;

    // Feign client for interacting with the Customer microservice.
    private final CustomerFeignClient customerFeignClient;

    // Feign client for interacting with the Payment microservice.
    private final PaymentFeignClient paymentFeignClient;

    // Service for handling product-related operations.
    private final ProductService productService;

    // Service for handling order line-related operations.
    private final OrderLineService orderLineService;

    // Service for sending order confirmation messages.
    private final OrderProducerService orderProducerService;

    /**
     * Creates a new order.
     *
     * @param orderRequestDTO The DTO containing order details.
     * @return The ID of the created order.
     * @throws BusinessException if the customer is not found.
     */
    @Transactional
    @Override
    public Integer createOrder(OrderRequestDTO orderRequestDTO) {
        var customer = customerFeignClient.findCustomerById(orderRequestDTO.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order :: Customer not found with id: " + orderRequestDTO.customerId()));
        var purchasedProducts = productService.executePurchaseProducts(
                orderRequestDTO.purchases()
        );
        var order = orderRepository.save(
                orderMapper.orderRequestDTOToOrder(orderRequestDTO)
        );

        for (var purchaseRequest : orderRequestDTO.purchases()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequestDTO(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        // Create a payment request for the order.
        // This will trigger the payment process in the Payment microservice.
        var paymentRequest = new PaymentRequestDTO(
                orderRequestDTO.amount(),
                orderRequestDTO.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );

        paymentFeignClient.requestOrderPayment(paymentRequest);

        // Send an order confirmation message to the Order Producer Service.
        // This will typically be used to notify other services or systems about the order creation.
        orderProducerService.sendOrderConfirmation(
                new OrderConfirmationDTO(
                        orderRequestDTO.reference(),
                        orderRequestDTO.amount(),
                        orderRequestDTO.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );

        log.info("Order created successfully with id: {}", order.getId());
        return order.getId();
    }

    /**
     * Retrieves all orders.
     *
     * @return A list of OrderResponseDTOs representing all orders.
     */
    @Override
    public List<OrderResponseDTO> findAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::orderToOrderResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id The ID of the order to retrieve.
     * @return The OrderResponseDTO representing the order.
     * @throws EntityNotFoundException if the order is not found.
     */
    @Override
    public OrderResponseDTO findOrderById(Integer id) {
        return orderRepository.findById(id)
                .map(orderMapper::orderToOrderResponseDTO)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("Order not found with id: " + id))
                );
    }
}