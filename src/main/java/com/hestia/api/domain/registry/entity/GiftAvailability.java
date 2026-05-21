package com.hestia.api.domain.registry.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;

@Entity
@Table(name = "gift_availability")
@Immutable
@Getter
public class GiftAvailability {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String description;

    @Column
    private String picture;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Integer remain;

    @Column(nullable = false)
    private Boolean availability;

    @Column(name = "wedding_id", nullable = false)
    private UUID weddingId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
