package com.example.ecommerce.order_service.services.impl;

import com.example.ecommerce.order_service.models.OrderConfirmationDTO;
import com.example.ecommerce.order_service.services.OrderProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderProducerServiceImpl implements OrderProducerService {

    @Override
    public void sendOrderConfirmation(OrderConfirmationDTO orderConfirmationDTO) {
        // Here you would implement the logic to send the order confirmation message
        // For example, you might use a message broker like Kafka or RabbitMQ
        // For now, we will just log the order confirmation
        log.info("Sending order confirmation: {}", orderConfirmationDTO);
    }
}
