package com.hestia.api.domain.accounts.repository;

import com.hestia.api.domain.accounts.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Page<User> findByWeddingIdAndIsActiveTrue(UUID weddingId, Pageable pageable);
    Optional<User> findByIdAndWeddingIdAndIsActiveTrue(UUID id, UUID weddingId);
    User findByAuthUserId(UUID id);
}
