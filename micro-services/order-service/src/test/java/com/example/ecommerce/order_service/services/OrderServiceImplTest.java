package com.example.ecommerce.order_service.services;

import com.example.ecommerce.order_service.entities.CustomerOrder;
import com.example.ecommerce.order_service.exceptions.BusinessException;
import com.example.ecommerce.order_service.feign_client.CustomerFeignClient;
import com.example.ecommerce.order_service.feign_client.PaymentFeignClient;
import com.example.ecommerce.order_service.mappers.OrderMapper;
import com.example.ecommerce.order_service.models.*;
import com.example.ecommerce.order_service.repositories.OrderRepository;
import com.example.ecommerce.order_service.services.impl.OrderServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CustomerFeignClient customerFeignClient;
    @Mock
    private PaymentFeignClient paymentFeignClient;
    @Mock
    private ProductService productService;
    @Mock
    private OrderLineService orderLineService;
    @Mock
    private OrderProducerService orderProducerService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderRequestDTO orderRequestDTO;
    private CustomerResponseDTO customerResponseDTO;
    private CustomerOrder order;
    private List<ProductPurchaseResponseDTO> purchasedProducts;

    @BeforeEach
    void setUp() {
        orderRequestDTO = mock(OrderRequestDTO.class);
        customerResponseDTO = mock(CustomerResponseDTO.class);
        order = mock(CustomerOrder.class);
        purchasedProducts = List.of(mock(ProductPurchaseResponseDTO.class));
    }

    @Test
    void createOrder_success() {
        when(orderRequestDTO.customerId()).thenReturn(String.valueOf(1));
        when(customerFeignClient.findCustomerById(String.valueOf(1))).thenReturn(Optional.of(customerResponseDTO));
        when(productService.executePurchaseProducts(anyList())).thenReturn(purchasedProducts);
        when(orderMapper.orderRequestDTOToOrder(orderRequestDTO)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(order.getId()).thenReturn(100);
        when(orderRequestDTO.purchases()).thenReturn(List.of(mock(ProductPurchaseRequestDTO.class)));
        when(orderRequestDTO.amount()).thenReturn(BigDecimal.valueOf(200.0));
        when(orderRequestDTO.paymentMethod()).thenReturn(PaymentMethod.CREDIT_CARD);
        when(order.getReference()).thenReturn("REF123");
        when(orderRequestDTO.reference()).thenReturn("REF123");

        Integer orderId = orderService.createOrder(orderRequestDTO);
        assertEquals(100, orderId);
        verify(paymentFeignClient).requestOrderPayment(any(PaymentRequestDTO.class));
        verify(orderProducerService).sendOrderConfirmation(any(OrderConfirmationDTO.class));
    }

    @Test
    void createOrder_customerNotFound_throwsBusinessException() {
        when(orderRequestDTO.customerId()).thenReturn(String.valueOf(2));
        when(customerFeignClient.findCustomerById(String.valueOf(2))).thenReturn(Optional.empty());
        BusinessException ex = assertThrows(BusinessException.class, () -> orderService.createOrder(orderRequestDTO));
        assertTrue(ex.getMessage().contains("Customer not found"));
    }

    @Test
    void findAllOrders_returnsMappedList() {
        var order1 = mock(CustomerOrder.class);
        var order2 = mock(CustomerOrder.class);
        OrderResponseDTO dto1 = mock(OrderResponseDTO.class);
        OrderResponseDTO dto2 = mock(OrderResponseDTO.class);
        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));
        when(orderMapper.orderToOrderResponseDTO(order1)).thenReturn(dto1);
        when(orderMapper.orderToOrderResponseDTO(order2)).thenReturn(dto2);
        List<OrderResponseDTO> result = orderService.findAllOrders();
        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
    }

    @Test
    void findOrderById_found() {
        var order = mock(CustomerOrder.class);
        OrderResponseDTO dto = mock(OrderResponseDTO.class);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderMapper.orderToOrderResponseDTO(order)).thenReturn(dto);
        OrderResponseDTO result = orderService.findOrderById(1);
        assertEquals(dto, result);
    }

    @Test
    void findOrderById_notFound_throwsEntityNotFoundException() {
        when(orderRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> orderService.findOrderById(99));
    }

    @Test
    void createOrder_handlesEmptyPurchases() {
        when(orderRequestDTO.customerId()).thenReturn(String.valueOf(1));
        when(customerFeignClient.findCustomerById(String.valueOf(1))).thenReturn(Optional.of(customerResponseDTO));
        when(orderRequestDTO.purchases()).thenReturn(List.of());
        when(productService.executePurchaseProducts(anyList())).thenReturn(List.of());
        when(orderMapper.orderRequestDTOToOrder(orderRequestDTO)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(order.getId()).thenReturn(101);
        when(orderRequestDTO.amount()).thenReturn(BigDecimal.valueOf(0.0));
        when(orderRequestDTO.paymentMethod()).thenReturn(PaymentMethod.CREDIT_CARD);
        when(order.getReference()).thenReturn("REFEMPTY");
        when(orderRequestDTO.reference()).thenReturn("REFEMPTY");

        Integer orderId = orderService.createOrder(orderRequestDTO);
        assertEquals(101, orderId);
        verify(orderLineService, never()).saveOrderLine(any());
    }

    @Test
    void createOrder_paymentFeignThrowsException_propagates() {
        when(orderRequestDTO.customerId()).thenReturn(String.valueOf(1));
        when(customerFeignClient.findCustomerById(String.valueOf(1))).thenReturn(Optional.of(customerResponseDTO));
        when(productService.executePurchaseProducts(anyList())).thenReturn(purchasedProducts);
        when(orderMapper.orderRequestDTOToOrder(orderRequestDTO)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(order.getId()).thenReturn(102);
        when(orderRequestDTO.purchases()).thenReturn(List.of(mock(ProductPurchaseRequestDTO.class)));
        when(orderRequestDTO.amount()).thenReturn(BigDecimal.valueOf(100.0));
        when(orderRequestDTO.paymentMethod()).thenReturn(PaymentMethod.CREDIT_CARD);
        when(order.getReference()).thenReturn("REFPAY");
        doThrow(new RuntimeException("Payment failed")).when(paymentFeignClient).requestOrderPayment(any());
        assertThrows(RuntimeException.class, () -> orderService.createOrder(orderRequestDTO));
    }
}
