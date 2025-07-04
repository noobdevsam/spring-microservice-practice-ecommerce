package com.example.ecommerce.order_service.services;

import com.example.ecommerce.order_service.entities.CustomerOrder;
import com.example.ecommerce.order_service.models.OrderLineRequestDTO;
import com.example.ecommerce.order_service.models.OrderLineResponseDTO;
import com.example.ecommerce.order_service.models.PaymentMethod;
import com.example.ecommerce.order_service.repositories.OrderLineRepository;
import com.example.ecommerce.order_service.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderLineServiceIntegrationTest {

    @Autowired
    private OrderLineService orderLineService;

    @Autowired
    private OrderLineRepository orderLineRepository;

    @Autowired
    private OrderRepository orderRepository;

    private OrderLineRequestDTO orderLineRequestDTO;

    private Integer customerOrderId;

    @BeforeEach
    void setUp() {
        orderLineRepository.deleteAll();
        orderRepository.deleteAll();

        // Create and save a CustomerOrder (Order)
        var order = CustomerOrder.builder()
                .reference("test-ref-" + System.nanoTime())
                .totalAmount(java.math.BigDecimal.valueOf(100.0))
                .paymentMethod(PaymentMethod.VISA)
                .customerId("test-customer")
                .build();

        order = orderRepository.save(order);
        customerOrderId = order.getId();

        orderLineRequestDTO = new OrderLineRequestDTO(null, customerOrderId, 1, 2.0);
    }

    @Test
    void testSaveOrderLineAndFindAllByOrderId_Success() {
        var id = orderLineService.saveOrderLine(orderLineRequestDTO);

        assertNotNull(id);

        List<OrderLineResponseDTO> result = orderLineService.findAllByOrderId(customerOrderId);

        assertEquals(1, result.size());
        assertEquals(2.0, result.getFirst().quantity());
    }

    @Test
    void testFindAllByOrderId_EmptyList() {
        List<OrderLineResponseDTO> result = orderLineService.findAllByOrderId(999);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllByOrderId_NullId() {
        List<OrderLineResponseDTO> result = orderLineService.findAllByOrderId(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSaveOrderLine_NullRequest() {
        assertThrows(
                NullPointerException.class,
                () -> orderLineService.saveOrderLine(null)
        );
    }

    @Test
    @Rollback
    void testMultipleOrderLinesForSameOrderId() {
        var dto1 = new OrderLineRequestDTO(null, customerOrderId, 1, 2.0);
        var dto2 = new OrderLineRequestDTO(null, customerOrderId, 2, 3.0);

        orderLineService.saveOrderLine(dto1);
        orderLineService.saveOrderLine(dto2);

        List<OrderLineResponseDTO> result = orderLineService.findAllByOrderId(customerOrderId);

        assertEquals(2, result.size());
    }

    @Test
    void testSaveOrderLine_InvalidData() {
        var invalidDto = new OrderLineRequestDTO(null, null, null, null);

        assertThrows(
                Exception.class,
                () -> orderLineService.saveOrderLine(invalidDto)
        );
    }
}
