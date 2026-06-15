package com.hestia.api.domain.rsvp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hestia.api.security.support.WithMockCouple;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockCouple
@DisplayName("Invite status filter")
class InviteStatusFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /rsvp/invite?status=CONFIRMED returns only confirmed guests in response")
    void filterByConfirmed() throws Exception {
        mockMvc.perform(get("/rsvp/invite").param("status", "CONFIRMED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.total_elements").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Familia Silva"))
                .andExpect(jsonPath("$.data[0].guests.length()").value(1))
                .andExpect(jsonPath("$.data[0].guests[0].name").value("João Silva"));
    }

    @Test
    @DisplayName("GET /rsvp/invite?status=PENDING returns only pending guests in response")
    void filterByPending() throws Exception {
        mockMvc.perform(get("/rsvp/invite").param("status", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.total_elements").value(2))
                .andExpect(jsonPath("$.data[?(@.name=='Familia Silva')].guests[0].name")
                        .value("Ana Silva"))
                .andExpect(jsonPath("$.data[?(@.name=='Familia Oliveira')].guests[0].name")
                        .value("Carlos Oliveira"));
    }

    @Test
    @DisplayName("GET /rsvp/invite without filter returns all invites with all guests")
    void noFilter() throws Exception {
        mockMvc.perform(get("/rsvp/invite"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.total_elements").value(2))
                .andExpect(jsonPath("$.data[?(@.name=='Familia Silva')].guests.length()")
                        .value(2));
    }
}
