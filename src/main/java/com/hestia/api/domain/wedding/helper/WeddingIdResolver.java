package com.hestia.api.domain.wedding.helper;

import com.hestia.api.infrastructure.security.principal.AuthenticatedUser;

import java.util.UUID;

public class WeddingIdResolver {
    public static UUID resolve(AuthenticatedUser user, UUID weddingId) {
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            System.out.println(weddingId);
            if (weddingId == null) throw new IllegalArgumentException("wedding_id is required");
            return weddingId;
        }
        return user.getWeddingId();
    }
}
