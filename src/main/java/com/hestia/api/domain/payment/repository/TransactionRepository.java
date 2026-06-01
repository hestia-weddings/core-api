package com.hestia.api.domain.payment.repository;

import com.hestia.api.domain.payment.entity.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Optional<Transaction> findByAsaasTransferId(String asaasTransferId);

    @Query("SELECT COALESCE(SUM(t.amount + t.fee), 0) FROM Transaction t"
            + " WHERE t.wallet.id = :walletId"
            + " AND t.status = com.hestia.api.domain.payment.enums.TransactionStatus.PENDING")
    Integer sumPendingAmountsByWalletId(@Param("walletId") UUID walletId);
}
