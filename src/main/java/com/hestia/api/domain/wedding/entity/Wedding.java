package com.hestia.api.domain.wedding.entity;

import com.hestia.api.common.model.BaseModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

import lombok.*;

@Entity
@Table(name = "weddings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wedding extends BaseModel {

    @Column(name = "couple_name", nullable = false)
    private String coupleName;

    @Column
    private LocalDateTime date;

    @Column(name = "invite_message")
    private String inviteMessage;

    @Column(name = "gift_message")
    private String giftMessage;

    @Column(nullable = false, unique = true)
    private String slug;
}
