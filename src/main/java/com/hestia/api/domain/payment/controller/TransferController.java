package com.hestia.api.domain.payment.controller;

import com.hestia.api.domain.payment.dto.TransferRequest;
import com.hestia.api.domain.payment.dto.TransferResponse;
import com.hestia.api.domain.payment.entity.Transaction;
import com.hestia.api.domain.payment.mapper.TransactionMapper;
import com.hestia.api.domain.payment.service.TransferService;
import com.hestia.api.infrastructure.security.principal.AuthenticatedUser;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transfers")
@Tag(name = "Transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;
    private final TransactionMapper transactionMapper;

    @PostMapping
    public ResponseEntity<TransferResponse> createTransfer(
            @AuthenticationPrincipal AuthenticatedUser user, @Valid @RequestBody TransferRequest request) {
        Transaction transaction = transferService.requestTransfer(user.resolveWeddingId(), request.getAmount());
        return ResponseEntity.ok(transactionMapper.toTransferResponse(transaction));
    }
}
