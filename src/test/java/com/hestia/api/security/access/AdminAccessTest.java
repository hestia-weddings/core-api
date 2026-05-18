package com.hestia.api.security.access;

import com.hestia.api.security.support.WithMockAdmin;
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
@WithMockAdmin
@DisplayName("Admin Access")
class AdminAccessTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String WEDDING_A = "11111111-1111-1111-1111-111111111111";
    private static final String WEDDING_B = "22222222-2222-2222-2222-222222222222";
    private static final String INVITE_A = "cccc0000-0000-0000-0000-000000000001";
    private static final String GUEST_A = "dddd0000-0000-0000-0000-000000000001";
    private static final String GIFT_A = "eeee0000-0000-0000-0000-000000000001";
    private static final String GIFT_B = "eeee0000-0000-0000-0000-000000000002";
    private static final String MESSAGE_A = "ffff0000-0000-0000-0000-000000000001";
    private static final String USER_COUPLE = "bbbb0000-0000-0000-0000-000000000001";

    // ==========================================
    // WEDDING ENDPOINTS (admin-only)
    // ==========================================

    @Nested
    @DisplayName("Weddings (admin-only CRUD)")
    class WeddingAdmin {

        @Test
        void canListAllWeddings() throws Exception {
            mockMvc.perform(get("/wedding")).andExpect(status().isOk());
        }

        @Test
        void canCreateWedding() throws Exception {
            mockMvc.perform(post("/wedding").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"couple_name\": \"Eve & Frank\", \"slug\": \"eve-frank\"}"))
                    .andExpect(status().isCreated());
        }

        @Test
        void canGetAnyWeddingById() throws Exception {
            mockMvc.perform(get("/wedding/{id}", WEDDING_B)).andExpect(status().isOk());
        }

        @Test
        void canPatchAnyWedding() throws Exception {
            mockMvc.perform(patch("/wedding/{id}", WEDDING_B).contentType(MediaType.APPLICATION_JSON)
                    .content("{\"couple_name\": \"Carol & Danny\"}")).andExpect(status().isOk());
        }

        @Test
        void canDeleteWedding() throws Exception {
            mockMvc.perform(delete("/wedding/{id}", WEDDING_A)).andExpect(status().isNoContent());
        }
    }

    // ==========================================
    // ACCOUNT ENDPOINTS (admin-only)
    // ==========================================

    @Nested
    @DisplayName("Accounts (admin-only CRUD)")
    class AccountAdmin {

        @Test
        void canListAccounts() throws Exception {
            mockMvc.perform(get("/account")).andExpect(status().isOk());
        }

        @Test
        void canCreateAccount() throws Exception {
            mockMvc.perform(post("/account").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\": \"New User\", \"email\": \"new@hestia.com\", \"wedding_id\": \"" + WEDDING_B + "\"}"))
                    .andExpect(status().isCreated());
        }

        @Test
        void canPatchAnyAccount() throws Exception {
            mockMvc.perform(patch("/account/{id}", USER_COUPLE).contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\": \"Updated\"}")).andExpect(status().isOk());
        }

        @Test
        void canDeleteAccount() throws Exception {
            mockMvc.perform(delete("/account/{id}", USER_COUPLE)).andExpect(status().isNoContent());
        }
    }

    // ==========================================
    // INVITE ENDPOINTS (cross-tenant)
    // ==========================================

    @Nested
    @DisplayName("Invites (cross-tenant via weddingId)")
    class InviteAdmin {

        @Test
        void canListInvites() throws Exception {
            mockMvc.perform(get("/rsvp/invite")).andExpect(status().isOk());
        }

        @Test
        void canCreateInvite() throws Exception {
            mockMvc.perform(post("/rsvp/invite").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\": \"Admin Invite\"}")).andExpect(status().isCreated());
        }

        @Test
        void canPatchInvite() throws Exception {
            mockMvc.perform(patch("/rsvp/invite/{id}", INVITE_A).contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\": \"Updated\"}")).andExpect(status().isOk());
        }

        @Test
        void canDeleteInvite() throws Exception {
            mockMvc.perform(delete("/rsvp/invite/{id}", INVITE_A)).andExpect(status().isNoContent());
        }
    }

    // ==========================================
    // GUEST ENDPOINTS (cross-tenant)
    // ==========================================

    @Nested
    @DisplayName("Guests (cross-tenant via weddingId)")
    class GuestAdmin {

        @Test
        void canListGuests() throws Exception {
            mockMvc.perform(get("/rsvp/guest")).andExpect(status().isOk());
        }

        @Test
        void canCreateGuest() throws Exception {
            mockMvc.perform(post("/rsvp/guest").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\": \"Admin Guest\", \"age_group\": \"ADULT\", \"invite_id\": \"" + INVITE_A + "\"}"))
                    .andExpect(status().isCreated());
        }

        @Test
        void canPatchGuest() throws Exception {
            mockMvc.perform(patch("/rsvp/guest/{id}", GUEST_A).contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\": \"Updated\"}")).andExpect(status().isOk());
        }

        @Test
        void canDeleteGuest() throws Exception {
            mockMvc.perform(delete("/rsvp/guest/{id}", GUEST_A)).andExpect(status().isNoContent());
        }
    }

    // ==========================================
    // GIFT ENDPOINTS (cross-tenant with wedding param)
    // ==========================================

    @Nested
    @DisplayName("Gifts (cross-tenant with wedding param)")
    class GiftAdmin {

        @Test
        void canListGiftsForWeddingA() throws Exception {
            mockMvc.perform(get("/gift").param("wedding", WEDDING_A)).andExpect(status().isOk());
        }

        @Test
        void canListGiftsForWeddingB() throws Exception {
            mockMvc.perform(get("/gift").param("wedding", WEDDING_B)).andExpect(status().isOk());
        }

        @Test
        void listGiftsWithoutWeddingReturns400() throws Exception {
            mockMvc.perform(get("/gift")).andExpect(status().isBadRequest());
        }

        @Test
        void canCreateGift() throws Exception {
            mockMvc.perform(post("/gift").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"description\": \"Admin Gift\", \"price\": 10000, \"stock\": 3}"))
                    .andExpect(status().isCreated());
        }

        @Test
        void canGetGiftById() throws Exception {
            mockMvc.perform(get("/gift/{id}", GIFT_A)).andExpect(status().isOk());
        }

        @Test
        void canGetGiftFromOtherWedding() throws Exception {
            mockMvc.perform(get("/gift/{id}", GIFT_B)).andExpect(status().isOk());
        }

        @Test
        void canPatchGift() throws Exception {
            mockMvc.perform(patch("/gift/{id}", GIFT_A).contentType(MediaType.APPLICATION_JSON)
                    .content("{\"description\": \"Updated\"}")).andExpect(status().isOk());
        }

        @Test
        void canDeleteGift() throws Exception {
            mockMvc.perform(delete("/gift/{id}", GIFT_A)).andExpect(status().isNoContent());
        }
    }

    // ==========================================
    // MESSAGE ENDPOINTS (cross-tenant)
    // ==========================================

    @Nested
    @DisplayName("Messages (cross-tenant via weddingId)")
    class MessageAdmin {

        @Test
        void canListMessages() throws Exception {
            mockMvc.perform(get("/message")).andExpect(status().isOk());
        }

        @Test
        void canPatchMessage() throws Exception {
            mockMvc.perform(patch("/message/{id}", MESSAGE_A).contentType(MediaType.APPLICATION_JSON)
                    .content("{\"is_favorite\": true}")).andExpect(status().isOk());
        }

        @Test
        void canReadMessage() throws Exception {
            mockMvc.perform(patch("/message/{id}/read", MESSAGE_A)).andExpect(status().isOk());
        }

        @Test
        void canDeleteMessage() throws Exception {
            mockMvc.perform(delete("/message/{id}", MESSAGE_A)).andExpect(status().isNoContent());
        }
    }
}
