package org.example.nextchallenge.chat.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.nextchallenge.chat.document.ChatMessage;
import org.example.nextchallenge.chat.service.ChatService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// 메인 WebSocket 핸들러
@Slf4j
@Component
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {

    private final ChatService chatService;

    // 전체 연결 세션 관리
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    // 웹소켓 연결 시 실행
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);

        Map<String, Object> attrs = session.getAttributes(); // JwtHandshakeInterceptor에서 저장한 사용자 정보
        String loginId = (String) attrs.get("loginId");
        String role = (String) attrs.get("role");

        log.info("✅ WebSocket 연결됨: loginId={}, role={}", loginId, role);
    }

    // 메시지 수신 시 실행
    @Override
    protected void handleTextMessage(WebSocketSession senderSession, TextMessage message) throws Exception {
        Map<String, Object> senderAttrs = senderSession.getAttributes();

        Long userId = (Long) senderAttrs.get("userId");
        String senderLoginId = (String) senderAttrs.get("loginId");
        String senderRole = (String) senderAttrs.get("role");

        // 프론트에서 받은 JSON 파싱
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(message.getPayload());
        Long lectureId = node.get("lectureId").asLong();
        String content = node.get("content").asText();

        // MongoDB에 메시지 저장
        ChatMessage saved = chatService.saveMessage(
                lectureId,
                userId,
                senderLoginId,
                null,
                senderRole,
                content
        );

        // 모든 세션에 메시지 전송
        for (WebSocketSession targetSession : sessions) {
            if (!targetSession.isOpen()) continue;

            Map<String, Object> targetAttrs = targetSession.getAttributes();
            String viewerRole = (String) targetAttrs.get("role");
            String viewerLoginId = (String) targetAttrs.get("loginId");

            // 교수/학생 화면별 표시 이름 처리
            String displayName = resolveDisplayNameForViewer(
                    viewerRole,
                    saved.getRole(),
                    viewerLoginId,
                    saved.getSenderLoginId()
            );

            // 최종 메시지 포맷 구성
            Map<String, Object> response = Map.of(
                    "lectureId", saved.getLectureId(),
                    "messageId", saved.getId(),
                    "senderName", displayName,
                    "senderLoginId", saved.getSenderLoginId(),
                    "role", saved.getRole(),
                    "content", saved.getContent(),
                    "createdAt", LocalDateTime.now().toString()
            );

            targetSession.sendMessage(new TextMessage(mapper.writeValueAsString(response)));
        }
    }

    // 연결 종료 시 실행
    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      org.springframework.web.socket.CloseStatus status) {
        sessions.remove(session);
        log.info("❌ 연결 종료: {}", session.getId());
    }

    // 교수/학생 화면 구분에 따른 이름 표시 로직
    private String resolveDisplayNameForViewer(
            String viewerRole,
            String senderRole,
            String viewerLoginId,
            String senderLoginId
    ) {
        // 교수 화면
        if ("PROFESSOR".equalsIgnoreCase(viewerRole)) {
            if ("PROFESSOR".equalsIgnoreCase(senderRole)) return "교수님";
            if ("STUDENT".equalsIgnoreCase(senderRole))
                return (senderLoginId != null && !senderLoginId.isBlank()) ? senderLoginId : "학생";
            return (senderLoginId != null && !senderLoginId.isBlank()) ? senderLoginId : "사용자";
        }

        // 학생 화면
        if ("STUDENT".equalsIgnoreCase(viewerRole)) {
            if ("PROFESSOR".equalsIgnoreCase(senderRole)) return "교수님";
            if ("STUDENT".equalsIgnoreCase(senderRole)) return "익명";
            return "익명";
        }

        // 예외 케이스
        return (senderLoginId != null && !senderLoginId.isBlank()) ? senderLoginId : "사용자";
    }
}
