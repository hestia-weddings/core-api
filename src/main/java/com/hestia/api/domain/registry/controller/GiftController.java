package com.hestia.api.domain.registry.controller;

import com.hestia.api.domain.registry.dto.GiftAvailabilityResponse;
import com.hestia.api.domain.registry.service.GiftService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gift")
@Tag(name = "Gifts", description = "CRUD operations for gifts")
@RequiredArgsConstructor
public class GiftController {

    @Autowired
    private GiftService giftService;

    @GetMapping
    public List<GiftAvailabilityResponse> getGift() {
        return giftService.getGifts();
    }
}
