package com.example.ecommerce.notification_service.services;

import com.example.ecommerce.notification_service.models.OrderConfirmationDTO;
import com.example.ecommerce.notification_service.models.PaymentConfirmationDTO;
import jakarta.mail.MessagingException;

public interface NotificationsConsumerService {

    void consumePaymentSuccessNotifications(
            PaymentConfirmationDTO paymentConfirmationDTO
    ) throws MessagingException;

    void consumeOrderConfirmationNotifications(
            OrderConfirmationDTO orderConfirmationDTO
    ) throws MessagingException;

}
