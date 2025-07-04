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

@Service
@RequiredArgsConstructor
public class OrderLineServiceImpl implements OrderLineService {

    private final OrderLineRepository orderLineRepository;
    private final OrderLineMapper orderLineMapper;

    @Override
    public Integer saveOrderLine(OrderLineRequestDTO orderLineRequestDTO) {
        return orderLineRepository.save(
                orderLineMapper.orderRequestDTOToOrderLine(orderLineRequestDTO)
        ).getId();
    }

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
