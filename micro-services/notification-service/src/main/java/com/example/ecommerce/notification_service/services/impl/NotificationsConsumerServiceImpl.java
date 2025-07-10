package com.example.ecommerce.notification_service.services.impl;

import com.example.ecommerce.notification_service.entities.Notification;
import com.example.ecommerce.notification_service.models.NotificationType;
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

import java.time.LocalDateTime;

/**
 * Implementation of the NotificationsConsumerService interface for consuming notifications from Kafka topics.
 * This service listens to Kafka topics and processes payment and order confirmation notifications.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationsConsumerServiceImpl implements NotificationsConsumerService {

    private final NotificationRepository notificationRepository; // Repository for persisting notifications
    private final EmailService emailService; // Service for sending email notifications

    /**
     * Consumes payment success notifications from the "payment-topic" Kafka topic.
     * Saves the notification to the database and sends a payment success email.
     *
     * @param paymentConfirmationDTO The DTO containing payment confirmation details.
     * @throws MessagingException If an error occurs while sending the email.
     */
    @KafkaListener(topics = "payment-topic")
    @Override
    public void consumePaymentSuccessNotifications(
            PaymentConfirmationDTO paymentConfirmationDTO
    ) throws MessagingException {

        log.info("Consuming the message from payment-topic Topic :: {}", paymentConfirmationDTO);

        // Save the notification to the database
        notificationRepository.save(
                Notification.builder()
                        .notificationType(NotificationType.PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentConfirmationDTO(paymentConfirmationDTO)
                        .build()
        );

        // Extract customer name from the DTO
        var customerName = paymentConfirmationDTO.customerFirstName() + " " + paymentConfirmationDTO.customerLastName();

        // Send payment success email
        emailService.sendPaymentSuccessEmail(
                paymentConfirmationDTO.customerEmail(),
                customerName,
                paymentConfirmationDTO.amount(),
                paymentConfirmationDTO.orderReference()
        );

    }

    /**
     * Consumes order confirmation notifications from the "order-topic" Kafka topic.
     * Saves the notification to the database and sends an order confirmation email.
     *
     * @param orderConfirmationDTO The DTO containing order confirmation details.
     * @throws MessagingException If an error occurs while sending the email.
     */
    @KafkaListener(topics = "order-topic")
    @Override
    public void consumeOrderConfirmationNotifications(
            OrderConfirmationDTO orderConfirmationDTO
    ) throws MessagingException {

        log.info("Consuming the message from order-topic Topic :: {}", orderConfirmationDTO);

        // Save the notification to the database
        notificationRepository.save(
                Notification.builder()
                        .notificationType(NotificationType.ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .orderConfirmationDTO(orderConfirmationDTO)
                        .build()
        );

        // Extract customer name from the DTO
        var customerName = orderConfirmationDTO.customerDTO().firstName() + " " + orderConfirmationDTO.customerDTO().lastName();

        // Send order confirmation email
        emailService.sendOrderConfirmationEmail(
                orderConfirmationDTO.customerDTO().email(),
                customerName,
                orderConfirmationDTO.totalAmount(),
                orderConfirmationDTO.orderReference(),
                orderConfirmationDTO.productDTOs()
        );

    }

}
