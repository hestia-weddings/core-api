package com.hestia.api.domain.registry.repository;

import com.hestia.api.domain.registry.entity.GiftAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GiftAvailabilityRepository extends JpaRepository<GiftAvailability, UUID> {

    List<GiftAvailability> findByAvailabilityTrue();
}
