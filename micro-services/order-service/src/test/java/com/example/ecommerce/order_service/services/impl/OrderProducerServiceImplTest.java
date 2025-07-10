package com.example.ecommerce.order_service.services.impl;

import com.example.ecommerce.order_service.models.CustomerResponseDTO;
import com.example.ecommerce.order_service.models.OrderConfirmationDTO;
import com.example.ecommerce.order_service.models.PaymentMethod;
import com.example.ecommerce.order_service.models.ProductPurchaseResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the OrderProducerServiceImpl class.
 * This class tests the functionality of sending order confirmation messages via Kafka.
 */
class OrderProducerServiceImplTest {

    @Mock
    private KafkaTemplate<String, OrderConfirmationDTO> kafkaTemplate;

    @InjectMocks
    private OrderProducerServiceImpl orderProducerService;

    private OrderConfirmationDTO orderConfirmationDTO;

    /**
     * Sets up the test environment before each test.
     * Initializes mocks and test data.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderConfirmationDTO = new OrderConfirmationDTO(
                "order123",
                BigDecimal.valueOf(100.00),
                PaymentMethod.CREDIT_CARD,
                new CustomerResponseDTO("customer123", "John Doe", "sdjsldj", "sdksdklf@djsldkf.com"),
                List.of(
                        new ProductPurchaseResponseDTO(1, "Product 1", "Description 1", BigDecimal.valueOf(50.00), 2.0),
                        new ProductPurchaseResponseDTO(2, "Product 2", "Description 2", BigDecimal.valueOf(25.00), 1.0)
                )
        );
    }

    /**
     * Tests sending an order confirmation message successfully.
     * Verifies that the message is sent and the payload matches the expected data.
     */
    @Test
    void sendOrderConfirmation_sendsMessageSuccessfully() {
        orderProducerService.sendOrderConfirmation(orderConfirmationDTO);
        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(kafkaTemplate, times(1)).send(captor.capture());
        Message sentMessage = captor.getValue();
        assertNotNull(sentMessage);
        assertEquals(orderConfirmationDTO, sentMessage.getPayload());
        assertEquals("order-topic", sentMessage.getHeaders().get("kafka_topic"));
    }

    /**
     * Tests sending an order confirmation message with a null DTO.
     * Verifies that an IllegalArgumentException is thrown and no message is sent.
     */
    @Test
    void sendOrderConfirmation_withNullDTO() {
        assertThrows(
                IllegalArgumentException.class,
                () -> orderProducerService.sendOrderConfirmation(null)
        );

        verify(kafkaTemplate, never()).send(any(Message.class));
    }

    /**
     * Tests handling exceptions thrown by the KafkaTemplate during message sending.
     * Verifies that the exception is propagated correctly.
     */
    @Test
    void sendOrderConfirmation_kafkaTemplateThrowsException() {
        doThrow(new RuntimeException("Kafka error")).when(kafkaTemplate).send(any(Message.class));
        assertThrows(RuntimeException.class, () -> orderProducerService.sendOrderConfirmation(orderConfirmationDTO));
    }
}
