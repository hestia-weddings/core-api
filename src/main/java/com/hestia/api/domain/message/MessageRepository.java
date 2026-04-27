package com.hestia.api.domain.message;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findByIsActiveTrue();
    List<Message> findByIsNewTrue();
    List<Message> findByIsFavoriteTrue();
}
