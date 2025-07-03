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

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderProducerServiceImpl implements OrderProducerService {

    private final KafkaTemplate<String, OrderConfirmationDTO> kafkaTemplate;

    @Override
    public void sendOrderConfirmation(OrderConfirmationDTO orderConfirmationDTO) {
        log.info("Sending order confirmation.....");

        Message<OrderConfirmationDTO> message = MessageBuilder
                .withPayload(orderConfirmationDTO)
                .setHeader(KafkaHeaders.TOPIC, "order-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
