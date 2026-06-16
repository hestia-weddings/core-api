package com.hestia.api.security.access;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hestia.api.security.support.WithMockCouple;

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
@WithMockCouple
@DisplayName("Couple Access")
class CoupleAccessTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String OWN_WEDDING = "11111111-1111-1111-1111-111111111111";
    private static final String OTHER_WEDDING = "22222222-2222-2222-2222-222222222222";
    private static final String OWN_INVITE = "cccc0000-0000-0000-0000-000000000001";
    private static final String OTHER_INVITE = "cccc0000-0000-0000-0000-000000000002";
    private static final String OWN_GUEST = "dddd0000-0000-0000-0000-000000000001";
    private static final String OTHER_GUEST = "dddd0000-0000-0000-0000-000000000002";
    private static final String OWN_GIFT = "eeee0000-0000-0000-0000-000000000001";
    private static final String OTHER_GIFT = "eeee0000-0000-0000-0000-000000000002";
    private static final String OWN_MESSAGE = "ffff0000-0000-0000-0000-000000000001";
    private static final String OTHER_MESSAGE = "ffff0000-0000-0000-0000-000000000002";
    private static final String OWN_USER = "bbbb0000-0000-0000-0000-000000000001";
    private static final String ADMIN_USER = "aaaa0000-0000-0000-0000-000000000001";
    private static final String OWN_WALLET = "11111111-1111-1111-1111-111111111111";
    private static final String OTHER_WALLET = "99999999-9999-9999-9999-999999999999";
    private static final String OWN_ORDER = "bbbb1111-0000-0000-0000-000000000001";

    // ==========================================
    // INVITE ENDPOINTS - Own wedding
    // ==========================================

    @Nested
    @DisplayName("Invites (own wedding)")
    class InviteOwn {

        @Test
        void canListInvites() throws Exception {
            mockMvc.perform(get("/rsvp/invite")).andExpect(status().isOk());
        }

        @Test
        void canGetInviteById() throws Exception {
            mockMvc.perform(get("/rsvp/invite/{id}", OWN_INVITE)).andExpect(status().isOk());
        }

        @Test
        void cannotGetOtherWeddingInvite() throws Exception {
            mockMvc.perform(get("/rsvp/invite/{id}", OTHER_INVITE)).andExpect(status().isNotFound());
        }

        @Test
        void canCreateInvite() throws Exception {
            mockMvc.perform(post("/rsvp/invite")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"New Family\"}"))
                    .andExpect(status().isCreated());
        }

        @Test
        void canPatchInvite() throws Exception {
            mockMvc.perform(patch("/rsvp/invite/{id}", OWN_INVITE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"Updated\"}"))
                    .andExpect(status().isOk());
        }

        @Test
        void canDeleteInvite() throws Exception {
            var response = mockMvc.perform(post("/rsvp/invite")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"Deletable Invite\"}"))
                    .andExpect(status().isCreated())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            String id = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readTree(response)
                    .get("id")
                    .asText();
            mockMvc.perform(delete("/rsvp/invite/{id}", id)).andExpect(status().isNoContent());
        }

        @Test
        void cannotPatchOtherWeddingInvite() throws Exception {
            mockMvc.perform(patch("/rsvp/invite/{id}", OTHER_INVITE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"Hacked\"}"))
                    .andExpect(status().isNotFound());
        }

        @Test
        void cannotDeleteOtherWeddingInvite() throws Exception {
            mockMvc.perform(delete("/rsvp/invite/{id}", OTHER_INVITE)).andExpect(status().isNotFound());
        }
    }

    // ==========================================
    // GUEST ENDPOINTS - Own wedding
    // ==========================================

    @Nested
    @DisplayName("Guests (own wedding)")
    class GuestOwn {

        @Test
        void canListGuests() throws Exception {
            mockMvc.perform(get("/rsvp/guest")).andExpect(status().isOk());
        }

        @Test
        void canGetGuestById() throws Exception {
            mockMvc.perform(get("/rsvp/guest/{id}", OWN_GUEST)).andExpect(status().isOk());
        }

        @Test
        void cannotGetOtherWeddingGuest() throws Exception {
            mockMvc.perform(get("/rsvp/guest/{id}", OTHER_GUEST)).andExpect(status().isNotFound());
        }

        @Test
        void canCreateGuest() throws Exception {
            mockMvc.perform(post("/rsvp/guest")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"New Guest\", \"age_group\": \"ADULT\", \"invite_id\": \""
                                    + OWN_INVITE + "\"}"))
                    .andExpect(status().isCreated());
        }

        @Test
        void canPatchGuest() throws Exception {
            mockMvc.perform(patch("/rsvp/guest/{id}", OWN_GUEST)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"Updated\"}"))
                    .andExpect(status().isOk());
        }

        @Test
        void canDeleteGuest() throws Exception {
            var response = mockMvc.perform(post("/rsvp/guest")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"Deletable Guest\", \"age_group\": \"ADULT\", \"invite_id\": \""
                                    + OWN_INVITE + "\"}"))
                    .andExpect(status().isCreated())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            String id = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readTree(response)
                    .get("id")
                    .asText();
            mockMvc.perform(delete("/rsvp/guest/{id}", id)).andExpect(status().isNoContent());
        }

        @Test
        void cannotPatchOtherWeddingGuest() throws Exception {
            mockMvc.perform(patch("/rsvp/guest/{id}", OTHER_GUEST)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"Hacked\"}"))
                    .andExpect(status().isNotFound());
        }

        @Test
        void cannotDeleteOtherWeddingGuest() throws Exception {
            mockMvc.perform(delete("/rsvp/guest/{id}", OTHER_GUEST)).andExpect(status().isNotFound());
        }
    }

    // ==========================================
    // GIFT ENDPOINTS - Own wedding
    // ==========================================

    @Nested
    @DisplayName("Gifts (own wedding)")
    class GiftOwn {

        @Test
        void canListGifts() throws Exception {
            mockMvc.perform(get("/gift")).andExpect(status().isOk());
        }

        @Test
        void canCreateGift() throws Exception {
            mockMvc.perform(post("/gift")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"description\": \"Toaster\", \"price\": 5000, \"stock\": 1}"))
                    .andExpect(status().isCreated());
        }

        @Test
        void canGetGiftById() throws Exception {
            mockMvc.perform(get("/gift/{id}", OWN_GIFT)).andExpect(status().isOk());
        }

        @Test
        void canPatchGift() throws Exception {
            mockMvc.perform(patch("/gift/{id}", OWN_GIFT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"description\": \"Updated\"}"))
                    .andExpect(status().isOk());
        }

        @Test
        void canDeleteGift() throws Exception {
            mockMvc.perform(delete("/gift/{id}", OWN_GIFT)).andExpect(status().isNoContent());
        }

        @Test
        void cannotGetOtherWeddingGift() throws Exception {
            mockMvc.perform(get("/gift/{id}", OTHER_GIFT)).andExpect(status().isNotFound());
        }

        @Test
        void cannotPatchOtherWeddingGift() throws Exception {
            mockMvc.perform(patch("/gift/{id}", OTHER_GIFT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"description\": \"Hacked\"}"))
                    .andExpect(status().isNotFound());
        }

        @Test
        void cannotDeleteOtherWeddingGift() throws Exception {
            mockMvc.perform(delete("/gift/{id}", OTHER_GIFT)).andExpect(status().isNotFound());
        }
    }

    // ==========================================
    // MESSAGE ENDPOINTS - Own wedding
    // ==========================================

    @Nested
    @DisplayName("Messages (own wedding)")
    class MessageOwn {

        @Test
        void canListMessages() throws Exception {
            mockMvc.perform(get("/message")).andExpect(status().isOk());
        }

        @Test
        void canGetMessageById() throws Exception {
            mockMvc.perform(get("/message/{id}", OWN_MESSAGE)).andExpect(status().isOk());
        }

        @Test
        void cannotGetOtherWeddingMessage() throws Exception {
            mockMvc.perform(get("/message/{id}", OTHER_MESSAGE)).andExpect(status().isNotFound());
        }

        @Test
        void canPatchMessage() throws Exception {
            mockMvc.perform(patch("/message/{id}", OWN_MESSAGE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"is_favorite\": true}"))
                    .andExpect(status().isOk());
        }

        @Test
        void canReadMessage() throws Exception {
            mockMvc.perform(patch("/message/{id}/read", OWN_MESSAGE)).andExpect(status().isOk());
        }

        @Test
        void canDeleteMessage() throws Exception {
            mockMvc.perform(delete("/message/{id}", OWN_MESSAGE)).andExpect(status().isNoContent());
        }

        @Test
        void cannotPatchOtherWeddingMessage() throws Exception {
            mockMvc.perform(patch("/message/{id}", OTHER_MESSAGE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"is_favorite\": true}"))
                    .andExpect(status().isNotFound());
        }

        @Test
        void cannotDeleteOtherWeddingMessage() throws Exception {
            mockMvc.perform(delete("/message/{id}", OTHER_MESSAGE)).andExpect(status().isNotFound());
        }
    }

    // ==========================================
    // WEDDING ENDPOINTS - Tenant isolation
    // ==========================================

    @Nested
    @DisplayName("Wedding (tenant isolation)")
    class WeddingTenant {

        @Test
        void canGetOwnWedding() throws Exception {
            mockMvc.perform(get("/wedding")).andExpect(status().isOk());
        }

        @Test
        void cannotGetWeddingById() throws Exception {
            mockMvc.perform(get("/wedding/{id}", OWN_WEDDING)).andExpect(status().isForbidden());
        }

        @Test
        void canPatchOwnWedding() throws Exception {
            mockMvc.perform(patch("/wedding")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"couple_name\": \"Alice & Bobby\"}"))
                    .andExpect(status().isOk());
        }

        @Test
        void cannotGetOtherWeddingById() throws Exception {
            mockMvc.perform(get("/wedding/{id}", OTHER_WEDDING)).andExpect(status().isForbidden());
        }
    }

    // ==========================================
    // ACCOUNT ENDPOINTS - Self only
    // ==========================================

    @Nested
    @DisplayName("Account (self access)")
    class AccountSelf {

        @Test
        void canGetOwnAccount() throws Exception {
            mockMvc.perform(get("/account")).andExpect(status().isOk());
        }

        @Test
        void canPatchOwnAccount() throws Exception {
            mockMvc.perform(patch("/account/{id}", OWN_USER)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"Updated Name\"}"))
                    .andExpect(status().isOk());
        }

        @Test
        void cannotPatchOtherAccount() throws Exception {
            mockMvc.perform(patch("/account/{id}", ADMIN_USER)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"Hacked\"}"))
                    .andExpect(status().isForbidden());
        }
    }

    // ==========================================
    // ADMIN-ONLY ENDPOINTS (should return 403)
    // ==========================================

    @Nested
    @DisplayName("Admin-only endpoints (should return 403)")
    class AdminOnly {

        @Test
        void cannotCreateWedding() throws Exception {
            mockMvc.perform(post("/wedding")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"couple_name\": \"X & Y\", \"slug\": \"x-y\"}"))
                    .andExpect(status().isForbidden());
        }

        @Test
        void cannotDeleteWedding() throws Exception {
            mockMvc.perform(delete("/wedding/{id}", OWN_WEDDING)).andExpect(status().isForbidden());
        }

        @Test
        void cannotCreateAccount() throws Exception {
            mockMvc.perform(post("/account")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"X\", \"email\": \"x@x.com\", \"wedding_id\": \"" + OWN_WEDDING
                                    + "\"}"))
                    .andExpect(status().isForbidden());
        }

        @Test
        void cannotDeleteAccount() throws Exception {
            mockMvc.perform(delete("/account/{id}", ADMIN_USER)).andExpect(status().isForbidden());
        }
    }

    // ==========================================
    // WALLET ENDPOINTS (own wedding)
    // ==========================================

    @Nested
    @DisplayName("Wallets (own wedding)")
    class WalletOwn {

        @Test
        void canListWallets() throws Exception {
            mockMvc.perform(get("/wallet")).andExpect(status().isOk());
        }

        @Test
        void canGetWalletById() throws Exception {
            mockMvc.perform(get("/wallet/{id}", OWN_WALLET)).andExpect(status().isOk());
        }

        @Test
        void cannotGetOtherWeddingWallet() throws Exception {
            mockMvc.perform(get("/wallet/{id}", OTHER_WALLET)).andExpect(status().isNotFound());
        }

        @Test
        void canPatchWallet() throws Exception {
            mockMvc.perform(patch("/wallet/{id}", OWN_WALLET)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"pix_key\": \"updated@pix.com\"}"))
                    .andExpect(status().isOk());
        }

        @Test
        void cannotPatchOtherWeddingWallet() throws Exception {
            mockMvc.perform(patch("/wallet/{id}", OTHER_WALLET)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"pix_key\": \"x@pix.com\"}"))
                    .andExpect(status().isNotFound());
        }
    }

    // ==========================================
    // ORDER ENDPOINTS (own wedding, read-only)
    // ==========================================

    @Nested
    @DisplayName("Orders (own wedding)")
    class OrderOwn {

        @Test
        void canListOrders() throws Exception {
            mockMvc.perform(get("/order")).andExpect(status().isOk());
        }

        @Test
        void canGetOrderById() throws Exception {
            mockMvc.perform(get("/order/{id}", OWN_ORDER)).andExpect(status().isOk());
        }

        @Test
        void cannotGetOtherWeddingOrder() throws Exception {
            mockMvc.perform(get("/order/{id}", "bbbb1111-0000-0000-0000-000000000099"))
                    .andExpect(status().isNotFound());
        }
    }
}
