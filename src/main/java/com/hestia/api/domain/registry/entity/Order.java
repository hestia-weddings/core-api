package com.hestia.api.domain.registry.entity;

import com.hestia.api.common.model.BaseTenantModel;
import com.hestia.api.domain.registry.enums.OrderStatus;

import jakarta.persistence.*;

import java.util.UUID;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Order extends BaseTenantModel {

    @Column(name = "guest_name", nullable = false)
    private String guestName;

    @Column(name = "guest_email", nullable = false)
    private String guestEmail;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private OrderStatus status;

    @Column(name = "payment_id", nullable = false)
    private UUID paymentId;

    @ManyToOne
    @JoinColumn(name = "gift_id")
    private Gift gift;
}
