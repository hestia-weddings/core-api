package com.hestia.api.infrastructure.security.jwt;

import java.util.Map;
import java.util.UUID;

public record JwtClaims(
        UUID subject,
        String email,
        Map<String, Object> claims
) {

    private Map<?, ?> getAppMetadata() {
        return (Map<?, ?>) claims.get("app_metadata");
    }

    public UUID getWeddingId() {
        Map<?, ?> appMetadata = getAppMetadata();

        if (appMetadata == null) return null;

        String weddingId = (String) appMetadata.get("wedding_id");

        return weddingId != null ? UUID.fromString(weddingId) : null;
    }
}
