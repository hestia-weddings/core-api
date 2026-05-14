package com.hestia.api.domain.registry.entity;

import com.hestia.api.common.model.BaseModel;
import com.hestia.api.domain.wedding.entity.Wedding;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "gifts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gift extends BaseModel {

    @Column(nullable = false)
    private String description;

    @Column
    private String picture;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "wedding_id")
    private Wedding wedding;
}
