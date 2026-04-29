package com.hestia.api.domain.rsvp.repository;

import com.hestia.api.domain.rsvp.entity.Household;
import com.hestia.api.domain.rsvp.entity.Invite;
import com.hestia.api.domain.rsvp.enums.InviteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, UUID> {

    List<Invite> findByIsActiveTrue();
    List<Invite> findByStatusAndIsActiveTrue(InviteStatus status);
    List<Invite> findByHouseholdAndIsActiveTrue(Household household);
}
