package com.hestia.api.infrastructure.security.jwt;

import java.util.Map;
import java.util.UUID;

public record JwtClaims(UUID subject, String email, Map<String, Object> claims) {}
