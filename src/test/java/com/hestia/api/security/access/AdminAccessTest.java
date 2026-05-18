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

    @Nested
    @DisplayName("Admin-only endpoints (should succeed)")
    class AdminOnlyEndpoints {

        @Test
        void canListAllWeddings() throws Exception {
            mockMvc.perform(get("/wedding"))
                    .andExpect(status().isOk());
        }

        @Test
        void canCreateWedding() throws Exception {
            mockMvc.perform(post("/wedding")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"couple_name\": \"Eve & Frank\", \"slug\": \"eve-frank\"}"))
                    .andExpect(status().isCreated());
        }

        @Test
        void canDeleteWedding() throws Exception {
            mockMvc.perform(delete("/wedding/{id}", WEDDING_A))
                    .andExpect(status().isNoContent());
        }

        @Test
        void canListAccounts() throws Exception {
            mockMvc.perform(get("/account"))
                    .andExpect(status().isOk());
        }

        @Test
        void canCreateAccount() throws Exception {
            mockMvc.perform(post("/account")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"New User\", \"email\": \"new@hestia.com\", \"role\": \"COUPLE\", \"auth_user_id\": \"cccc0000-0000-0000-0000-cccccccccccc\", \"wedding_id\": \"22222222-2222-2222-2222-222222222222\"}"))
                    .andExpect(status().isCreated());
        }

        @Test
        void canDeleteAccount() throws Exception {
            mockMvc.perform(delete("/account/{id}", "bbbb0000-0000-0000-0000-000000000001"))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Cross-tenant access (must pass wedding_id)")
    class CrossTenant {

        @Test
        void canAccessWeddingAGifts() throws Exception {
            mockMvc.perform(get("/gift").param("wedding", WEDDING_A))
                    .andExpect(status().isOk());
        }

        @Test
        void canAccessWeddingBGifts() throws Exception {
            mockMvc.perform(get("/gift").param("wedding", WEDDING_B))
                    .andExpect(status().isOk());
        }

        @Test
        void canAccessAnyWeddingById() throws Exception {
            mockMvc.perform(get("/wedding/{id}", WEDDING_B))
                    .andExpect(status().isOk());
        }

        @Test
        void giftsWithoutWeddingIdReturns400() throws Exception {
            mockMvc.perform(get("/gift"))
                    .andExpect(status().isBadRequest());
        }
    }
}
