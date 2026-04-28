package com.hestia.api.domain.household.controller;

import com.hestia.api.domain.household.dto.HouseholdResponse;
import com.hestia.api.domain.household.entity.Household;
import com.hestia.api.domain.household.service.HouseholdService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
