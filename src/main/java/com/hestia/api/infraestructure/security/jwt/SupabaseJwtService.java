package com.hestia.api.infraestructure.security.jwt;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupabaseJwtService {

    private final JwtDecoder jwtDecoder;

    public JwtClaims validate(String token) {
        Jwt jwt = jwtDecoder.decode(token);

        return new JwtClaims(
                UUID.fromString(jwt.getSubject()),
                jwt.getClaimAsString("email"),
                jwt.getClaims()
        );
    }
}
