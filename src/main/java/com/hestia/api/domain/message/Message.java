package com.hestia.api.domain.message;

import com.hestia.api.common.model.BaseModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message extends BaseModel {

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "is_favorite", nullable = false)
    private Boolean isFavorite;

    @Column(name = "is_new", nullable = false)
    private Boolean isNew;

    @Column(name = "wedding_id", nullable = false)
    private UUID weddingId;
}
