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

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducerServiceImpl implements NotificationProducerService {

    private final KafkaTemplate<String, PaymentNotificationRequestDTO> paymentKafkaTemplate;

    @Override
    public void sendNotification(PaymentNotificationRequestDTO paymentNotificationRequestDTO) {
        log.info("Sending notification with body <{}>", paymentNotificationRequestDTO);

        Message<PaymentNotificationRequestDTO> message = MessageBuilder
                .withPayload(paymentNotificationRequestDTO)
                .setHeader(KafkaHeaders.TOPIC, "payment-topic")
                .build();

        paymentKafkaTemplate.send(message);
    }
}
