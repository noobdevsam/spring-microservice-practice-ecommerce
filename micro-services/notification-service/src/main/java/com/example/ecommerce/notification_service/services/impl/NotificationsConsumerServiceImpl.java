package com.example.ecommerce.notification_service.services.impl;

import com.example.ecommerce.notification_service.models.OrderConfirmationDTO;
import com.example.ecommerce.notification_service.models.PaymentConfirmationDTO;
import com.example.ecommerce.notification_service.repositories.NotificationRepository;
import com.example.ecommerce.notification_service.services.EmailService;
import com.example.ecommerce.notification_service.services.NotificationsConsumerService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationsConsumerServiceImpl implements NotificationsConsumerService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @KafkaListener(topics = "payment-topic")
    @Override
    public void consumePaymentSuccessNotifications(PaymentConfirmationDTO paymentConfirmationDTO) throws MessagingException {

    }

    @KafkaListener(topics = "order-topic")
    @Override
    public void consumeOrderConfirmationNotifications(OrderConfirmationDTO orderConfirmationDTO) throws MessagingException {

    }
}
