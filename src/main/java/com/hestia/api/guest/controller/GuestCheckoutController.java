package com.hestia.api.guest.controller;

import com.hestia.api.guest.dto.CheckoutRequest;
import com.hestia.api.guest.dto.CheckoutResponse;
import com.hestia.api.guest.service.GuestCheckoutService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/w/{slug}/gift/{giftId}/checkout")
@Tag(name = "Guest")
@RequiredArgsConstructor
public class GuestCheckoutController {

    private final GuestCheckoutService guestCheckoutService;

    @PostMapping
    public ResponseEntity<CheckoutResponse> checkout(
            @RequestAttribute UUID weddingId, @PathVariable UUID giftId, @Valid @RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(guestCheckoutService.createCheckout(weddingId, giftId, request));
    }
}
