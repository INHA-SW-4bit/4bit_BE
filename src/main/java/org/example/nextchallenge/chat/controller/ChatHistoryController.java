package org.example.nextchallenge.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.nextchallenge.chat.document.ChatMessage;
import org.example.nextchallenge.chat.dto.ChatMessageListResponseDto;
import org.example.nextchallenge.chat.service.ChatService;
import org.example.nextchallenge.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lectures/{lectureId}/chat")
public class ChatHistoryController {

    private final ChatService chatService;

    /**
     * 특정 강의의 채팅 메시지 조회 (커서 기반 지원)
     * - cursor: ISO-8601 날짜 문자열 (예: 2025-11-02T18:00:00)
     * - limit: 한 번에 가져올 메시지 개수 (기본 30)
     *
     * 요청 예시:
     * GET /api/lectures/1/chat/messages?cursor=2025-11-02T18:00:00&limit=20
     */

    @GetMapping("/messages")
    public ChatMessageListResponseDto getChatHistory(
            @PathVariable Long lectureId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "20") int limit
    ) {
        if (userDetails == null) {
            log.warn("인증된 사용자 정보가 없습니다. lectureId={}", lectureId);
            throw new IllegalArgumentException("사용자 인증이 필요합니다.");
        }

        String loginId = userDetails.getLoginId();
        String role = userDetails.getRole().name();
        boolean isProfessorView = "PROFESSOR".equalsIgnoreCase(role);

        LocalDateTime cursorTime = (cursor != null)
                ? LocalDateTime.parse(cursor)
                : LocalDateTime.now();

        log.info("[ChatHistory] 커서 기반 조회 요청 - lectureId={}, loginId={}, role={}, cursor={}, limit={}",
                lectureId, loginId, role, cursorTime, limit);

        // 커서 이전 메시지 불러오기
        List<ChatMessage> messages = chatService.findMessagesBefore(lectureId, cursorTime, limit);

        return ChatMessageListResponseDto.of(lectureId, messages, isProfessorView);
    }
}
