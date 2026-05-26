package com.hestia.api.common.model;

import com.hestia.api.domain.wedding.entity.Wedding;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public class BaseTenantModel extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "wedding_id")
    private Wedding wedding;
}
