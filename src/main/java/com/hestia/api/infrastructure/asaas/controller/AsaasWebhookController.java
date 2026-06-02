package com.hestia.api.infrastructure.asaas.controller;

import com.hestia.api.infrastructure.asaas.dto.AsaasWebhookPayload;
import com.hestia.api.infrastructure.asaas.service.AsaasWebhookService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/webhook/asaas")
@Tag(name = "Webhooks")
@RequiredArgsConstructor
public class AsaasWebhookController {

    private final AsaasWebhookService asaasWebhookService;

    @PostMapping("/{webhookToken}")
    public ResponseEntity<Void> handleWebhook(
            @PathVariable String webhookToken, @RequestBody AsaasWebhookPayload payload) {
        asaasWebhookService.handleWebhook(webhookToken, payload);
        return ResponseEntity.ok().build();
    }
}
