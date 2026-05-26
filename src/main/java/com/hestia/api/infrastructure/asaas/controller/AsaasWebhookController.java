package com.hestia.api.infrastructure.asaas.controller;

import com.hestia.api.infrastructure.asaas.dto.AsaasWebhookPayload;
import com.hestia.api.infrastructure.asaas.service.AsaasWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook/asaas")
@RequiredArgsConstructor
public class AsaasWebhookController {

    private final AsaasWebhookService asaasWebhookService;

    @PostMapping("/{webhookToken}")
    public ResponseEntity<Void> handleWebhook(
            @PathVariable String webhookToken,
            @RequestBody AsaasWebhookPayload  payload) {
        asaasWebhookService.handleWebhook(webhookToken, payload);
        return ResponseEntity.ok().build();
    }
}
