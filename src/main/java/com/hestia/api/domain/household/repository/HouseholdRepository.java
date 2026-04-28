package com.hestia.api.domain.household.repository;

import com.hestia.api.domain.household.entity.Household;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HouseholdRepository extends JpaRepository<Household, UUID> {

    List<Household> findByIsActiveTrue();
}
