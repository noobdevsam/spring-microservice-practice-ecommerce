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

/**
 * Unit tests for the PaymentController class.
 * Verifies the behavior of the createPayment method under various conditions.
 */
class PaymentControllerTest {

    /**
     * Mocked instance of PaymentService used for testing.
     */
    @Mock
    private PaymentService paymentService;

    /**
     * Injected instance of PaymentController being tested.
     */
    @InjectMocks
    private PaymentController paymentController;

    /**
     * Initializes mocks before each test execution.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the successful creation of a payment.
     * Verifies that the response contains the correct status code and payment ID.
     */
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

    /**
     * Tests the behavior when the PaymentService throws an exception.
     * Verifies that the exception is propagated correctly.
     */
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

    /**
     * Tests the behavior when a null request is passed to createPayment.
     * Verifies that an IllegalArgumentException is thrown.
     */
    @Test
    void createPayment_nullRequest_throwsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> paymentController.createPayment(null)
        );
    }

    /**
     * Tests the behavior when the CustomerDTO in the request is null.
     * Verifies that a NullPointerException is thrown.
     */
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

    /**
     * Tests the behavior when the payment amount is zero.
     * Verifies that the response contains the correct status code and payment ID.
     */
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

    /**
     * Tests the behavior when the payment amount is negative.
     * Verifies that the response contains the correct status code and payment ID.
     */
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

