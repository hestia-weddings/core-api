package com.hestia.api.domain.registry;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hestia.api.security.support.WithMockCouple;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockCouple
@DisplayName("Gift Update Validation")
class GiftUpdateValidationTest {

    @Autowired
    private MockMvc mockMvc;

    // Gift A: stock=3, 2 PAID orders → soldCount=2
    private static final String GIFT_A = "eeee0000-0000-0000-0000-000000000001";

    @Test
    @DisplayName("PATCH stock below sold count → 422")
    void rejectsStockBelowSoldCount() throws Exception {
        mockMvc.perform(patch("/gift/{id}", GIFT_A)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stock\": 1}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Cannot reduce stock below 2 already-sold units"));
    }

    @Test
    @DisplayName("PATCH stock equal to sold count → 200")
    void acceptsStockEqualToSoldCount() throws Exception {
        mockMvc.perform(patch("/gift/{id}", GIFT_A)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stock\": 2}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH stock above sold count → 200")
    void acceptsStockAboveSoldCount() throws Exception {
        mockMvc.perform(patch("/gift/{id}", GIFT_A)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stock\": 5}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH with only stock (no description) → 200")
    void acceptsPartialUpdateWithoutDescription() throws Exception {
        mockMvc.perform(patch("/gift/{id}", GIFT_A)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stock\": 3}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH with positive price → 200")
    void acceptsPositivePrice() throws Exception {
        mockMvc.perform(patch("/gift/{id}", GIFT_A)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"price\": 100}"))
                .andExpect(status().isOk());
    }
}
