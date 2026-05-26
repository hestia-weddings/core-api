package com.hestia.api.domain.registry.entity;

import com.hestia.api.common.model.BaseTenantModel;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "gifts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Gift extends BaseTenantModel {

    @Column(nullable = false)
    private String description;

    @Column
    private String picture;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer stock;
}
