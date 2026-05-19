package com.hestia.api.domain.message.repository;

import com.hestia.api.domain.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    Page<Message> findByWeddingIdAndIsActiveTrue(UUID weddingId, Pageable pageable);
    Optional<Message> findByIdAndWeddingId(UUID id, UUID weddingId);
    Optional<Message> findByIdAndIsActiveTrue(UUID id);
    Page<Message> findByWeddingIdAndIsNewTrueAndIsActiveTrue(UUID weddingId, Pageable pageable);
    Page<Message> findByWeddingIdAndIsFavoriteTrueAndIsActiveTrue(UUID weddingId, Pageable pageable);
    Optional<Message> findByIdAndWeddingIdAndIsActiveTrue(UUID id, UUID weddingId);
}
