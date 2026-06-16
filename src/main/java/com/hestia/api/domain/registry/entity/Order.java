package com.hestia.api.domain.registry.entity;

import com.hestia.api.common.model.BaseTenantModel;
import com.hestia.api.domain.message.entity.Message;
import com.hestia.api.domain.registry.enums.OrderStatus;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, columnDefinition = "order_status_enum")
    private OrderStatus status;

    @Column(name = "payment_id", nullable = false)
    private UUID paymentId;

    @ManyToOne
    @JoinColumn(name = "gift_id")
    private Gift gift;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;
}
