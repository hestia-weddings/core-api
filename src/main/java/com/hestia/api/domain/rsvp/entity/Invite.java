package com.hestia.api.domain.rsvp.entity;

import com.hestia.api.common.model.BaseTenantModel;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Entity
@Table(name = "invites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invite extends BaseTenantModel {

    @Column(nullable = false)
    private String name;

    @Column()
    private String phone;

    @OneToMany(mappedBy = "invite")
    @Builder.Default
    private List<Guest> guests = new ArrayList<>();
}
