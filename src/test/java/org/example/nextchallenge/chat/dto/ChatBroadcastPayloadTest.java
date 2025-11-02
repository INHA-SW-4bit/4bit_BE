package org.example.nextchallenge.chat.dto;

import org.example.nextchallenge.chat.document.ChatMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ChatBroadcastPayloadTest {

    @Test
    @DisplayName("ChatMessage와 displayName으로 ChatBroadcastPayload를 생성해야 한다")
    void of_shouldCreateChatBroadcastPayload() {
        // Given
        ChatMessage chatMessage = ChatMessage.builder()
                .id("msg123")
                .lectureId(1L)
                .userId(101L)
                .senderLoginId("student123")
                .senderUsername("Student Name")
                .role("STUDENT")
                .content("Test message content")
                .createdAt(LocalDateTime.of(2025, 1, 1, 10, 0, 0))
                .build();
        String displayName = "익명123";

        // When
        ChatBroadcastPayload payload = ChatBroadcastPayload.of(chatMessage, displayName);

        // Then
        assertNotNull(payload);
        assertEquals("msg123", payload.getMessageId());
        assertEquals(1L, payload.getLectureId());
        assertEquals(displayName, payload.getSnederName()); // Note: typo in original DTO: snederName
        assertEquals("Test message content", payload.getContent());
        assertEquals(LocalDateTime.of(2025, 1, 1, 10, 0, 0).toString(), payload.getCreatedAt());
    }
}
