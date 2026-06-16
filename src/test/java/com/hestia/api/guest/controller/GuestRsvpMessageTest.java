package com.hestia.api.guest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
@DisplayName("Guest RSVP Message")
class GuestRsvpMessageTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String SLUG = "alice-bob";
    private static final String INVITE_A = "cccc0000-0000-0000-0000-000000000003";

    @Test
    void createsRsvpMessage() throws Exception {
        mockMvc.perform(post("/w/{slug}/rsvp/message", SLUG)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"invite_id\": \""
                                + INVITE_A
                                + "\", \"sender\": \"Carlos\", \"message\": \"Estaremos lá!\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void overwritesExistingRsvpMessage() throws Exception {
        mockMvc.perform(post("/w/{slug}/rsvp/message", SLUG)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"invite_id\": \""
                                + INVITE_A
                                + "\", \"sender\": \"Carlos\", \"message\": \"First message\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/w/{slug}/rsvp/message", SLUG)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"invite_id\": \""
                                + INVITE_A
                                + "\", \"sender\": \"Carlos\", \"message\": \"Updated message\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void defaultsSenderToInviteName() throws Exception {
        mockMvc.perform(post("/w/{slug}/rsvp/message", SLUG)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"invite_id\": \"" + INVITE_A + "\", \"message\": \"Congrats!\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void returns404ForInvalidInvite() throws Exception {
        mockMvc.perform(post("/w/{slug}/rsvp/message", SLUG)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"invite_id\": \"00000000-0000-0000-0000-000000000099\", \"message\": \"Hello\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void returns400WhenMessageBlank() throws Exception {
        mockMvc.perform(post("/w/{slug}/rsvp/message", SLUG)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"invite_id\": \"" + INVITE_A + "\", \"message\": \"\"}"))
                .andExpect(status().isBadRequest());
    }
}
