package org.example.nextchallenge.chat.service;

import org.example.nextchallenge.chat.document.ChatMessage;
import org.example.nextchallenge.chat.repository.ChatMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.DisplayName;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;



    private ChatService chatService;

    @Test
    @DisplayName("강의 ID로 메시지 목록을 반환해야 한다")
    void getMessages_shouldReturnMessages() {
        chatService = new ChatService(chatMessageRepository);

        Long lectureId = 1L;
        // Mock behavior for chatMessageRepository.findByLectureId
        when(chatMessageRepository.findByLectureIdOrderByCreatedAtAsc(anyLong())).thenReturn(List.of(
                new ChatMessage(
                        "id1", lectureId, 101L, "student123", "Student Name", "STUDENT", "Msg 1", java.time.LocalDateTime.now()
                ),
                new ChatMessage(
                        "id2", lectureId, 102L, "prof456", "Professor Name", "PROFESSOR", "Msg 2", java.time.LocalDateTime.now()
                )
        ));

        List<ChatMessage> messages = chatService.getMessages(lectureId);

        assertEquals(2, messages.size());
        assertEquals("id1", messages.get(0).getId());
        assertEquals("id2", messages.get(1).getId());

    }
}
