package com.hestia.api.domain.rsvp.controller;

import com.hestia.api.domain.rsvp.dto.CreateHouseholdRequest;
import com.hestia.api.domain.rsvp.dto.HouseholdResponse;
import com.hestia.api.domain.rsvp.dto.UpdateHouseholdRequest;
import com.hestia.api.domain.rsvp.service.HouseholdService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/household")
@Tag(name = "Households", description = "CRUD operations for households")
@RequiredArgsConstructor
public class HouseholdController {

    @Autowired
    private HouseholdService householdService;

    @GetMapping
    public Page<HouseholdResponse> getHousehold(Pageable pageable) {
        return householdService.getHouseholds(pageable);
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
