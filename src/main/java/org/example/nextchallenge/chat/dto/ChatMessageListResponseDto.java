package org.example.nextchallenge.chat.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.nextchallenge.chat.document.ChatMessage;

import java.util.List;

// 전체 채팅 목록을 반환하는 DTO

@Getter
@Builder
public class ChatMessageListResponseDto {

    private Long lectureId;
    private List<ChatMessageItem> messages;

    @Getter
    @Builder
    public static class ChatMessageItem {
        private String messageId;
        private String senderName;     // 화면에 표시되는 이름 (익명 / 교수님 / 학번)
        private String senderLoginId;  // 실제 사용자 구분용
        private String content;
        private String createdAt;
    }

    // ChatMessage 리스트를 화면용 DTO 리스트로 변환
    public static ChatMessageListResponseDto of(
            Long lectureId,
            List<ChatMessage> chatMessages,
            boolean isProfessorView // true면 교수, false면 학생
    ) {
        return ChatMessageListResponseDto.builder()
                .lectureId(lectureId)
                .messages(chatMessages.stream()
                        .map(m -> ChatMessageItem.builder()
                                .messageId(m.getId())
                                .senderName(resolveDisplayName(m, isProfessorView))  // 역할별 표시 이름 결정
                                .senderLoginId(m.getSenderLoginId())                 // 내 메시지 판별용
                                .content(m.getContent())
                                .createdAt(m.getCreatedAt() != null ? m.getCreatedAt().toString() : "")
                                .build())
                        .toList())
                .build();
    }

    // 요청자 역할에 따라 표시 이름 결정
    private static String resolveDisplayName(ChatMessage m, boolean isProfessorView) {
        String senderRole = m.getRole();

        if ("PROFESSOR".equalsIgnoreCase(senderRole)) {
            // 교수 메시지는 항상 "교수님"
            return "교수님";
        } else {
            // 학생 메시지
            if (isProfessorView) {
                // 교수는 학번으로 봄
                return m.getSenderLoginId() != null ? m.getSenderLoginId() : "학생";
            } else {
                // 학생은 익명으로 봄
                return "익명";
            }
        }
    }
}
