package com.hestia.api.domain.household.entity;

import com.hestia.api.common.model.BaseModel;
import com.hestia.api.domain.household.enums.InviteAge;
import com.hestia.api.domain.household.enums.InviteStatus;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "age_group", nullable = false)
    private InviteAge ageGroup;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InviteStatus status;

    @ManyToOne
    @JoinColumn(name = "household_id")
    private Household household;
}
