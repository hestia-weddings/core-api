package com.hestia.api.guest.controller;

import com.hestia.api.domain.wedding.service.WeddingService;
import com.hestia.api.guest.dto.GiftMessageResponse;
import com.hestia.api.guest.dto.InviteMessageResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("w/{slug}/wedding")
@Tag(name = "Weddings")
@RequiredArgsConstructor
public class GuestWeddingController {

    private final WeddingService weddingService;

    @GetMapping("/invite")
    public ResponseEntity<InviteMessageResponse> getInviteMessage(@RequestAttribute UUID weddingId) {
        return ResponseEntity.ok(weddingService.getInviteMessage(weddingId));
    }

    @GetMapping("/gift")
    public ResponseEntity<GiftMessageResponse> getGiftMessage(@RequestAttribute UUID weddingId) {
        return ResponseEntity.ok(weddingService.getGiftMessage(weddingId));
    }
}
