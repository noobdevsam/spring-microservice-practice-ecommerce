package com.example.ecommerce.notification_service.services.impl;

import com.example.ecommerce.notification_service.models.ProductDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the EmailServiceImpl class.
 * These tests verify the behavior of email sending methods using mocked dependencies.
 */
class EmailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    @InjectMocks
    private EmailServiceImpl emailService;

    /**
     * Sets up the test environment by initializing mocks and configuring behavior for the JavaMailSender.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Provide a real MimeMessage instance for mocking
        MimeMessage mimeMessage = new JavaMailSenderImpl().createMimeMessage();
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    /**
     * Tests the sendPaymentSuccessEmail method with valid input.
     * Verifies that the email is sent successfully without throwing exceptions.
     *
     * @throws MessagingException if an error occurs during email sending
     */
    @Test
    void sendPaymentSuccessEmail_validInput_sendsEmail() throws MessagingException {
        when(
                templateEngine.process(anyString(), any(Context.class))
        ).thenReturn("Email Body");

        doNothing().when(javaMailSender).send((MimeMessage) any());

        assertDoesNotThrow(
                () -> emailService.sendPaymentSuccessEmail(
                        "test@example.com", "John Doe", BigDecimal.TEN, "ORDER123"
                )
        );

        verify(
                javaMailSender,
                times(1)
        ).send(
                (MimeMessage) any()
        );
    }

    /**
     * Tests the sendPaymentSuccessEmail method with a null email.
     * Verifies that an IllegalArgumentException is thrown.
     */
    @Test
    void sendPaymentSuccessEmail_nullEmail_throwsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> emailService.sendPaymentSuccessEmail(null, "John Doe", BigDecimal.TEN, "ORDER123")
        );
    }

    /**
     * Tests the sendOrderConfirmationEmail method with valid input.
     * Verifies that the email is sent successfully without throwing exceptions.
     *
     * @throws MessagingException if an error occurs during email sending
     */
    @Test
    void sendOrderConfirmationEmail_validInput_sendsEmail() throws MessagingException {
        when(
                templateEngine.process(anyString(), any(Context.class))
        ).thenReturn("Order Confirmation Body");

        doNothing().when(javaMailSender).send((MimeMessage) any());

        List<ProductDTO> products = Collections.emptyList();

        assertDoesNotThrow(
                () -> emailService.sendOrderConfirmationEmail("test@example.com", "John Doe", BigDecimal.TEN, "ORDER123", products)
        );

        verify(
                javaMailSender,
                times(1)
        ).send(
                (MimeMessage) any()
        );
    }

    /**
     * Tests the sendOrderConfirmationEmail method with an empty product list.
     * Verifies that the email is sent successfully without throwing exceptions.
     *
     * @throws MessagingException if an error occurs during email sending
     */
    @Test
    void sendOrderConfirmationEmail_emptyProductList_sendsEmail() throws MessagingException {
        when(
                templateEngine.process(anyString(), any(Context.class))
        ).thenReturn("Order Confirmation Body");
        doNothing().when(javaMailSender).send(
                (MimeMessage) any()
        );

        List<ProductDTO> products = Collections.emptyList();

        assertDoesNotThrow(
                () -> emailService.sendOrderConfirmationEmail("test@example.com", "John Doe", BigDecimal.TEN, "ORDER123", products)
        );
        verify(javaMailSender, times(1)).send((MimeMessage) any());
    }

    /**
     * Tests the sendOrderConfirmationEmail method with a null product list.
     * Verifies that an IllegalArgumentException is thrown.
     */
    @Test
    void sendOrderConfirmationEmail_nullProductList_throwsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> emailService.sendOrderConfirmationEmail("test@example.com", "John Doe", BigDecimal.TEN, "ORDER123", null)
        );
    }

    /**
     * Tests the sendOrderConfirmationEmail method with a null email.
     * Verifies that an IllegalArgumentException is thrown.
     */
    @Test
    void sendOrderConfirmationEmail_nullEmail_throwsException() {
        List<ProductDTO> products = Collections.emptyList();

        assertThrows(
                IllegalArgumentException.class,
                () -> emailService.sendOrderConfirmationEmail(null, "John Doe", BigDecimal.TEN, "ORDER123", products)
        );
    }

}