package com.hestia.api.domain.rsvp.repository;

import com.hestia.api.domain.rsvp.entity.Invite;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, UUID> {

    Page<Invite> findByWeddingIdAndIsActiveTrue(UUID weddingId, Pageable pageable);

    @Query(
            value = "SELECT DISTINCT i.* FROM invites i JOIN guests g ON i.id = g.invite_id"
                    + " WHERE i.wedding_id = :weddingId AND i.is_active = true"
                    + " AND g.is_active = true AND g.status = CAST(:status AS guest_status_enum)",
            countQuery = "SELECT COUNT(DISTINCT i.id) FROM invites i JOIN guests g ON i.id = g.invite_id"
                    + " WHERE i.wedding_id = :weddingId AND i.is_active = true"
                    + " AND g.is_active = true AND g.status = CAST(:status AS guest_status_enum)",
            nativeQuery = true)
    Page<Invite> findByWeddingIdAndGuestStatus(
            @Param("weddingId") UUID weddingId, @Param("status") String status, Pageable pageable);

    Optional<Invite> findByIdAndIsActiveTrue(UUID id);

    Optional<Invite> findByIdAndWeddingIdAndIsActiveTrue(UUID id, UUID weddingId);

    Optional<Invite> findByNameIgnoreCaseAndWeddingIdAndIsActiveTrue(String name, UUID weddingId);
}
