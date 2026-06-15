package com.hestia.api.domain.wedding.controller;

import com.hestia.api.domain.wedding.dto.CreateWeddingRequest;
import com.hestia.api.domain.wedding.dto.UpdateWeddingRequest;
import com.hestia.api.domain.wedding.dto.WeddingResponse;
import com.hestia.api.domain.wedding.service.WeddingService;
import com.hestia.api.infrastructure.security.principal.AuthenticatedUser;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/wedding")
@Tag(name = "Weddings")
@RequiredArgsConstructor
public class WeddingController {

    private final WeddingService weddingService;

    @GetMapping
    public ResponseEntity<?> getWeddings(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam(name = "wedding_id", required = false) UUID weddingId,
            Pageable pageable) {
        boolean isCouple =
                user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COUPLE"));

        if (isCouple) {
            return ResponseEntity.ok(weddingService.getWeddingById(user.getWeddingId()));
        }

        if (weddingId != null) {
            return ResponseEntity.ok(weddingService.getWeddingById(weddingId));
        }

        return ResponseEntity.ok(weddingService.getWeddings(pageable));
    }

    @PostMapping
    public ResponseEntity<WeddingResponse> postWedding(@Valid @RequestBody CreateWeddingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(weddingService.createWedding(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeddingResponse> getWeddingById(@PathVariable UUID id) {
        return ResponseEntity.ok(weddingService.getWeddingById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WeddingResponse> patchWedding(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateWeddingRequest request) {
        validateWeddingAccess(user, id);
        return ResponseEntity.ok(weddingService.updateWedding(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWedding(@PathVariable UUID id) {
        weddingService.deleteWedding(id);
        return ResponseEntity.noContent().build();
    }

    private void validateWeddingAccess(AuthenticatedUser user, UUID weddingId) {
        boolean isCouple =
                user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COUPLE"));
        if (isCouple && !weddingId.equals(user.getWeddingId())) {
            throw new AccessDeniedException("Access denied");
        }
    }
}
