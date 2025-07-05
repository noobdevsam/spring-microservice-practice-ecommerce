package com.example.ecommerce.payment_service.services.impl;

import com.example.ecommerce.payment_service.mappers.PaymentMapper;
import com.example.ecommerce.payment_service.models.PaymentNotificationRequestDTO;
import com.example.ecommerce.payment_service.models.PaymentRequestDTO;
import com.example.ecommerce.payment_service.repositories.PaymentRepository;
import com.example.ecommerce.payment_service.services.NotificationProducerService;
import com.example.ecommerce.payment_service.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final NotificationProducerService notificationProducerService;

    @Override
    public Integer createPayment(PaymentRequestDTO paymentRequestDTO) {

        var payment = paymentRepository.save(
                paymentMapper.paymentRequestDTOToPayment(paymentRequestDTO)
        );

        notificationProducerService.sendNotification(
                new PaymentNotificationRequestDTO(
                        paymentRequestDTO.orderReference(),
                        paymentRequestDTO.amount(),
                        paymentRequestDTO.paymentMethod(),
                        paymentRequestDTO.customerDTO().firstName(),
                        paymentRequestDTO.customerDTO().lastName(),
                        paymentRequestDTO.customerDTO().email()
                )
        );

        return payment.getId();
    }
}
