package com.hestia.api.domain.payment.controller;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.payment.dto.CreatePaymentConfigRequest;
import com.hestia.api.domain.payment.dto.PaymentConfigResponse;
import com.hestia.api.domain.payment.dto.UpdatePaymentConfigRequest;
import com.hestia.api.domain.payment.service.PaymentConfigService;
import com.hestia.api.infrastructure.security.principal.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("payment-config")
@Tag(name = "Payment Configs", description = "CRUD operations for payment configs management")
@RequiredArgsConstructor
public class PaymentConfigController {

    private final PaymentConfigService paymentConfigService;

    @GetMapping
    public ResponseEntity<PageResponse<PaymentConfigResponse>> getPaymentConfig(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam(required = false) UUID wedding,
            Pageable pageable) {
        return ResponseEntity.ok(
                paymentConfigService.getPaymentConfigs(user.resolveWeddingId(wedding), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentConfigResponse> getPaymentConfigById(
            @AuthenticationPrincipal AuthenticatedUser user, @PathVariable UUID id) {
        return ResponseEntity.ok(paymentConfigService.getPaymentConfigById(user.resolveWeddingId(), id));
    }

    @PostMapping
    public ResponseEntity<PaymentConfigResponse> postPaymentConfig(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam(required = false) UUID wedding,
            @Valid @RequestBody CreatePaymentConfigRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentConfigService.createPaymentConfig(user.resolveWeddingId(wedding), request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PaymentConfigResponse> updatePaymentConfig(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody UpdatePaymentConfigRequest request,
            @PathVariable UUID id) {
        return ResponseEntity.ok(paymentConfigService.updatePaymentConfig(user.resolveWeddingId(), id, request));
    }
}
