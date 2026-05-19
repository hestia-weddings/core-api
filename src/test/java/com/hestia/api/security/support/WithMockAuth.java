package com.hestia.api.security.support;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockSecurityContextFactory.class)
public @interface WithMockAuth {
    String userId();

    String authUserId();

    String email();

    String name() default "Test User";

    String role();

    String weddingId() default "";
}
