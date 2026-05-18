package com.hestia.api.security.access;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Guest (Anonymous) Access")
class GuestAccessTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String VALID_SLUG = "alice-bob";
    private static final String INVALID_SLUG = "nonexistent";

    @Nested
    @DisplayName("Public slug endpoints (/w/{slug})")
    class PublicEndpoints {

        @Test
        void canSearchInviteBySlug() throws Exception {
            mockMvc.perform(post("/w/{slug}/rsvp/invite/search", VALID_SLUG)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"Familia Silva\"}"))
                    .andExpect(status().isOk());
        }

        @Test
        void canListGuestsByInvite() throws Exception {
            mockMvc.perform(get("/w/{slug}/rsvp/guest", VALID_SLUG)
                            .param("invite_id", "cccc0000-0000-0000-0000-000000000001"))
                    .andExpect(status().isOk());
        }

        @Test
        void canUpdateGuestStatus() throws Exception {
            mockMvc.perform(patch("/w/{slug}/rsvp/guest/status/{id}", VALID_SLUG, "dddd0000-0000-0000-0000-000000000001")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"status\": \"CONFIRMED\"}"))
                    .andExpect(status().isOk());
        }

        @Test
        void canListGifts() throws Exception {
            mockMvc.perform(get("/w/{slug}/gift", VALID_SLUG))
                    .andExpect(status().isOk());
        }

        @Test
        void canGetGiftById() throws Exception {
            mockMvc.perform(get("/w/{slug}/gift/{id}", VALID_SLUG, "eeee0000-0000-0000-0000-000000000001"))
                    .andExpect(status().isOk());
        }

        @Test
        void canCreateMessage() throws Exception {
            mockMvc.perform(post("/w/{slug}/message", VALID_SLUG)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"sender\": \"Guest\", \"message\": \"Congrats!\"}"))
                    .andExpect(status().isCreated());
        }

        @Test
        void invalidSlugReturns404() throws Exception {
            mockMvc.perform(get("/w/{slug}/gift", INVALID_SLUG))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Protected endpoints (should return 401)")
    class ProtectedEndpoints {

        @Test
        void cannotAccessInvites() throws Exception {
            mockMvc.perform(get("/rsvp/invite"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotAccessGuests() throws Exception {
            mockMvc.perform(get("/rsvp/guest"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotAccessGifts() throws Exception {
            mockMvc.perform(get("/gift"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotAccessMessages() throws Exception {
            mockMvc.perform(get("/message"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotAccessAccounts() throws Exception {
            mockMvc.perform(get("/account"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotAccessWeddings() throws Exception {
            mockMvc.perform(get("/wedding"))
                    .andExpect(status().isUnauthorized());
        }
    }
}
