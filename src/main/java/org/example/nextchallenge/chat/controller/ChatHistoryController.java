package org.example.nextchallenge.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.nextchallenge.chat.document.ChatMessage;
import org.example.nextchallenge.chat.dto.ChatMessageListResponseDto;
import org.example.nextchallenge.chat.service.ChatService;
import org.example.nextchallenge.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 특정 강의 채팅 전체 메시지 조회 컨트롤러
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lectures/{lectureId}/chat")
public class ChatHistoryController {

    private final ChatService chatService;

    //  특정 강의의 모든 채팅 메시지 불러오는 함수
    @GetMapping("/messages")
    public ChatMessageListResponseDto getChatHistory(
            @PathVariable Long lectureId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        // 인증 정보 확인
        if (userDetails == null) {
            log.warn("인증된 사용자 정보가 없습니다. lectureId={}", lectureId);
            throw new IllegalArgumentException("사용자 인증이 필요합니다.");
        }

        // SecurityContext에서 사용자 정보 추출
        String loginId = userDetails.getLoginId();
        String role = userDetails.getRole().name();

        log.info("[ChatHistory] 전체 조회 요청 - lectureId={}, loginId={}, role={}", lectureId, loginId, role);

        // MongoDB에서 해당 강의의 메시지 전체 조회
        List<ChatMessage> messages = chatService.getMessages(lectureId);

        // 교수 / 학생 뷰 구분
        boolean isProfessorView = "PROFESSOR".equalsIgnoreCase(role);

        // DTO 변환 후 반환
        return ChatMessageListResponseDto.of(lectureId, messages, isProfessorView);
    }
}
