package com.hestia.api.domain.payment.controller;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.payment.dto.CreateWalletRequest;
import com.hestia.api.domain.payment.dto.UpdateWalletRequest;
import com.hestia.api.domain.payment.dto.WalletResponse;
import com.hestia.api.domain.payment.service.WalletService;
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
@RequestMapping("wallet")
@Tag(name = "Payments")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping
    public ResponseEntity<PageResponse<WalletResponse>> getWallets(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam(required = false) UUID wedding,
            Pageable pageable) {
        return ResponseEntity.ok(walletService.getWallets(user.resolveWeddingId(wedding), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletResponse> getWalletById(
            @AuthenticationPrincipal AuthenticatedUser user, @PathVariable UUID id) {
        return ResponseEntity.ok(walletService.getWalletById(user.resolveWeddingId(), id));
    }

    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam(required = false) UUID wedding,
            @Valid @RequestBody CreateWalletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(walletService.createWallet(user.resolveWeddingId(wedding), request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WalletResponse> updateWallet(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody UpdateWalletRequest request,
            @PathVariable UUID id) {
        return ResponseEntity.ok(walletService.updateWallet(user.resolveWeddingId(), id, request));
    }
}
