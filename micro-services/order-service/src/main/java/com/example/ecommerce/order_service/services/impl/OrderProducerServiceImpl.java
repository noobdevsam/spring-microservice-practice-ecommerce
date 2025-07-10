package com.example.ecommerce.order_service.services.impl;

import com.example.ecommerce.order_service.models.OrderConfirmationDTO;
import com.example.ecommerce.order_service.services.OrderProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * Implementation of the OrderProducerService interface.
 * Responsible for sending order confirmation messages to a Kafka topic.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderProducerServiceImpl implements OrderProducerService {

    // KafkaTemplate for sending messages to Kafka.
    private final KafkaTemplate<String, OrderConfirmationDTO> kafkaTemplate;

    /**
     * Sends an order confirmation message to the Kafka topic.
     *
     * @param orderConfirmationDTO The DTO containing order confirmation details.
     * @throws IllegalArgumentException if the provided payload is null.
     */
    @Override
    public void sendOrderConfirmation(OrderConfirmationDTO orderConfirmationDTO) {
        if (orderConfirmationDTO == null) {
            throw new IllegalArgumentException("Payload must not be null");
        }
        log.info("Sending order confirmation.....");

        // Build a Kafka message with the payload and topic header.
        Message<OrderConfirmationDTO> message = MessageBuilder
                .withPayload(orderConfirmationDTO)
                .setHeader(KafkaHeaders.TOPIC, "order-topic")
                .build();

        // Send the message to the Kafka topic.
        kafkaTemplate.send(message);
    }
}
