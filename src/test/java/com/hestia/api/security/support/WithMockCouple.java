package com.hestia.api.security.support;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithMockAuth(
        userId = "bbbb0000-0000-0000-0000-000000000001",
        authUserId = "bbbb0000-0000-0000-0000-bbbbbbbbbbbb",
        email = "couple@hestia.com",
        name = "Couple User",
        role = "COUPLE",
        weddingId = "11111111-1111-1111-1111-111111111111"
)
public @interface WithMockCouple {
}
