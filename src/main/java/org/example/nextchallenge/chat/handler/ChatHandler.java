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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {

    private final ChatService chatService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 강의실별 세션 관리: Map<lectureId, Set<WebSocketSession>>
    private final Map<Long, Set<WebSocketSession>> lectureRooms = new ConcurrentHashMap<>();
    // 세션이 어느 강의실에 있는지 추적: Map<WebSocketSession, lectureId>
    private final Map<WebSocketSession, Long> sessionToLectureId = new ConcurrentHashMap<>();

    // 강의별 익명 매핑: Map<lectureId, Map<loginId, 익명번호>>
    private final Map<Long, Map<String, Integer>> lectureAnonymousMap = new ConcurrentHashMap<>();
    // 강의별 익명 카운터
    private final Map<Long, Integer> lectureAnonymousCounter = new ConcurrentHashMap<>();

    // 연결 시
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Map<String, Object> attrs = session.getAttributes();
        String loginId = (String) attrs.get("loginId");
        log.info("✅ WebSocket 연결됨: session={}, user={}", session.getId(), loginId);
    }

    // 메시지 수신
    @Override
    protected void handleTextMessage(WebSocketSession senderSession, TextMessage message) throws Exception {
        JsonNode node = objectMapper.readTree(message.getPayload());
        String type = node.has("type") ? node.get("type").asText() : "CHAT";

        // 과거 메시지 불러오기 요청
        if ("FETCH_HISTORY".equalsIgnoreCase(type)) {
            handleFetchHistory(senderSession, node);
            return;
        }

        // --- 기본 채팅 메시지 처리 ---
        Map<String, Object> senderAttrs = senderSession.getAttributes();
        Long userId = (Long) senderAttrs.get("userId");
        String senderLoginId = (String) senderAttrs.get("loginId");
        String senderRole = (String) senderAttrs.get("role");

        Long lectureId = node.get("lectureId").asLong();
        String content = node.get("content").asText();

        // 방 이동
        updateSessionRoom(senderSession, lectureId);

        // MongoDB 저장
        ChatMessage saved = chatService.saveMessage(
                lectureId,
                userId,
                senderLoginId,
                null,
                senderRole,
                content
        );

        // 같은 강의실 모두에게 브로드캐스트
        broadcastMessage(lectureId, saved);
    }

    // 연결 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        Long lectureId = sessionToLectureId.remove(session);
        if (lectureId != null) {
            Set<WebSocketSession> roomSessions = lectureRooms.get(lectureId);
            if (roomSessions != null) {
                roomSessions.remove(session);
            }
        }
        log.info("❌ 연결 종료: session={}, lectureId={}", session.getId(), lectureId);
    }

    // 채팅방 업데이트
    private void updateSessionRoom(WebSocketSession session, Long newLectureId) {
        Long oldLectureId = sessionToLectureId.get(session);
        if (newLectureId.equals(oldLectureId)) {
            return;
        }

        // 기존 방에서 제거
        if (oldLectureId != null) {
            Set<WebSocketSession> oldRoom = lectureRooms.get(oldLectureId);
            if (oldRoom != null) {
                oldRoom.remove(session);
            }
        }

        // 새 방에 추가
        lectureRooms
                .computeIfAbsent(newLectureId, id -> ConcurrentHashMap.newKeySet())
                .add(session);

        sessionToLectureId.put(session, newLectureId);
        log.info("➡️ 채팅방 이동: user={} -> lectureId={}", session.getAttributes().get("loginId"), newLectureId);
    }

    // 브로드캐스트
    private void broadcastMessage(Long lectureId, ChatMessage saved) throws IOException {
        Set<WebSocketSession> roomSessions = lectureRooms.get(lectureId);
        if (roomSessions == null) {
            return;
        }

        for (WebSocketSession targetSession : roomSessions) {
            if (!targetSession.isOpen()) {
                continue;
            }

            Map<String, Object> targetAttrs = targetSession.getAttributes();
            String viewerRole = (String) targetAttrs.get("role");
            String viewerLoginId = (String) targetAttrs.get("loginId");

            String displayName = resolveDisplayNameForViewer(
                    lectureId,
                    viewerRole,
                    saved.getRole(),
                    viewerLoginId,
                    saved.getSenderLoginId()
            );

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("type", "CHAT");
            response.put("lectureId", saved.getLectureId());
            response.put("messageId", saved.getId());
            response.put("senderName", displayName);
            response.put("senderLoginId", saved.getSenderLoginId());
            response.put("role", saved.getRole());
            response.put("content", saved.getContent());
            response.put("createdAt", saved.getCreatedAt() != null ? saved.getCreatedAt().toString() : "");
            // 내 메시지인지 여부
            response.put("mine", viewerLoginId != null && viewerLoginId.equals(saved.getSenderLoginId()));

            targetSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
        }
    }

    // 커서 기반 채팅 히스토리 조회
    private void handleFetchHistory(WebSocketSession session, JsonNode node) throws IOException {
        Long lectureId = node.get("lectureId").asLong();
        String cursorStr = node.has("cursor") ? node.get("cursor").asText() : null;
        int limit = node.has("limit") ? node.get("limit").asInt() : 20;

        LocalDateTime cursor = (cursorStr != null)
                ? LocalDateTime.parse(cursorStr)
                : LocalDateTime.now();

        // 서비스에서 페이지 결과 받아오기
        ChatService.ChatPageResult result = chatService.findMessagesBefore(lectureId, cursor, limit);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("type", "HISTORY_RESULT");
        response.put("lectureId", lectureId);
        response.put("count", result.messages().size());
        response.put("hasMore", result.hasMore());
        response.put("messages", result.messages());

        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
        log.info("📜 [{}] 이전 메시지 {}개 전송 (hasMore={})", lectureId, result.messages().size(), result.hasMore());
    }

    // 교수/학생 이름 처리 (익명 + 번호)
    private String resolveDisplayNameForViewer(
            Long lectureId,
            String viewerRole,
            String senderRole,
            String viewerLoginId,
            String senderLoginId
    ) {
        // 교수 화면
        if ("PROFESSOR".equalsIgnoreCase(viewerRole)) {
            if ("PROFESSOR".equalsIgnoreCase(senderRole)) {
                return "교수님";
            }
            if ("STUDENT".equalsIgnoreCase(senderRole)) {
                return (senderLoginId != null && !senderLoginId.isBlank()) ? senderLoginId : "학생";
            }
        }

        // 학생 화면 (익명번호 부여)
        if ("STUDENT".equalsIgnoreCase(viewerRole)) {
            if ("PROFESSOR".equalsIgnoreCase(senderRole)) {
                return "교수님";
            }
            if ("STUDENT".equalsIgnoreCase(senderRole)) {
                // 강의별 익명 매핑
                lectureAnonymousCounter.putIfAbsent(lectureId, 1);
                Map<String, Integer> anonMap =
                        lectureAnonymousMap.computeIfAbsent(lectureId, id -> new ConcurrentHashMap<>());

                if (senderLoginId == null) {
                    return "익명";
                }

                // 없으면 새 번호 부여
                anonMap.computeIfAbsent(senderLoginId, k -> {
                    int current = lectureAnonymousCounter.get(lectureId);
                    lectureAnonymousCounter.put(lectureId, current + 1);
                    return current;
                });

                return "익명" + anonMap.get(senderLoginId);
            }
        }

        // 나머지
        return (senderLoginId != null && !senderLoginId.isBlank()) ? senderLoginId : "사용자";
    }
}
