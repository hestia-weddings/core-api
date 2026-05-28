package com.hestia.api.domain.payment.mapper;

import com.hestia.api.domain.payment.dto.WalletResponse;
import com.hestia.api.domain.payment.entity.Wallet;

import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    public WalletResponse toResponse(Wallet wallet) {
        return WalletResponse.builder()
                .id(wallet.getId())
                .pixKey(wallet.getPixKey())
                .balance(wallet.getBalance())
                .fee(wallet.getFee())
                .createdAt(wallet.getCreatedAt())
                .build();
    }
}
