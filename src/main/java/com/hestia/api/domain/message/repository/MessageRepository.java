package com.hestia.api.domain.message.repository;

import com.hestia.api.domain.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    Page<Message> findByIsActiveTrue(Pageable pageable);
    Page<Message> findByIsNewTrueAndIsActiveTrue(Pageable pageable);
    Page<Message> findByIsFavoriteTrueAndIsActiveTrue(Pageable pageable);
    Optional<Message> findByIdAndIsActiveTrue(UUID id);
}
