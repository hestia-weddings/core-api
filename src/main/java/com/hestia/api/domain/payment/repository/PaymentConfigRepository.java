package com.hestia.api.domain.payment.repository;

import com.hestia.api.domain.payment.entity.PaymentConfig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentConfigRepository extends JpaRepository<PaymentConfig, UUID> {

    Page<PaymentConfig> findByWeddingIdAndIsActiveTrue(UUID weddingId, Pageable pageable);

    Optional<PaymentConfig> findByIdAndIsActiveTrue(UUID id);

    Optional<PaymentConfig> findByIdAndWeddingIdAndIsActiveTrue(UUID id, UUID weddingId);

    Optional<PaymentConfig> findFirstByWeddingIdAndIsActiveTrue(UUID weddingId);

    boolean existsByWeddingIdAndIsActiveTrue(UUID weddingId);
}
