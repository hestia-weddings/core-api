package com.hestia.api.domain.message.entity;

import com.hestia.api.common.model.BaseModel;
import com.hestia.api.domain.wedding.entity.Wedding;

import jakarta.persistence.*;

import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "wedding_id")
    private Wedding wedding;
}
