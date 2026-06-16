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
    public ResponseEntity<?> getWeddings(@AuthenticationPrincipal AuthenticatedUser user, Pageable pageable) {
        UUID weddingId = user.resolveWeddingId();
        if (weddingId != null) {
            return ResponseEntity.ok(weddingService.getWeddingById(weddingId));
        }
        return ResponseEntity.ok(weddingService.getWeddings(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeddingResponse> getWeddingById(@PathVariable UUID id) {
        return ResponseEntity.ok(weddingService.getWeddingById(id));
    }

    @PostMapping
    public ResponseEntity<WeddingResponse> postWedding(@Valid @RequestBody CreateWeddingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(weddingService.createWedding(request));
    }

    @PatchMapping
    public ResponseEntity<WeddingResponse> patchWedding(
            @AuthenticationPrincipal AuthenticatedUser user, @Valid @RequestBody UpdateWeddingRequest request) {
        return ResponseEntity.ok(weddingService.updateWedding(user.resolveWeddingId(), request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WeddingResponse> patchWeddingById(
            @PathVariable UUID id, @Valid @RequestBody UpdateWeddingRequest request) {
        return ResponseEntity.ok(weddingService.updateWedding(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWedding(@PathVariable UUID id) {
        weddingService.deleteWedding(id);
        return ResponseEntity.noContent().build();
    }
}
