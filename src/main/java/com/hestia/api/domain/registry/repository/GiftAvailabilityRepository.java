package com.hestia.api.domain.registry.repository;

import com.hestia.api.domain.registry.entity.GiftAvailability;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GiftAvailabilityRepository extends JpaRepository<GiftAvailability, UUID> {

    Page<GiftAvailability> findByWeddingId(UUID weddingId, Pageable pageable);

    Optional<GiftAvailability> findByIdAndWeddingId(UUID id, UUID weddingId);
}
