package com.hestia.api.domain.registry.repository;

import com.hestia.api.domain.registry.entity.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findByIdAndIsActiveTrue(UUID id);

    Page<Order> findByWeddingIdAndIsActiveTrue(UUID weddingId, Pageable pageable);

    Optional<Order> findByIdAndWeddingIdAndIsActiveTrue(UUID id, UUID weddingId);

    Optional<Order> findByPaymentIdAndIsActiveTrue(UUID paymentId);
}
