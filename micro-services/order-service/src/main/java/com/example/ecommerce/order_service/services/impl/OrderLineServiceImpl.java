package com.example.ecommerce.order_service.services.impl;

import com.example.ecommerce.order_service.mappers.OrderLineMapper;
import com.example.ecommerce.order_service.models.OrderLineRequestDTO;
import com.example.ecommerce.order_service.models.OrderLineResponseDTO;
import com.example.ecommerce.order_service.repositories.OrderLineRepository;
import com.example.ecommerce.order_service.services.OrderLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the OrderLineService interface.
 * Provides methods for saving and retrieving order line data.
 */
@Service
@RequiredArgsConstructor
public class OrderLineServiceImpl implements OrderLineService {

    // Repository for accessing order line data from the database.
    private final OrderLineRepository orderLineRepository;

    // Mapper for converting between DTOs and entity objects.
    private final OrderLineMapper orderLineMapper;

    /**
     * Saves an order line to the database.
     *
     * @param orderLineRequestDTO The DTO containing order line data to be saved.
     * @return The ID of the saved order line.
     */
    @Override
    public Integer saveOrderLine(OrderLineRequestDTO orderLineRequestDTO) {
        return orderLineRepository.save(
                orderLineMapper.orderRequestDTOToOrderLine(orderLineRequestDTO)
        ).getId();
    }

    /**
     * Retrieves all order lines associated with a specific order ID.
     *
     * @param orderId The ID of the order whose order lines are to be retrieved.
     * @return A list of OrderLineResponseDTOs representing the order lines.
     * Returns an empty list if no order lines are found.
     */
    @Override
    public List<OrderLineResponseDTO> findAllByOrderId(Integer orderId) {
        return Optional.ofNullable(
                        orderLineRepository.findAllByOrderId(orderId)
                ).orElseGet(
                        Collections::emptyList
                )
                .stream()
                .map(orderLineMapper::orderLineToOrderLineResponseDTO)
                .collect(Collectors.toList());
    }

}
