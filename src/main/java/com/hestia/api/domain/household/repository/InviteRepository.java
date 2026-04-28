package com.hestia.api.domain.household.repository;

import com.hestia.api.domain.household.entity.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, UUID> {

    List<Invite> findByIsActiveTrue();
}
