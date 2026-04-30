package com.hestia.api.domain.rsvp.controller;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.domain.rsvp.dto.CreateHouseholdRequest;
import com.hestia.api.domain.rsvp.dto.HouseholdResponse;
import com.hestia.api.domain.rsvp.dto.UpdateHouseholdRequest;
import com.hestia.api.domain.rsvp.service.HouseholdService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<PageResponse<HouseholdResponse>> getHousehold(Pageable pageable) {
        return ResponseEntity.ok(householdService.getHouseholds(pageable));
    }

    @PostMapping
    public ResponseEntity<HouseholdResponse> postHousehold(@RequestBody CreateHouseholdRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(householdService.createHousehold(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HouseholdResponse> patchHousehold(
            @PathVariable UUID id,
            @RequestBody UpdateHouseholdRequest request
    ) {
        return ResponseEntity.ok(householdService.updateHousehold(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHousehold(@PathVariable UUID id) {
        householdService.deleteHousehold(id);
        return ResponseEntity.noContent().build();
    }
}
