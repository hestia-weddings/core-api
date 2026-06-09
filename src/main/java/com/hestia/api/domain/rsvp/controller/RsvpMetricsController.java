package com.hestia.api.domain.rsvp.controller;

import com.hestia.api.domain.rsvp.dto.RsvpMetricsResponse;
import com.hestia.api.domain.rsvp.service.RsvpMetricsService;
import com.hestia.api.infrastructure.security.principal.AuthenticatedUser;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rsvp/metrics")
@Tag(name = "RSVP")
@RequiredArgsConstructor
public class RsvpMetricsController {

    private final RsvpMetricsService rsvpMetricsService;

    @GetMapping
    public ResponseEntity<RsvpMetricsResponse> generateRsvpMetrics(
            @AuthenticationPrincipal AuthenticatedUser user, @RequestParam(required = false) UUID wedding) {
        return ResponseEntity.ok(rsvpMetricsService.generateRsvpMetrics(user.resolveWeddingId(wedding)));
    }
}
