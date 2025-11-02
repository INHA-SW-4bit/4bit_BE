package org.example.nextchallenge.chat.service;

import lombok.RequiredArgsConstructor;
import org.example.nextchallenge.chat.document.ChatMessage;
import org.example.nextchallenge.chat.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    // 익명 번호 관리용 (lectureId 기준)
    private final Map<Long, Map<String, Integer>> anonymousMap = new ConcurrentHashMap<>();
    private final Map<Long, Integer> anonymousCounter = new ConcurrentHashMap<>();


     //새 메시지 저장 (익명 번호 부여)

    public ChatMessage saveMessage(
            Long lectureId,
            Long userId,
            String senderLoginId,
            String senderUsername,
            String role,
            String content
    ) {
        Integer anonymousNumber = null;

        // 학생일 경우 익명 번호 부여
        if ("STUDENT".equalsIgnoreCase(role)) {
            Map<String, Integer> map = anonymousMap.computeIfAbsent(lectureId, id -> new ConcurrentHashMap<>());
            int counter = anonymousCounter.getOrDefault(lectureId, 1);

            if (!map.containsKey(senderLoginId)) {
                map.put(senderLoginId, counter);
                anonymousCounter.put(lectureId, counter + 1);
            }
            anonymousNumber = map.get(senderLoginId);
        }

        ChatMessage chatMessage = ChatMessage.builder()
                .lectureId(lectureId)
                .userId(userId)
                .senderLoginId(senderLoginId)
                .senderUsername(senderUsername)
                .role(role)
                .content(content)
                .createdAt(LocalDateTime.now())
                .anonymousNumber(anonymousNumber)
                .build();

        return chatMessageRepository.save(chatMessage);
    }

    //커서 기반 조회 (limit+1개 불러와 hasMore 계산)

    public ChatPageResult findMessagesBefore(Long lectureId, LocalDateTime cursor, int limit) {
        List<ChatMessage> fetched;

        // cursor가 없으면 최신순으로 limit+1개 조회
        if (cursor == null) {
            fetched = chatMessageRepository
                    .findByLectureIdOrderByCreatedAtDesc(lectureId)
                    .stream()
                    .limit(limit + 1)
                    .collect(Collectors.toList());
        } else {
            // cursor 이전(createdAt < cursor) 메시지만 limit+1개 조회
            fetched = chatMessageRepository
                    .findByLectureIdAndCreatedAtBeforeOrderByCreatedAtDesc(lectureId, cursor)
                    .stream()
                    .limit(limit + 1)
                    .collect(Collectors.toList());
        }

        // ✅ 남은 메시지가 있으면 true
        boolean hasMore = fetched.size() > limit;

        // 반환용 메시지는 limit까지만
        List<ChatMessage> limited = fetched.stream()
                .limit(limit)
                .collect(Collectors.toList());

        return new ChatPageResult(limited, hasMore);
    }


     //커서 기반 페이지 결과 DTO

    public record ChatPageResult(List<ChatMessage> messages, boolean hasMore) {}
}
