package com.hestia.api.domain.rsvp.entity;

import com.hestia.api.common.model.BaseTenantModel;
import com.hestia.api.domain.rsvp.enums.GuestAge;
import com.hestia.api.domain.rsvp.enums.GuestStatus;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import lombok.*;

@Entity
@Table(name = "guests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guest extends BaseTenantModel {

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "age_group", columnDefinition = "age_group_enum", nullable = false)
    private GuestAge ageGroup;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(columnDefinition = "guest_status_enum", nullable = false)
    private GuestStatus status;

    @ManyToOne
    @JoinColumn(name = "invite_id")
    private Invite invite;
}
