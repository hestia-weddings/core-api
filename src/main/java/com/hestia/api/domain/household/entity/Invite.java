package com.hestia.api.domain.household.entity;

import com.hestia.api.common.model.BaseModel;
import com.hestia.api.domain.household.enums.InviteAge;
import com.hestia.api.domain.household.enums.InviteStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "age_group", columnDefinition = "age_group_enum", nullable = false)
    private InviteAge ageGroup;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "invite_status_enum", nullable = false)
    private InviteStatus status;

    @ManyToOne
    @JoinColumn(name = "household_id")
    private Household household;

    @Column(name = "wedding_id", nullable = false)
    private UUID weddingId;
}
