package com.hestia.api.guest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
@DisplayName("Guest Messages")
class GuestMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String VALID_SLUG = "alice-bob";
    private static final String INVALID_SLUG = "nonexistent";

    @Nested
    @DisplayName("GET /w/{slug}/message/invite")
    class GetInviteMessage {

        @Test
        void returnsInviteMessage() throws Exception {
            mockMvc.perform(get("/w/{slug}/message/invite", VALID_SLUG))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.invite_message").value("You are invited!"));
        }

        @Test
        void returns404ForInvalidSlug() throws Exception {
            mockMvc.perform(get("/w/{slug}/message/invite", INVALID_SLUG)).andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /w/{slug}/message/gift")
    class GetGiftMessage {

        @Test
        void returnsGiftMessage() throws Exception {
            mockMvc.perform(get("/w/{slug}/message/gift", VALID_SLUG))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.gift_message").value("Check our gifts"));
        }

        @Test
        void returns404ForInvalidSlug() throws Exception {
            mockMvc.perform(get("/w/{slug}/message/gift", INVALID_SLUG)).andExpect(status().isNotFound());
        }
    }
}
