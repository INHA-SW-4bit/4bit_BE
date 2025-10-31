package org.example.nextchallenge.chat.service;

import org.example.nextchallenge.chat.document.ChatMessage;
import org.example.nextchallenge.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;

    //완성된 document형식으로 객체 반환
    public ChatMessage saveMessage(
            Long lectureId,
            Long userId,
            String senderLoginId,
            String senderUsername,
            String role,
            String content
    ) {
        ChatMessage chatMessage = ChatMessage.builder()
                .lectureId(lectureId)
                .userId(userId)
                .senderLoginId(senderLoginId)
                .senderUsername(senderUsername)
                .role(role)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        return chatMessageRepository.save(chatMessage);
    }

    //오래된 순으로 채팅내역들 리턴
    public List<ChatMessage> getMessages(Long lectureId) {
        return chatMessageRepository.findByLectureIdOrderByCreatedAtAsc(lectureId);
    }}
