package com.hestia.api.infrastructure.config;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.List;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class OpenApiConfig {

    private static final List<String> TAG_ORDER = List.of(
            "Weddings", "Accounts", "RSVP", "Registry", "Messages", "Guest", "Wallets", "Transfers", "Webhooks");

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hestia Core API")
                        .description("API for managing wedding events: accounts, RSVP, registry, and messages")
                        .version("2.0.0"))
                .tags(List.of(
                        // Auth modules
                        new Tag().name("Weddings").description("Authorization module management"),
                        new Tag().name("Accounts").description("Authorization module management"),
                        // Domain modules
                        new Tag().name("RSVP").description("Domain modules management"),
                        new Tag().name("Registry").description("Domain modules management"),
                        new Tag().name("Messages").description("Domain modules management"),
                        // Guests
                        new Tag().name("Guest").description("Public guest-facing endpoints"),
                        // Payment modules
                        new Tag().name("Wallets").description("Payment modules management"),
                        new Tag().name("Transfers").description("Payment modules management"),
                        // Webhook handlers
                        new Tag().name("Webhooks").description("Webhook calls handler")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Token"))
                .components(new Components()
                        .addSecuritySchemes(
                                "Bearer Token",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    @Bean
    public OpenApiCustomizer tagOrderCustomizer() {
        return openApi -> openApi.getTags().sort(Comparator.comparingInt(tag -> TAG_ORDER.indexOf(tag.getName())));
    }
}
