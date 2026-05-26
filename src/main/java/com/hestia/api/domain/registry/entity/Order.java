package com.hestia.api.domain.registry.entity;

import com.hestia.api.common.model.BaseModel;
import com.hestia.api.domain.registry.enums.OrderStatus;
import com.hestia.api.domain.wedding.entity.Wedding;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseModel {

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

    @ManyToOne
    @JoinColumn(name = "wedding_id")
    private Wedding wedding;
}
