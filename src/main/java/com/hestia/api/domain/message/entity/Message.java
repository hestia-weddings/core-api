package com.hestia.api.domain.message.entity;

import com.hestia.api.common.model.BaseTenantModel;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message extends BaseTenantModel {

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "is_favorite", nullable = false)
    private Boolean isFavorite;

    @Column(name = "is_new", nullable = false)
    private Boolean isNew;
}
