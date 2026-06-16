package com.hestia.api.domain.message.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
@DisplayName("Message Delete Linked")
class MessageDeleteLinkedTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String LINKED_TO_ORDER = "ffff0000-0000-0000-0000-000000000003";
    private static final String LINKED_TO_INVITE = "ffff0000-0000-0000-0000-000000000004";
    private static final String UNLINKED = "ffff0000-0000-0000-0000-000000000001";

    @Test
    void returns409WhenDeletingMessageLinkedToOrder() throws Exception {
        mockMvc.perform(delete("/message/{id}", LINKED_TO_ORDER)).andExpect(status().isConflict());
    }

    @Test
    void returns409WhenDeletingMessageLinkedToInvite() throws Exception {
        mockMvc.perform(delete("/message/{id}", LINKED_TO_INVITE)).andExpect(status().isConflict());
    }

    @Test
    void returns204WhenDeletingUnlinkedMessage() throws Exception {
        mockMvc.perform(delete("/message/{id}", UNLINKED)).andExpect(status().isNoContent());
    }
}
