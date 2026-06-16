package com.hestia.api.domain.message.entity;

import com.hestia.api.common.model.BaseTenantModel;
import com.hestia.api.domain.message.enums.MessageType;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Message extends BaseTenantModel {

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "is_favorite", nullable = false)
    private Boolean isFavorite;

    @Column(name = "is_new", nullable = false)
    private Boolean isNew;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, columnDefinition = "message_type_enum")
    private MessageType type;
}
