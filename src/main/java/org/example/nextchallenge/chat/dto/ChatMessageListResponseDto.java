package org.example.nextchallenge.chat.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.nextchallenge.chat.document.ChatMessage;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


 //전체 채팅 목록을 반환하는 DTO (Mongo 기반 무한스크롤용)

@Getter
@Builder
public class ChatMessageListResponseDto {

    private Long lectureId;
    private List<ChatMessageItem> messages;
    private boolean hasMore; // 추가 메시지 여부 (무한스크롤용)

    @Getter
    @Builder
    public static class ChatMessageItem {
        private String messageId;
        private String senderName;
        private String senderLoginId;
        private String content;
        private String createdAt;
        private boolean mine;
    }



    //ChatMessage 리스트를 화면용 DTO로 변환

    public static ChatMessageListResponseDto of(
            Long lectureId,
            List<ChatMessage> chatMessages, // 최신순(limit)으로 가져온 메시지
            boolean hasMore,                // ChatService에서 계산된 hasMore
            boolean isProfessorView,        // true면 교수, false면 학생
            String currentLoginId           // 현재 사용자 loginId
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //  createdAt null-safe 오름차순 정렬
        List<ChatMessageItem> sortedMessages = chatMessages.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(
                        ChatMessage::getCreatedAt,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ))
                .map(m -> ChatMessageItem.builder()
                        .messageId(m.getId())
                        .senderName(resolveDisplayName(m, isProfessorView))
                        .senderLoginId(m.getSenderLoginId())
                        .content(m.getContent() != null ? m.getContent() : "")
                        .createdAt(m.getCreatedAt() != null ? m.getCreatedAt().format(formatter) : "")
                        .mine(isMyMessage(m.getSenderLoginId(), currentLoginId))
                        .build())
                .collect(Collectors.toList());

        return ChatMessageListResponseDto.builder()
                .lectureId(lectureId)
                .messages(sortedMessages)
                .hasMore(hasMore)
                .build();
    }


     // 내 메시지 여부 판단 (null-safe)
    private static boolean isMyMessage(String senderLoginId, String currentLoginId) {
        return senderLoginId != null && senderLoginId.equalsIgnoreCase(currentLoginId);
    }


     // 요청자 역할에 따라 표시 이름 결정
    private static String resolveDisplayName(ChatMessage m, boolean isProfessorView) {
        String senderRole = m.getRole();

        if ("PROFESSOR".equalsIgnoreCase(senderRole)) {
            return "교수님";
        }

        // 학생 메시지 처리
        if (isProfessorView) {
            // 교수는 학번으로 표시
            return m.getSenderLoginId() != null ? m.getSenderLoginId() : "학생";
        } else {
            // 학생은 익명으로 표시
            Integer anonNum = m.getAnonymousNumber();
            return (anonNum != null) ? "익명" + anonNum : "익명";
        }
    }
}
