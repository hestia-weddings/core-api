package com.hestia.api.domain.registry.service;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.common.exception.ResourceNotFoundException;
import com.hestia.api.common.mapper.PageMapper;
import com.hestia.api.domain.registry.dto.OrderResponse;
import com.hestia.api.domain.registry.entity.Order;
import com.hestia.api.domain.registry.mapper.OrderMapper;
import com.hestia.api.domain.registry.repository.OrderRepository;

import jakarta.annotation.Nullable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public PageResponse<OrderResponse> getOrders(@Nullable UUID weddingId, Pageable pageable) {
        Page<Order> page;

        if (weddingId == null) page = orderRepository.findAll(pageable);
        else page = orderRepository.findByWeddingIdAndIsActiveTrue(weddingId, pageable);

        return PageMapper.toResponse(page.map(orderMapper::toResponse));
    }

    public OrderResponse getOrderById(@Nullable UUID weddingId, UUID id) {
        Order order;

        if (weddingId == null)
            order = orderRepository
                    .findByIdAndIsActiveTrue(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        else
            order = orderRepository
                    .findByIdAndWeddingIdAndIsActiveTrue(id, weddingId)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return orderMapper.toResponse(order);
    }
}
