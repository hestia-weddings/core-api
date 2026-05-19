package com.hestia.api.domain.rsvp.repository;

import com.hestia.api.domain.rsvp.entity.Guest;
import com.hestia.api.domain.rsvp.enums.GuestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GuestRepository extends JpaRepository<Guest, UUID> {

    Page<Guest> findByWeddingIdAndIsActiveTrue(UUID weddingId, Pageable pageable);
    Page<Guest> findByWeddingIdAndStatusAndIsActiveTrue(UUID weddingId, GuestStatus status, Pageable pageable);
    Page<Guest> findByWeddingIdAndInviteIdAndIsActiveTrue(UUID weddingId, UUID inviteId, Pageable pageable);
    Optional<Guest> findByIdAndIsActiveTrue(UUID id);
    Optional<Guest> findByIdAndWeddingIdAndIsActiveTrue(UUID id, UUID weddingId);
}
