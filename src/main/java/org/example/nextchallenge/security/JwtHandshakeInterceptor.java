package org.example.nextchallenge.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

// WebSocket 연결 전에 JWT 인증 처리
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final boolean useJwtAuth = true; // true: JWT 인증 사용 / false: 테스트 모드

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        // HTTP 요청으로 변환 가능할 때만 처리
        if (request instanceof ServletServerHttpRequest servletRequest) {
            var servlet = servletRequest.getServletRequest();

            // 토큰 추출 (쿼리 파라미터 > 헤더 순서)
            String tokenParam = servlet.getParameter("token");
            String authHeader = servlet.getHeader("Authorization");
            if (authHeader == null && tokenParam != null) {
                authHeader = "Bearer " + tokenParam;
            }

            // JWT 유효성 검사
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                if (jwtTokenProvider.validateToken(token)) {
                    // 토큰에서 사용자 정보 추출
                    Long userId = jwtTokenProvider.getUserId(token);
                    String loginId = jwtTokenProvider.getLoginId(token);
                    String role = jwtTokenProvider.getRole(token);

                    // WebSocket 세션에 사용자 정보 저장
                    attributes.put("userId", userId);
                    attributes.put("loginId", loginId);
                    attributes.put("role", role);

                    System.out.printf("✅ [WebSocket 인증 성공] loginId=%s, role=%s%n", loginId, role);
                    return true;
                }

                System.out.println("❌ [WebSocket 인증 실패] JWT 검증 오류");
                return false;
            }

            System.out.println("⚠️ [WebSocket 인증 실패] JWT 토큰이 전달되지 않음");
            return false;
        }

        // HTTP 요청이 아닐 경우
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // (선택) 인증 후처리
    }
}
