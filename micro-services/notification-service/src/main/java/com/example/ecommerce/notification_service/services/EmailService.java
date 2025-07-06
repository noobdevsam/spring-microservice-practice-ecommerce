package com.example.ecommerce.notification_service.services;

import com.example.ecommerce.notification_service.models.ProductDTO;

import java.math.BigDecimal;
import java.util.List;

public interface EmailService {

    void sendPaymentSuccessEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amount,
            String orderReference
    );

    void sendOrderConfirmationEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amount,
            String orderReference,
            List<ProductDTO> productDTOs
    );
}
