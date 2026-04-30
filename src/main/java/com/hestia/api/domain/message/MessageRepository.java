package com.hestia.api.domain.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    Page<Message> findByIsActiveTrue(Pageable pageable);
    Page<Message> findByIsNewTrueAndIsActiveTrue(Pageable pageable);
    Page<Message> findByIsFavoriteTrueAndIsActiveTrue(Pageable pageable);
}
