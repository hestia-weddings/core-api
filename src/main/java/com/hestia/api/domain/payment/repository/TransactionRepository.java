package com.hestia.api.domain.payment.repository;

import com.hestia.api.domain.payment.entity.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Optional<Transaction> findByAsaasTransferId(String asaasTransferId);

    @Query(
            value = "SELECT COALESCE(SUM(t.amount + t.fee), 0) FROM transactions t"
                    + " WHERE t.wallet_id = :walletId"
                    + " AND t.status = CAST(:status AS transaction_status_enum)",
            nativeQuery = true)
    Integer sumPendingAmountsByWalletId(@Param("walletId") UUID walletId, @Param("status") String status);
}
