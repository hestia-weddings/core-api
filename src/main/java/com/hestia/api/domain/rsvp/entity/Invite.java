package com.hestia.api.domain.rsvp.entity;

import com.hestia.api.common.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;
import java.util.UUID;

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
    private List<Guest> guests;

    @Column(name = "wedding_id", nullable = false)
    private UUID weddingId;
}
