package com.hestia.api.domain.rsvp.repository;

import com.hestia.api.domain.rsvp.entity.Household;
import com.hestia.api.domain.rsvp.entity.Guest;
import com.hestia.api.domain.rsvp.enums.GuestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GuestRepository extends JpaRepository<Guest, UUID> {

    Page<Guest> findByIsActiveTrue(Pageable pageable);
    Page<Guest> findByStatusAndIsActiveTrue(Pageable pageable, GuestStatus status);
    Page<Guest> findByHouseholdAndIsActiveTrue(Pageable pageable, Household household);
}
