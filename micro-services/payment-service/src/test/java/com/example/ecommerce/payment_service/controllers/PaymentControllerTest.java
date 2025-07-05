package com.example.ecommerce.payment_service.controllers;

import com.example.ecommerce.payment_service.models.CustomerDTO;
import com.example.ecommerce.payment_service.models.PaymentMethod;
import com.example.ecommerce.payment_service.models.PaymentRequestDTO;
import com.example.ecommerce.payment_service.services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPayment_success() {
        var request = new PaymentRequestDTO(
                123,
                BigDecimal.TEN,
                1,
                PaymentMethod.CREDIT_CARD,
                "orderRef123",
                new CustomerDTO(null, "John", "Doe", "sdlkdjfl@sdfjl.sdf")
        );

        when(paymentService.createPayment(any())).thenReturn(1);

        ResponseEntity<Integer> response = paymentController.createPayment(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody());

        verify(paymentService).createPayment(request);
    }

    @Test
    void createPayment_serviceThrowsException() {
        var request = new PaymentRequestDTO(
                123,
                BigDecimal.TEN,
                1,
                PaymentMethod.CREDIT_CARD,
                "orderRef123",
                new CustomerDTO(null, "John", "Doe", "sdlkdjfl@sdfjl.sdf")
        );

        when(paymentService.createPayment(any())).thenThrow(new RuntimeException("Service error"));

        assertThrows(
                RuntimeException.class,
                () -> paymentController.createPayment(request)
        );
    }

    @Test
    void createPayment_nullRequest_throwsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> paymentController.createPayment(null)
        );
    }

    @Test
    void createPayment_nullCustomerDTO() {
        PaymentRequestDTO request = new PaymentRequestDTO(
                123,
                BigDecimal.TEN,
                1,
                PaymentMethod.CREDIT_CARD,
                "orderRef123",
                null
        );

        when(paymentService.createPayment(any())).thenThrow(new NullPointerException("CustomerDTO is null"));

        assertThrows(
                NullPointerException.class,
                () -> paymentController.createPayment(request)
        );
    }

    @Test
    void createPayment_zeroAmount() {
        PaymentRequestDTO request = new PaymentRequestDTO(
                123,
                BigDecimal.ZERO,
                1,
                PaymentMethod.CREDIT_CARD,
                "orderRef123",
                new CustomerDTO(null, "John", "Doe", "sdlkdjfl@sdfjl.sdf")
        );
        when(paymentService.createPayment(any())).thenReturn(2);

        ResponseEntity<Integer> response = paymentController.createPayment(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody());
    }

    @Test
    void createPayment_negativeAmount() {
        PaymentRequestDTO request = new PaymentRequestDTO(
                123,
                BigDecimal.valueOf(-10),
                1,
                PaymentMethod.CREDIT_CARD,
                "orderRef123",
                new CustomerDTO(null, "John", "Doe", "sdlkdjfl@sdfjl.sdf")
        );

        when(paymentService.createPayment(any())).thenReturn(3);

        ResponseEntity<Integer> response = paymentController.createPayment(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(3, response.getBody());
    }
}

