package com.example.ecommerce.notification_service.entities;

import com.example.ecommerce.notification_service.models.NotificationType;
import com.example.ecommerce.notification_service.models.OrderConfirmationDTO;
import com.example.ecommerce.notification_service.models.PaymentConfirmationDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Notification {

    @Id
    private String id;

    private NotificationType notificationType;
    private LocalDateTime notificationDate;

    private OrderConfirmationDTO orderConfirmationDTO;
    private PaymentConfirmationDTO paymentConfirmationDTO;

}
