package com.hestia.api.infrastructure.security.principal;

import com.hestia.api.domain.accounts.entity.User;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
public class AuthenticatedUser implements UserDetails {

    private final UUID id;
    private final UUID authUserId;
    private final UUID weddingId;
    private final String email;
    private final boolean isActive;
    private final Collection<SimpleGrantedAuthority> authorities;

    public AuthenticatedUser(User user) {
        this.id = user.getId();
        this.authUserId = user.getAuthUserId();
        this.weddingId = user.getWedding() != null ? user.getWedding().getId() : null;
        this.email = user.getEmail();
        this.isActive = user.getIsActive();
        this.authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
