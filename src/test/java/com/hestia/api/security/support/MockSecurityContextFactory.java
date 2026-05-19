package com.hestia.api.security.support;

import com.hestia.api.domain.accounts.entity.User;
import com.hestia.api.domain.accounts.enums.UserRole;
import com.hestia.api.domain.wedding.entity.Wedding;
import com.hestia.api.infrastructure.security.principal.AuthenticatedUser;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.UUID;

public class MockSecurityContextFactory implements WithSecurityContextFactory<WithMockAuth> {

    @Override
    public SecurityContext createSecurityContext(WithMockAuth annotation) {
        User user = new User();
        user.setId(UUID.fromString(annotation.userId()));
        user.setAuthUserId(UUID.fromString(annotation.authUserId()));
        user.setEmail(annotation.email());
        user.setName(annotation.name());
        user.setRole(UserRole.valueOf(annotation.role()));
        user.setIsActive(true);

        if (!annotation.weddingId().isEmpty()) {
            Wedding wedding = new Wedding();
            wedding.setId(UUID.fromString(annotation.weddingId()));
            user.setWedding(wedding);
        }

        AuthenticatedUser principal = new AuthenticatedUser(user);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        return context;
    }
}
