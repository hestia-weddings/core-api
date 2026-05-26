package com.hestia.api.security.access;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private static final String INVITE_A = "cccc0000-0000-0000-0000-000000000001";
    private static final String GUEST_A = "dddd0000-0000-0000-0000-000000000001";
    private static final String GIFT_A = "eeee0000-0000-0000-0000-000000000001";

    // ==========================================
    // PUBLIC SLUG ENDPOINTS (/w/{slug}/*)
    // ==========================================

    @Nested
    @DisplayName("POST /w/{slug}/rsvp/invite/search")
    class SearchInvite {

        @Test
        void returnsOkWithValidSlug() throws Exception {
            mockMvc.perform(post("/w/{slug}/rsvp/invite/search", VALID_SLUG)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"Familia Silva\"}"))
                    .andExpect(status().isOk());
        }

        @Test
        void returns404WithInvalidSlug() throws Exception {
            mockMvc.perform(post("/w/{slug}/rsvp/invite/search", INVALID_SLUG)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"Familia Silva\"}"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /w/{slug}/rsvp/guest")
    class ListGuestsByInvite {

        @Test
        void returnsOkWithValidSlug() throws Exception {
            mockMvc.perform(get("/w/{slug}/rsvp/guest", VALID_SLUG).param("invite_id", INVITE_A))
                    .andExpect(status().isOk());
        }

        @Test
        void returns404WithInvalidSlug() throws Exception {
            mockMvc.perform(get("/w/{slug}/rsvp/guest", INVALID_SLUG).param("invite_id", INVITE_A))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("PATCH /w/{slug}/rsvp/guest/status/{id}")
    class UpdateGuestStatus {

        @Test
        void returnsOkWithValidSlug() throws Exception {
            mockMvc.perform(patch("/w/{slug}/rsvp/guest/status/{id}", VALID_SLUG, GUEST_A)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"status\": \"CONFIRMED\"}"))
                    .andExpect(status().isOk());
        }

        @Test
        void returns404WithInvalidSlug() throws Exception {
            mockMvc.perform(patch("/w/{slug}/rsvp/guest/status/{id}", INVALID_SLUG, GUEST_A)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"status\": \"CONFIRMED\"}"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /w/{slug}/gift")
    class ListGifts {

        @Test
        void returnsOkWithValidSlug() throws Exception {
            mockMvc.perform(get("/w/{slug}/gift", VALID_SLUG)).andExpect(status().isOk());
        }

        @Test
        void returns404WithInvalidSlug() throws Exception {
            mockMvc.perform(get("/w/{slug}/gift", INVALID_SLUG)).andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /w/{slug}/gift/{id}")
    class GetGiftById {

        @Test
        void returnsOkWithValidSlug() throws Exception {
            mockMvc.perform(get("/w/{slug}/gift/{id}", VALID_SLUG, GIFT_A)).andExpect(status().isOk());
        }

        @Test
        void returns404WithInvalidSlug() throws Exception {
            mockMvc.perform(get("/w/{slug}/gift/{id}", INVALID_SLUG, GIFT_A)).andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /w/{slug}/message")
    class CreateMessage {

        @Test
        void returnsCreatedWithValidSlug() throws Exception {
            mockMvc.perform(post("/w/{slug}/message", VALID_SLUG)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"sender\": \"Guest\", \"message\": \"Congrats!\"}"))
                    .andExpect(status().isCreated());
        }

        @Test
        void returns404WithInvalidSlug() throws Exception {
            mockMvc.perform(post("/w/{slug}/message", INVALID_SLUG)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"sender\": \"Guest\", \"message\": \"Congrats!\"}"))
                    .andExpect(status().isNotFound());
        }
    }

    // ==========================================
    // PROTECTED ENDPOINTS (should return 401)
    // ==========================================

    @Nested
    @DisplayName("Protected endpoints (should return 401)")
    class ProtectedEndpoints {

        @Test
        void cannotListInvites() throws Exception {
            mockMvc.perform(get("/rsvp/invite")).andExpect(status().isUnauthorized());
        }

        @Test
        void cannotCreateInvite() throws Exception {
            mockMvc.perform(post("/rsvp/invite")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"X\"}"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotPatchInvite() throws Exception {
            mockMvc.perform(patch("/rsvp/invite/{id}", INVITE_A)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"X\"}"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotDeleteInvite() throws Exception {
            mockMvc.perform(delete("/rsvp/invite/{id}", INVITE_A)).andExpect(status().isUnauthorized());
        }

        @Test
        void cannotListGuests() throws Exception {
            mockMvc.perform(get("/rsvp/guest")).andExpect(status().isUnauthorized());
        }

        @Test
        void cannotCreateGuest() throws Exception {
            mockMvc.perform(post("/rsvp/guest")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"name\": \"X\", \"age_group\": \"ADULT\", \"invite_id\": \"" + INVITE_A + "\"}"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotPatchGuest() throws Exception {
            mockMvc.perform(patch("/rsvp/guest/{id}", GUEST_A)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"X\"}"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotDeleteGuest() throws Exception {
            mockMvc.perform(delete("/rsvp/guest/{id}", GUEST_A)).andExpect(status().isUnauthorized());
        }

        @Test
        void cannotListGifts() throws Exception {
            mockMvc.perform(get("/gift")).andExpect(status().isUnauthorized());
        }

        @Test
        void cannotCreateGift() throws Exception {
            mockMvc.perform(post("/gift")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"description\": \"X\", \"price\": 1000, \"stock\": 1}"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotGetGiftById() throws Exception {
            mockMvc.perform(get("/gift/{id}", GIFT_A)).andExpect(status().isUnauthorized());
        }

        @Test
        void cannotPatchGift() throws Exception {
            mockMvc.perform(patch("/gift/{id}", GIFT_A)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"description\": \"X\"}"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotDeleteGift() throws Exception {
            mockMvc.perform(delete("/gift/{id}", GIFT_A)).andExpect(status().isUnauthorized());
        }

        @Test
        void cannotListMessages() throws Exception {
            mockMvc.perform(get("/message")).andExpect(status().isUnauthorized());
        }

        @Test
        void cannotPatchMessage() throws Exception {
            mockMvc.perform(patch("/message/{id}", "ffff0000-0000-0000-0000-000000000001")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"is_favorite\": true}"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotReadMessage() throws Exception {
            mockMvc.perform(patch("/message/{id}/read", "ffff0000-0000-0000-0000-000000000001"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotDeleteMessage() throws Exception {
            mockMvc.perform(delete("/message/{id}", "ffff0000-0000-0000-0000-000000000001"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotListWeddings() throws Exception {
            mockMvc.perform(get("/wedding")).andExpect(status().isUnauthorized());
        }

        @Test
        void cannotCreateWedding() throws Exception {
            mockMvc.perform(post("/wedding")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"couple_name\": \"X\", \"slug\": \"x\"}"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotGetWeddingById() throws Exception {
            mockMvc.perform(get("/wedding/{id}", "11111111-1111-1111-1111-111111111111"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotPatchWedding() throws Exception {
            mockMvc.perform(patch("/wedding/{id}", "11111111-1111-1111-1111-111111111111")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"couple_name\": \"X\"}"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotDeleteWedding() throws Exception {
            mockMvc.perform(delete("/wedding/{id}", "11111111-1111-1111-1111-111111111111"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotListAccounts() throws Exception {
            mockMvc.perform(get("/account")).andExpect(status().isUnauthorized());
        }

        @Test
        void cannotCreateAccount() throws Exception {
            mockMvc.perform(
                            post("/account")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            "{\"name\": \"X\", \"email\": \"x@x.com\", \"wedding_id\": \"11111111-1111-1111-1111-111111111111\"}"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotPatchAccount() throws Exception {
            mockMvc.perform(patch("/account/{id}", "bbbb0000-0000-0000-0000-000000000001")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"X\"}"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotDeleteAccount() throws Exception {
            mockMvc.perform(delete("/account/{id}", "bbbb0000-0000-0000-0000-000000000001"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void cannotListPaymentConfigs() throws Exception {
            mockMvc.perform(get("/payment-config")).andExpect(status().isUnauthorized());
        }

        @Test
        void cannotCreatePaymentConfig() throws Exception {
            mockMvc.perform(post("/payment-config")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"api_key\": \"$aact_hmlg_x\", \"environment\": \"SANDBOX\", \"webhook_token\": \"wh_x\"}"))
                    .andExpect(status().isUnauthorized());
        }
    }
}
