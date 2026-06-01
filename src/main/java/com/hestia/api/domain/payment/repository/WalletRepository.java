package com.hestia.api.domain.payment.repository;

import com.hestia.api.domain.payment.entity.Wallet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    Page<Wallet> findByWeddingIdAndIsActiveTrue(UUID weddingId, Pageable pageable);

    Optional<Wallet> findByIdAndIsActiveTrue(UUID id);

    Optional<Wallet> findByIdAndWeddingIdAndIsActiveTrue(UUID id, UUID weddingId);

    boolean existsByWeddingIdAndIsActiveTrue(UUID weddingId);

    Optional<Wallet> findFirstByWeddingIdAndIsActiveTrue(UUID weddingId);
}
