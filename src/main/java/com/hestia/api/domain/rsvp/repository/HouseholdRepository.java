package com.hestia.api.domain.rsvp.repository;

import com.hestia.api.domain.rsvp.entity.Household;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HouseholdRepository extends JpaRepository<Household, UUID> {

    Page<Household> findByIsActiveTrue(Pageable pageable);
}
