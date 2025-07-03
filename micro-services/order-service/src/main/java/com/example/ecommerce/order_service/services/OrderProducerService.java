package com.example.ecommerce.order_service.services;

import com.example.ecommerce.order_service.models.OrderConfirmationDTO;

public interface OrderProducerService {
    void sendOrderConfirmation(OrderConfirmationDTO orderConfirmationDTO);
}
