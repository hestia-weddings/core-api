package com.hestia.api.security.access;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Nested
    @DisplayName("Own wedding (should succeed)")
    class OwnWedding {

        @Test
        void canListInvites() throws Exception {
            mockMvc.perform(get("/rsvp/invite"))
                    .andExpect(status().isOk());
        }

        @Test
        void canCreateInvite() throws Exception {
            mockMvc.perform(post("/rsvp/invite")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"New Invite\"}"))
                    .andExpect(status().isCreated());
        }

        @Test
        void canListGuests() throws Exception {
            mockMvc.perform(get("/rsvp/guest"))
                    .andExpect(status().isOk());
        }

        @Test
        void canListGifts() throws Exception {
            mockMvc.perform(get("/gift"))
                    .andExpect(status().isOk());
        }

        @Test
        void canCreateGift() throws Exception {
            mockMvc.perform(post("/gift")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"description\": \"Toaster\", \"price\": 5000, \"stock\": 1}"))
                    .andExpect(status().isCreated());
        }

        @Test
        void canListMessages() throws Exception {
            mockMvc.perform(get("/message"))
                    .andExpect(status().isOk());
        }

        @Test
        void canGetOwnWedding() throws Exception {
            mockMvc.perform(get("/wedding/{id}", OWN_WEDDING))
                    .andExpect(status().isOk());
        }

        @Test
        void canUpdateOwnWedding() throws Exception {
            mockMvc.perform(patch("/wedding/{id}", OWN_WEDDING)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"couple_name\": \"Alice & Bobby\"}"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Other wedding (should return 403)")
    class OtherWedding {

        @Test
        void cannotGetOtherWedding() throws Exception {
            mockMvc.perform(get("/wedding/{id}", OTHER_WEDDING))
                    .andExpect(status().isForbidden());
        }

        @Test
        void cannotUpdateOtherWedding() throws Exception {
            mockMvc.perform(patch("/wedding/{id}", OTHER_WEDDING)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"couple_name\": \"Hacked\"}"))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("Admin-only endpoints (should return 403)")
    class AdminOnly {

        @Test
        void cannotListAllWeddings() throws Exception {
            mockMvc.perform(get("/wedding"))
                    .andExpect(status().isForbidden());
        }

        @Test
        void cannotCreateWedding() throws Exception {
            mockMvc.perform(post("/wedding")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"couple_name\": \"X & Y\", \"slug\": \"x-y\"}"))
                    .andExpect(status().isForbidden());
        }

        @Test
        void cannotDeleteWedding() throws Exception {
            mockMvc.perform(delete("/wedding/{id}", OWN_WEDDING))
                    .andExpect(status().isForbidden());
        }

        @Test
        void cannotListAccounts() throws Exception {
            mockMvc.perform(get("/account"))
                    .andExpect(status().isForbidden());
        }

        @Test
        void cannotCreateAccount() throws Exception {
            mockMvc.perform(post("/account")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"Hacker\", \"email\": \"h@h.com\", \"role\": \"ADMIN\", \"auth_user_id\": \"00000000-0000-0000-0000-000000000000\", \"wedding_id\": \"11111111-1111-1111-1111-111111111111\"}"))
                    .andExpect(status().isForbidden());
        }

        @Test
        void cannotDeleteAccount() throws Exception {
            mockMvc.perform(delete("/account/{id}", "aaaa0000-0000-0000-0000-000000000001"))
                    .andExpect(status().isForbidden());
        }
    }
}
