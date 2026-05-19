package com.hestia.api.domain.rsvp.entity;

import com.hestia.api.common.model.BaseModel;
import com.hestia.api.domain.rsvp.enums.GuestAge;
import com.hestia.api.domain.rsvp.enums.GuestStatus;
import com.hestia.api.domain.wedding.entity.Wedding;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "guests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guest extends BaseModel {

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "age_group", columnDefinition = "age_group_enum", nullable = false)
    private GuestAge ageGroup;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "guest_status_enum", nullable = false)
    private GuestStatus status;

    @ManyToOne
    @JoinColumn(name = "invite_id")
    private Invite invite;

    @ManyToOne
    @JoinColumn(name = "wedding_id")
    private Wedding wedding;
}
