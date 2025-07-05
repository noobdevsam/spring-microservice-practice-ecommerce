package com.example.ecommerce.payment_service.services;

import com.example.ecommerce.payment_service.models.PaymentRequestDTO;

public interface PaymentService {

    Integer createPayment(PaymentRequestDTO paymentRequestDTO);
}
