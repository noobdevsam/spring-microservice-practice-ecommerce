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

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final CustomerFeignClient customerFeignClient;
    private final PaymentFeignClient paymentFeignClient;
    private final ProductService productService;
    private final OrderLineService orderLineService;
    private final OrderProducerService orderProducerService;

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

        var paymentRequest = new PaymentRequestDTO(
                orderRequestDTO.amount(),
                orderRequestDTO.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );

        paymentFeignClient.requestOrderPayment(paymentRequest);

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

    @Override
    public List<OrderResponseDTO> findAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::orderToOrderResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO findOrderById(Integer id) {
        return orderRepository.findById(id)
                .map(orderMapper::orderToOrderResponseDTO)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("Order not found with id: " + id))
                );
    }
}
