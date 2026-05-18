package com.hestia.api.security.support;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithMockAuth(
        userId = "aaaa0000-0000-0000-0000-000000000001",
        authUserId = "aaaa0000-0000-0000-0000-aaaaaaaaaaaa",
        email = "admin@hestia.com",
        name = "Admin User",
        role = "ADMIN",
        weddingId = "11111111-1111-1111-1111-111111111111"
)
public @interface WithMockAdmin {
}
