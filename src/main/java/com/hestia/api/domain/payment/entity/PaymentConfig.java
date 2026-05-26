package com.hestia.api.domain.payment.entity;

import com.hestia.api.common.model.BaseModel;
import com.hestia.api.domain.payment.enums.PaymentEnvironment;
import com.hestia.api.domain.wedding.entity.Wedding;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import lombok.*;

@Entity
@Table(name = "payment_configs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentConfig extends BaseModel {

    @Column(name = "api_key", unique = true, nullable = false)
    private String apiKey;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(columnDefinition = "payment_env_enum", nullable = false)
    private PaymentEnvironment environment;

    @Column(name = "webhook_token", length = 64, nullable = false)
    private String webhookToken;

    @OneToOne
    @JoinColumn(name = "wedding_id")
    private Wedding wedding;
}
