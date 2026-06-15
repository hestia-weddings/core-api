package com.hestia.api.domain.rsvp.repository;

import com.hestia.api.domain.rsvp.entity.Invite;
import com.hestia.api.domain.rsvp.enums.GuestStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, UUID> {

    Page<Invite> findByWeddingIdAndIsActiveTrue(UUID weddingId, Pageable pageable);

    @Query("SELECT DISTINCT i FROM Invite i JOIN i.guests g WHERE i.wedding.id = :weddingId"
            + " AND i.isActive = true AND g.isActive = true AND g.status = :status")
    Page<Invite> findByWeddingIdAndGuestStatus(
            @Param("weddingId") UUID weddingId, @Param("status") GuestStatus status, Pageable pageable);

    Optional<Invite> findByIdAndIsActiveTrue(UUID id);

    Optional<Invite> findByIdAndWeddingIdAndIsActiveTrue(UUID id, UUID weddingId);

    Optional<Invite> findByNameIgnoreCaseAndWeddingIdAndIsActiveTrue(String name, UUID weddingId);
}
