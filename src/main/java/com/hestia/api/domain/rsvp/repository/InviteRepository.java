package com.hestia.api.domain.rsvp.repository;

import com.hestia.api.domain.rsvp.entity.Household;
import com.hestia.api.domain.rsvp.entity.Invite;
import com.hestia.api.domain.rsvp.enums.InviteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, UUID> {

    Page<Invite> findByIsActiveTrue(Pageable pageable);
    Page<Invite> findByStatusAndIsActiveTrue(Pageable pageable, InviteStatus status);
    Page<Invite> findByHouseholdAndIsActiveTrue(Pageable pageable, Household household);
}
