package com.hestia.api.infraestructure.security.config;

import com.hestia.api.infraestructure.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                        // ACCOUNT
                        .requestMatchers(HttpMethod.PATCH, "/account/**").hasRole("COUPLE")
                        .requestMatchers("/account", "/account/**").hasRole("ADMIN")

                        // MESSAGE
                        .requestMatchers(HttpMethod.POST, "/message").permitAll()

                        // REGISTRY
                        .requestMatchers(HttpMethod.GET, "/gift", "/gift/**").permitAll()

                        // RSVP
                        .requestMatchers(HttpMethod.POST, "/rsvp/invite/search").permitAll()
                        .requestMatchers(HttpMethod.GET, "/rsvp/guest").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/rsvp/guest/status/**").permitAll()

                        // SWAGGER
                        .requestMatchers( "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // GENERAL
                        .anyRequest().hasRole("COUPLE")
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


}
