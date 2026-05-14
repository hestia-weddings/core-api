package com.hestia.api.infrastructure.security.config;

import com.hestia.api.infrastructure.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // STRUCTURAL (ADMIN)
                        .requestMatchers("/account/**").hasRole("ADMIN")
                        .requestMatchers("/wedding/**").hasRole("ADMIN")

                        // GUEST (PUBLIC)
                        .requestMatchers("/w/**").permitAll()

                        // SWAGGER
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // ERROR
                        .requestMatchers("/error").permitAll()

                        // DOMAIN MANAGEMENT (COUPLE)
                        .anyRequest().hasRole("COUPLE")
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


}
