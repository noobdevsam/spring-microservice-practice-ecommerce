package com.example.ecommerce.payment_service.services;

import com.example.ecommerce.payment_service.entities.Payment;
import com.example.ecommerce.payment_service.mappers.PaymentMapper;
import com.example.ecommerce.payment_service.models.CustomerDTO;
import com.example.ecommerce.payment_service.models.PaymentMethod;
import com.example.ecommerce.payment_service.models.PaymentNotificationRequestDTO;
import com.example.ecommerce.payment_service.models.PaymentRequestDTO;
import com.example.ecommerce.payment_service.repositories.PaymentRepository;
import com.example.ecommerce.payment_service.services.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the PaymentService class.
 * Verifies the behavior of the createPayment method under various conditions.
 */
class PaymentServiceTest {

    /**
     * Mocked instance of PaymentRepository used for testing.
     */
    @Mock
    private PaymentRepository paymentRepository;

    /**
     * Mocked instance of PaymentMapper used for testing.
     */
    @Mock
    private PaymentMapper paymentMapper;

    /**
     * Mocked instance of NotificationProducerService used for testing.
     */
    @Mock
    private NotificationProducerService notificationProducerService;

    /**
     * Injected instance of PaymentServiceImpl being tested.
     */
    @InjectMocks
    private PaymentServiceImpl paymentService;

    /**
     * Initializes mocks before each test execution.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the successful creation of a payment.
     * Verifies that the payment is saved and a notification is sent.
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
        var paymentEntity = mock(Payment.class);

        when(paymentMapper.paymentRequestDTOToPayment(any())).thenReturn(paymentEntity);
        when(paymentRepository.save(any())).thenReturn(paymentEntity);
        when(paymentEntity.getId()).thenReturn(1);

        Integer result = paymentService.createPayment(request);

        assertEquals(1, result);

        verify(paymentRepository).save(paymentEntity);
        verify(notificationProducerService).sendNotification(any(PaymentNotificationRequestDTO.class));
    }

    /**
     * Tests the behavior when the CustomerDTO in the request is null.
     * Verifies that a NullPointerException is thrown.
     */
    @Test
    void createPayment_nullCustomer_throwsException() {
        var request = new PaymentRequestDTO(
                23,
                BigDecimal.TEN,
                5,
                PaymentMethod.CREDIT_CARD,
                "f",
                null
        );

        when(paymentMapper.paymentRequestDTOToPayment(any())).thenThrow(new NullPointerException("CustomerDTO is null"));

        assertThrows(
                NullPointerException.class,
                () -> paymentService.createPayment(request)
        );
    }

    /**
     * Tests the behavior when the PaymentRepository throws an exception.
     * Verifies that the exception is propagated correctly.
     */
    @Test
    void createPayment_repositoryThrowsException() {
        var request = new PaymentRequestDTO(
                123,
                BigDecimal.TEN,
                1,
                PaymentMethod.CREDIT_CARD,
                "orderRef123",
                new CustomerDTO(null, "John", "Doe", "sdlkdjfl@sdfjl.sdf")
        );

        when(paymentMapper.paymentRequestDTOToPayment(any())).thenReturn(mock(Payment.class));
        when(paymentRepository.save(any())).thenThrow(new RuntimeException("DB error"));

        assertThrows(
                RuntimeException.class,
                () -> paymentService.createPayment(request)
        );
    }

    /**
     * Tests the behavior when the NotificationProducerService throws an exception.
     * Verifies that the exception is propagated correctly.
     */
    @Test
    void createPayment_notificationThrowsException() {
        var request = new PaymentRequestDTO(
                123,
                BigDecimal.TEN,
                1,
                PaymentMethod.CREDIT_CARD,
                "orderRef123",
                new CustomerDTO(null, "John", "Doe", "sdlkdjfl@sdfjl.sdf")
        );
        var paymentEntity = mock(Payment.class);

        when(paymentMapper.paymentRequestDTOToPayment(any())).thenReturn(paymentEntity);
        when(paymentRepository.save(any())).thenReturn(paymentEntity);
        when(paymentEntity.getId()).thenReturn(2);

        doThrow(new RuntimeException("Notification error")).when(notificationProducerService).sendNotification(any());
        assertThrows(RuntimeException.class, () -> paymentService.createPayment(request));
    }

    /**
     * Tests the content of the notification sent by the NotificationProducerService.
     * Verifies that the notification contains the correct details.
     */
    @Test
    void createPayment_checkNotificationContent() {
        var request = new PaymentRequestDTO(
                123,
                BigDecimal.TEN,
                1,
                PaymentMethod.CREDIT_CARD,
                "orderRef123",
                new CustomerDTO(null, "John", "Doe", "alice@example.com")
        );
        var paymentEntity = mock(com.example.ecommerce.payment_service.entities.Payment.class);

        when(paymentMapper.paymentRequestDTOToPayment(any())).thenReturn(paymentEntity);
        when(paymentRepository.save(any())).thenReturn(paymentEntity);
        when(paymentEntity.getId()).thenReturn(3);

        paymentService.createPayment(request);

        ArgumentCaptor<PaymentNotificationRequestDTO> captor = ArgumentCaptor.forClass(PaymentNotificationRequestDTO.class);

        verify(notificationProducerService).sendNotification(captor.capture());

        PaymentNotificationRequestDTO notification = captor.getValue();

        assertEquals("orderRef123", notification.orderReference());
        assertEquals(BigDecimal.TEN, notification.amount());
        assertEquals(PaymentMethod.CREDIT_CARD, notification.paymentMethod());
        assertEquals("John", notification.customerFirstName());
        assertEquals("Doe", notification.customerLastName());
        assertEquals("alice@example.com", notification.customerEmail());
    }

}