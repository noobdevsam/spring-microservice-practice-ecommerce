package com.example.ecommerce.order_service.services.impl;

import com.example.ecommerce.order_service.entities.OrderLine;
import com.example.ecommerce.order_service.mappers.OrderLineMapper;
import com.example.ecommerce.order_service.models.OrderLineRequestDTO;
import com.example.ecommerce.order_service.models.OrderLineResponseDTO;
import com.example.ecommerce.order_service.repositories.OrderLineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the OrderLineServiceImpl class.
 * This class tests the functionality of saving and retrieving order lines using mocked dependencies.
 */
class OrderLineServiceImplTest {

    @Mock
    private OrderLineRepository orderLineRepository;

    @Mock
    private OrderLineMapper orderLineMapper;

    @InjectMocks
    private OrderLineServiceImpl orderLineServiceImpl;

    private OrderLineRequestDTO orderLineRequestDTO;
    private OrderLine orderLine;
    private OrderLineResponseDTO orderLineResponseDTO;

    /**
     * Sets up the test environment before each test.
     * Initializes mocks and test data.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderLineRequestDTO = new OrderLineRequestDTO(
                1,
                1,
                1,
                2.0
        );
        orderLine = new OrderLine();
        orderLine.setId(1);
        orderLineResponseDTO = new OrderLineResponseDTO(
                1,
                2.0
        );
    }

    /**
     * Tests saving an order line successfully.
     * Verifies that the order line is saved and the correct ID is returned.
     */
    @Test
    void testSaveOrderLine_Success() {
        when(
                orderLineMapper.orderRequestDTOToOrderLine(orderLineRequestDTO)
        ).thenReturn(orderLine);

        when(
                orderLineRepository.save(orderLine)
        ).thenReturn(orderLine);

        var id = orderLineServiceImpl.saveOrderLine(orderLineRequestDTO);

        assertNotNull(id);
        assertEquals(1, id);

        verify(
                orderLineMapper, times(1)
        ).orderRequestDTOToOrderLine(orderLineRequestDTO);

        verify(
                orderLineRepository, times(1)
        ).save(orderLine);
    }

    /**
     * Tests saving an order line with a null request.
     * Verifies that a NullPointerException is thrown.
     */
    @Test
    void testSaveOrderLine_NullRequest() {
        assertThrows(
                NullPointerException.class,
                () -> orderLineServiceImpl.saveOrderLine(null)
        );
    }

    /**
     * Tests retrieving all order lines by a valid order ID.
     * Verifies that the correct list of order lines is returned.
     */
    @Test
    void testFindAllByOrderId_Success() {
        List<OrderLine> orderLines = Collections.singletonList(orderLine);

        when(orderLineRepository.findAllByOrderId(1)).thenReturn(orderLines);
        when(orderLineMapper.orderLineToOrderLineResponseDTO(orderLine)).thenReturn(orderLineResponseDTO);

        List<OrderLineResponseDTO> result = orderLineServiceImpl.findAllByOrderId(1);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(
                orderLineRepository, times(1)
        ).findAllByOrderId(1);
        verify(
                orderLineMapper, times(1)
        ).orderLineToOrderLineResponseDTO(orderLine);
    }

    /**
     * Tests retrieving order lines by an order ID that does not exist.
     * Verifies that an empty list is returned.
     */
    @Test
    void testFindAllByOrderId_EmptyList() {
        when(
                orderLineRepository.findAllByOrderId(999)
        ).thenReturn(Collections.emptyList());

        List<OrderLineResponseDTO> result = orderLineServiceImpl.findAllByOrderId(999);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(
                orderLineRepository, times(1)
        ).findAllByOrderId(999);
    }

    /**
     * Tests retrieving order lines by a null order ID.
     * Verifies that an empty list is returned.
     */
    @Test
    void testFindAllByOrderId_NullId() {
        when(
                orderLineRepository.findAllByOrderId(null)
        ).thenReturn(null);

        List<OrderLineResponseDTO> result = orderLineServiceImpl.findAllByOrderId(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(
                orderLineRepository, times(1)
        ).findAllByOrderId(null);
        verify(
                orderLineMapper, never()
        ).orderLineToOrderLineResponseDTO(any());
    }

    /**
     * Tests retrieving order lines when the repository returns null.
     * Verifies that an empty list is returned.
     */
    @Test
    void testFindAllByOrderId_RepositoryReturnsNull() {
        when(
                orderLineRepository.findAllByOrderId(123)
        ).thenReturn(null);

        List<OrderLineResponseDTO> result = orderLineServiceImpl.findAllByOrderId(123);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(
                orderLineRepository, times(1)
        ).findAllByOrderId(123);

        // No mapping should occur if repository returns null
        verify(
                orderLineMapper, never()
        ).orderLineToOrderLineResponseDTO(any());
    }

}
