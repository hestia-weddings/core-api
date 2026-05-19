package com.hestia.api.domain.rsvp.repository;

import com.hestia.api.domain.rsvp.entity.Invite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, UUID> {

    Page<Invite> findByWeddingIdAndIsActiveTrue(UUID weddingId, Pageable pageable);
    Optional<Invite> findByIdAndIsActiveTrue(UUID id);
    Optional<Invite> findByIdAndWeddingIdAndIsActiveTrue(UUID id, UUID weddingId);
    Optional<Invite> findByNameIgnoreCaseAndWeddingIdAndIsActiveTrue(String name, UUID weddingId);
}
