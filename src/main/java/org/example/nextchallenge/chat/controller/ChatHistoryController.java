package org.example.nextchallenge.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.nextchallenge.chat.document.ChatMessage;
import org.example.nextchallenge.chat.dto.ChatMessageListResponseDto;
import org.example.nextchallenge.chat.service.ChatService;
import org.example.nextchallenge.chat.service.ChatService.ChatPageResult;
import org.example.nextchallenge.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lectures/{lectureId}/chat")
public class ChatHistoryController {

    private final ChatService chatService;

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

        ChatPageResult result = chatService.findMessagesBefore(lectureId, cursorTime, limit);

        //  result 안에서 메시지와 hasMore 함께 꺼냄
        return ChatMessageListResponseDto.of(
                lectureId,
                result.messages(),    // List<ChatMessage>
                result.hasMore(),     // boolean
                isProfessorView,
                loginId
        );
    }
}
