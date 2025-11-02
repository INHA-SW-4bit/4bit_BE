package org.example.nextchallenge.chat.service;

import org.example.nextchallenge.chat.document.ChatMessage;
import org.example.nextchallenge.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    // 새 메시지 저장
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

    // 오래된 순으로 전체 메시지 조회
    public List<ChatMessage> getMessages(Long lectureId) {
        return chatMessageRepository.findByLectureIdOrderByCreatedAtAsc(lectureId);
    }

    // 커서 기반 (특정 시점 이전 메시지만 limit 만큼 조회)
    public List<ChatMessage> findMessagesBefore(Long lectureId, LocalDateTime cursor, int limit) {
        // cursor가 없으면 가장 최근 메시지부터 limit개 조회
        if (cursor == null) {
            return chatMessageRepository
                    .findByLectureIdOrderByCreatedAtDesc(lectureId)
                    .stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        }

        // cursor 이전(createdAt < cursor) 메시지를 최신순으로 불러오고, limit개까지만 반환
        return chatMessageRepository
                .findByLectureIdAndCreatedAtBeforeOrderByCreatedAtDesc(lectureId, cursor)
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
}
