package com.example.ecommerce.notification_service.services.impl;

import com.example.ecommerce.notification_service.entities.Notification;
import com.example.ecommerce.notification_service.models.*;
import com.example.ecommerce.notification_service.repositories.NotificationRepository;
import com.example.ecommerce.notification_service.services.EmailService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the NotificationsConsumerServiceImpl class.
 * These tests verify the behavior of notification consumption methods using mocked dependencies.
 */
class NotificationsConsumerServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationsConsumerServiceImpl notificationsConsumerService;

    /**
     * Initializes mocks before each test.
     * Ensures dependencies are properly injected into the service.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the consumption of payment success notifications with valid input.
     * Verifies that the notification is saved and an email is sent.
     *
     * @throws MessagingException if an error occurs during email sending
     */
    @Test
    void consumePaymentSuccessNotifications_validInput_savesNotificationAndSendsEmail() throws MessagingException {
        var dto = new PaymentConfirmationDTO(
                "ORDER123",
                BigDecimal.TEN,
                PaymentMethod.VISA,
                "John",
                "Doe",
                "test@example.com"
        );

        notificationsConsumerService.consumePaymentSuccessNotifications(dto);

        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(emailService, times(1)).sendPaymentSuccessEmail(
                eq("test@example.com"), eq("John Doe"), eq(BigDecimal.TEN), eq("ORDER123")
        );
    }

    /**
     * Tests the consumption of payment success notifications with a null DTO.
     * Verifies that a NullPointerException is thrown.
     */
    @Test
    void consumePaymentSuccessNotifications_nullDTO_throwsException() {
        assertThrows(
                NullPointerException.class,
                () -> notificationsConsumerService.consumePaymentSuccessNotifications(null)
        );
    }

    /**
     * Tests the consumption of payment success notifications when the email service throws an exception.
     * Verifies that the exception propagates.
     *
     * @throws MessagingException if an error occurs during email sending
     */
    @Test
    void consumePaymentSuccessNotifications_emailServiceThrows_exceptionPropagates() throws MessagingException {
        var dto = new PaymentConfirmationDTO(
                "45435sdf",
                BigDecimal.TEN,
                PaymentMethod.VISA,
                "SO34sf",
                "ORDER123",
                "test@testd.ds"
        );

        doThrow(new MessagingException("fail")).when(emailService).sendPaymentSuccessEmail(
                any(), any(), any(), any()
        );

        assertThrows(
                MessagingException.class,
                () -> notificationsConsumerService.consumePaymentSuccessNotifications(dto)
        );

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    /**
     * Tests the consumption of order confirmation notifications with valid input.
     * Verifies that the notification is saved and an email is sent.
     *
     * @throws MessagingException if an error occurs during email sending
     */
    @Test
    void consumeOrderConfirmationNotifications_validInput_savesNotificationAndSendsEmail() throws MessagingException {
        var customer = new CustomerDTO("54", "John", "Doe", "test@example.com");
        List<ProductDTO> products = Collections.emptyList();
        var dto = new OrderConfirmationDTO(
                "ORDER123",
                BigDecimal.TEN,
                PaymentMethod.VISA,
                customer,
                products
        );

        notificationsConsumerService.consumeOrderConfirmationNotifications(dto);

        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(emailService, times(1)).sendOrderConfirmationEmail(
                eq("test@example.com"), eq("John Doe"), eq(BigDecimal.TEN), eq("ORDER123"), eq(products)
        );
    }

    /**
     * Tests the consumption of order confirmation notifications with a null DTO.
     * Verifies that a NullPointerException is thrown.
     */
    @Test
    void consumeOrderConfirmationNotifications_nullDTO_throwsException() {
        assertThrows(
                NullPointerException.class,
                () -> notificationsConsumerService.consumeOrderConfirmationNotifications(null)
        );
    }

    /**
     * Tests the consumption of order confirmation notifications when the email service throws an exception.
     * Verifies that the exception propagates.
     *
     * @throws MessagingException if an error occurs during email sending
     */
    @Test
    void consumeOrderConfirmationNotifications_emailServiceThrows_exceptionPropagates() throws MessagingException {
        var customer = new CustomerDTO("98", "John", "Doe", "test@example.com");
        List<ProductDTO> products = Collections.emptyList();
        var dto = new OrderConfirmationDTO(
                "45435sdf",
                BigDecimal.TEN,
                PaymentMethod.VISA,
                customer,
                products
        );

        doThrow(new MessagingException("fail")).when(emailService).sendOrderConfirmationEmail(
                any(), any(), any(), any(), any()
        );

        assertThrows(
                MessagingException.class,
                () -> notificationsConsumerService.consumeOrderConfirmationNotifications(dto)
        );

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    /**
     * Tests the consumption of order confirmation notifications with a missing customer.
     * Verifies that a NullPointerException is thrown.
     */
    @Test
    void consumeOrderConfirmationNotifications_missingCustomer_throwsException() {
        var dto = new OrderConfirmationDTO(
                "654", BigDecimal.TEN, PaymentMethod.VISA, null, Collections.emptyList()
        );

        assertThrows(
                NullPointerException.class,
                () -> notificationsConsumerService.consumeOrderConfirmationNotifications(dto)
        );
    }

}