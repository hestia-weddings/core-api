package com.hestia.api.domain.household.controller;

import com.hestia.api.domain.household.dto.CreateHouseholdRequest;
import com.hestia.api.domain.household.dto.HouseholdResponse;
import com.hestia.api.domain.household.dto.UpdateHouseholdRequest;
import com.hestia.api.domain.household.entity.Household;
import com.hestia.api.domain.household.service.HouseholdService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/household")
@Tag(name = "Households", description = "CRUD operations for households")
@RequiredArgsConstructor
public class HouseholdController {

    @Autowired
    private HouseholdService householdService;

    @GetMapping
    public List<HouseholdResponse> getHousehold() {
        return householdService.getHouseholds();
    }

    @PostMapping
    public HouseholdResponse postHousehold(@RequestBody CreateHouseholdRequest request) {
        return householdService.createHousehold(request);
    }

    @PatchMapping("/{id}")
    public HouseholdResponse patchHousehold(
            @PathVariable UUID id,
            @RequestBody UpdateHouseholdRequest request
    ) {
        return householdService.updateHousehold(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteHousehold(@PathVariable UUID id) { householdService.deleteHousehold(id); }
}
