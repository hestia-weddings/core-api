package com.hestia.api.domain.household.entity;

import com.hestia.api.common.model.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "households")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Household extends BaseModel {

    @OneToMany(mappedBy = "household")
    private List<Invite> invites;
}
