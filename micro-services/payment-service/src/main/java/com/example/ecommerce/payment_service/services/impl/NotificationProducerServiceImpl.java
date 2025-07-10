package com.example.ecommerce.payment_service.services.impl;

import com.example.ecommerce.payment_service.models.PaymentNotificationRequestDTO;
import com.example.ecommerce.payment_service.services.NotificationProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * Implementation of the NotificationProducerService interface.
 * Responsible for sending payment notifications to a Kafka topic.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducerServiceImpl implements NotificationProducerService {

    /**
     * KafkaTemplate for sending messages to Kafka.
     * Configured to send messages with a payload of type PaymentNotificationRequestDTO.
     */
    private final KafkaTemplate<String, PaymentNotificationRequestDTO> paymentKafkaTemplate;

    /**
     * Sends a payment notification to the Kafka topic "payment-topic".
     * Constructs a message with the provided payload and sends it using KafkaTemplate.
     *
     * @param paymentNotificationRequestDTO the payload containing payment notification details
     */
    @Override
    public void sendNotification(PaymentNotificationRequestDTO paymentNotificationRequestDTO) {
        log.info("Sending notification with body <{}>", paymentNotificationRequestDTO);

        // Build a Kafka message with the payload and topic header
        Message<PaymentNotificationRequestDTO> message = MessageBuilder
                .withPayload(paymentNotificationRequestDTO)
                .setHeader(KafkaHeaders.TOPIC, "payment-topic")
                .build();

        // Send the message to the Kafka topic
        paymentKafkaTemplate.send(message);
    }
}
