package com.hestia.api.domain.payment.mapper;

import com.hestia.api.domain.payment.dto.TransferResponse;
import com.hestia.api.domain.payment.entity.Transaction;

import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransferResponse toTransferResponse(Transaction transaction) {
        return TransferResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .fee(transaction.getFee())
                .netAmount(transaction.getAmount() - transaction.getFee())
                .status(transaction.getStatus().name())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
