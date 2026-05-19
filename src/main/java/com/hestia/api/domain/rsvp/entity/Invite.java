package com.hestia.api.domain.rsvp.entity;

import com.hestia.api.common.model.BaseModel;
import com.hestia.api.domain.wedding.entity.Wedding;

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
public class Invite extends BaseModel {

    @Column(nullable = false)
    private String name;

    @Column()
    private String phone;

    @OneToMany(mappedBy = "invite")
    @Builder.Default
    private List<Guest> guests = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "wedding_id")
    private Wedding wedding;
}
