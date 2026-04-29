package com.hestia.api.domain.registry.repository;

import com.hestia.api.domain.registry.entity.Gift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GiftRepository extends JpaRepository<Gift, UUID> {

    List<Gift> findByIsActiveTrue();
}
