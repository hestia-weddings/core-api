package com.hestia.api.domain.accounts.entity;

import com.hestia.api.common.model.BaseModel;
import com.hestia.api.domain.accounts.enums.UserRole;
import com.hestia.api.domain.wedding.entity.Wedding;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseModel {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "user_role_enum", nullable = false)
    private UserRole role;

    @Column(name = "auth_user_id", nullable = false)
    private UUID authUserId;

    @ManyToOne
    @JoinColumn(name = "wedding_id")
    private Wedding wedding;
}
