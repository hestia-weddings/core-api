package com.hestia.api.domain.payment.mapper;

import com.hestia.api.domain.payment.dto.WalletResponse;
import com.hestia.api.domain.payment.entity.Wallet;
import com.hestia.api.domain.payment.repository.TransactionRepository;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WalletMapper {

    private final TransactionRepository transactionRepository;

    public WalletResponse toResponse(Wallet wallet) {
        int pendingAmount = transactionRepository.sumPendingAmountsByWalletId(
                wallet.getId(), com.hestia.api.domain.payment.enums.TransactionStatus.PENDING.name());
        int netBalance = (wallet.getBalance() - pendingAmount) * 10000 / (10000 + wallet.getFee());

        return WalletResponse.builder()
                .id(wallet.getId())
                .pixKey(wallet.getPixKey())
                .availableBalance(netBalance)
                .createdAt(wallet.getCreatedAt())
                .build();
    }
}
