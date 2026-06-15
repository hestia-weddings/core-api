package com.hestia.api.infrastructure.security.config;

import com.hestia.api.infrastructure.security.filter.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // GUEST (PUBLIC)
                        .requestMatchers("/w/**")
                        .permitAll()

                        // WEBHOOK (PUBLIC)
                        .requestMatchers("/webhook/**")
                        .permitAll()

                        // SWAGGER
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()

                        // H2 DB
                        .requestMatchers("/h2-console", "/h2-console/**")
                        .permitAll()

                        // ERROR
                        .requestMatchers("/error")
                        .permitAll()

                        // ADMIN-ONLY (structural management)
                        .requestMatchers(HttpMethod.GET, "/account")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/account")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/account/{id}")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/wedding")
                        .hasAnyRole("ADMIN", "COUPLE")
                        .requestMatchers(HttpMethod.GET, "/wedding/{id}")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/wedding")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/wedding/{id}")
                        .hasRole("ADMIN")

                        // ALL AUTHENTICATED (ADMIN + COUPLE)
                        .anyRequest()
                        .hasAnyRole("ADMIN", "COUPLE"))
                .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter()
                            .write(
                                    "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Authentication required\"}");
                }))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
