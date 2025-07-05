package com.example.ecommerce.payment_service.services;

import com.example.ecommerce.payment_service.models.PaymentNotificationRequestDTO;

public interface NotificationProducerService {
    void sendNotification(PaymentNotificationRequestDTO paymentNotificationRequestDTO);
}
