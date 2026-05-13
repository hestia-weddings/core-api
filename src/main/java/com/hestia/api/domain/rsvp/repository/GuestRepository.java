package com.hestia.api.domain.rsvp.repository;

import com.hestia.api.domain.rsvp.entity.Invite;
import com.hestia.api.domain.rsvp.entity.Guest;
import com.hestia.api.domain.rsvp.enums.GuestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GuestRepository extends JpaRepository<Guest, UUID> {

    Page<Guest> findByIsActiveTrue(Pageable pageable);
    Page<Guest> findByStatusAndIsActiveTrue(Pageable pageable, GuestStatus status);
    Page<Guest> findByInviteAndIsActiveTrue(Pageable pageable, Invite invite);
    Optional<Guest> findByIdAndIsActiveTrue(UUID id);
}
