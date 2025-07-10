package com.example.ecommerce.payment_service.services.impl;

import com.example.ecommerce.payment_service.mappers.PaymentMapper;
import com.example.ecommerce.payment_service.models.PaymentNotificationRequestDTO;
import com.example.ecommerce.payment_service.models.PaymentRequestDTO;
import com.example.ecommerce.payment_service.repositories.PaymentRepository;
import com.example.ecommerce.payment_service.services.NotificationProducerService;
import com.example.ecommerce.payment_service.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of the PaymentService interface.
 * Handles payment creation and notification sending.
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    /**
     * Repository for managing payment entities.
     */
    private final PaymentRepository paymentRepository;

    /**
     * Mapper for converting between PaymentRequestDTO and Payment entities.
     */
    private final PaymentMapper paymentMapper;

    /**
     * Service for sending payment notifications to Kafka.
     */
    private final NotificationProducerService notificationProducerService;

    /**
     * Creates a payment record and sends a notification.
     * Saves the payment details to the database and sends a notification to the Kafka topic.
     *
     * @param paymentRequestDTO the payment request containing details such as order reference, amount, payment method, and customer information
     * @return the ID of the created payment record
     */
    @Override
    public Integer createPayment(PaymentRequestDTO paymentRequestDTO) {

        // Save the payment entity to the database
        var payment = paymentRepository.save(
                paymentMapper.paymentRequestDTOToPayment(paymentRequestDTO)
        );

        // Send a payment notification to Kafka
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

        // Return the ID of the saved payment entity
        return payment.getId();
    }
}
