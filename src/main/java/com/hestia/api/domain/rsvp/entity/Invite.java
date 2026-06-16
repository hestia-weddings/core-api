package com.hestia.api.domain.rsvp.entity;

import com.hestia.api.common.model.BaseTenantModel;
import com.hestia.api.domain.message.entity.Message;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "invites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Invite extends BaseTenantModel {

    @Column(nullable = false)
    private String name;

    @Column()
    private String phone;

    @OneToMany(mappedBy = "invite")
    @Builder.Default
    private List<Guest> guests = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;
}
