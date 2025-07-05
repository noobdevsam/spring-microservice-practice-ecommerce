package com.example.ecommerce.payment_service.mappers;

import com.example.ecommerce.payment_service.entities.Payment;
import com.example.ecommerce.payment_service.models.PaymentRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public Payment paymentRequestDTOToPayment(PaymentRequestDTO paymentRequestDTO) {
        if (paymentRequestDTO == null) {
            return null;
        }

        return Payment.builder()
                .paymentMethod(paymentRequestDTO.paymentMethod())
                .amount(paymentRequestDTO.amount())
                .orderId(paymentRequestDTO.orderId())
                .build();
    }
}
