package com.hestia.api.domain.wedding.repository;

import com.hestia.api.domain.wedding.entity.Wedding;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WeddingRepository extends JpaRepository<Wedding, UUID> {

    Page<Wedding> findByIsActiveTrue(Pageable pageable);

    Optional<Wedding> findByIdAndIsActiveTrue(UUID id);

    Optional<Wedding> findBySlugAndIsActiveTrue(String slug);
}
