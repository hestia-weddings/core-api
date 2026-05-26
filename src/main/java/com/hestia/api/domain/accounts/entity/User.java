package com.hestia.api.domain.accounts.entity;

import com.hestia.api.common.model.BaseTenantModel;
import com.hestia.api.domain.accounts.enums.UserRole;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BaseTenantModel {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(columnDefinition = "user_role_enum", nullable = false)
    private UserRole role;

    @Column(name = "auth_user_id", nullable = false)
    private UUID authUserId;
}
