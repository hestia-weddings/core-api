package com.hestia.api.domain.payment.entity;

import com.hestia.api.common.model.BaseTenantModel;
import com.hestia.api.domain.payment.enums.TransactionStatus;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Transaction extends BaseTenantModel {

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private Integer fee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(name = "asaas_transfer_id")
    private String asaasTransferId;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;
}
