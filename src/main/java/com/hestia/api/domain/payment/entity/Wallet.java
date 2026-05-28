package com.hestia.api.domain.payment.entity;

import com.hestia.api.common.model.BaseTenantModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Wallet extends BaseTenantModel {

    @Column(name = "pix_key", nullable = false)
    private String pixKey;

    @Column(nullable = false)
    private Integer balance;

    @Column(nullable = false)
    private Integer fee;
}
