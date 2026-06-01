package com.hestia.api.domain.payment.service;

import com.hestia.api.common.exception.ResourceNotFoundException;
import com.hestia.api.domain.payment.entity.Transaction;
import com.hestia.api.domain.payment.entity.Wallet;
import com.hestia.api.domain.payment.enums.TransactionStatus;
import com.hestia.api.domain.payment.exception.InsufficientBalanceException;
import com.hestia.api.domain.payment.repository.TransactionRepository;
import com.hestia.api.domain.payment.repository.WalletRepository;
import com.hestia.api.domain.wedding.entity.Wedding;
import com.hestia.api.domain.wedding.repository.WeddingRepository;
import com.hestia.api.infrastructure.asaas.AsaasTransferClient;
import com.hestia.api.infrastructure.asaas.dto.AsaasTransferRequest;
import com.hestia.api.infrastructure.asaas.dto.AsaasTransferResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final WeddingRepository weddingRepository;
    private final AsaasTransferClient asaasTransferClient;

    @Transactional
    public Transaction requestTransfer(UUID weddingId, Integer amount) {
        Wallet wallet = walletRepository
                .findFirstByWeddingIdAndIsActiveTrue(weddingId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for wedding: " + weddingId));

        Wedding wedding = weddingRepository
                .findById(weddingId)
                .orElseThrow(() -> new ResourceNotFoundException("Wedding not found: " + weddingId));

        int fee = amount * wallet.getFee() / 10000;
        int availableBalance = wallet.getBalance() - transactionRepository.sumPendingAmountsByWalletId(wallet.getId());

        if (amount + fee > availableBalance) {
            throw new InsufficientBalanceException("Insufficient balance for transfer");
        }

        Transaction transaction = Transaction.builder()
                .wallet(wallet)
                .wedding(wedding)
                .amount(amount)
                .fee(fee)
                .status(TransactionStatus.PENDING)
                .build();

        AsaasTransferRequest asaasRequest = new AsaasTransferRequest(
                BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(100)), "PIX", wallet.getPixKey());

        AsaasTransferResponse response = asaasTransferClient.createTransfer(asaasRequest);
        transaction.setAsaasTransferId(response.id());

        return transactionRepository.save(transaction);
    }

    @Transactional
    public void handleTransferDone(String asaasTransferId) {
        Optional<Transaction> opt = transactionRepository.findByAsaasTransferId(asaasTransferId);
        if (opt.isEmpty()) return;

        Transaction transaction = opt.get();
        if (transaction.getStatus() != TransactionStatus.PENDING) return;

        transaction.setStatus(TransactionStatus.COMPLETED);
        Wallet wallet = transaction.getWallet();
        wallet.setBalance(wallet.getBalance() - transaction.getAmount() - transaction.getFee());
        walletRepository.save(wallet);
        transactionRepository.save(transaction);
    }

    @Transactional
    public void handleTransferFailed(String asaasTransferId) {
        Optional<Transaction> opt = transactionRepository.findByAsaasTransferId(asaasTransferId);
        if (opt.isEmpty()) return;

        Transaction transaction = opt.get();
        if (transaction.getStatus() != TransactionStatus.PENDING) return;

        transaction.setStatus(TransactionStatus.FAILED);
        transactionRepository.save(transaction);
    }
}
