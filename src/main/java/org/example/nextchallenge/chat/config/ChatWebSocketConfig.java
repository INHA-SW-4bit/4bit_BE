package org.example.nextchallenge.chat.config;

import lombok.RequiredArgsConstructor;
import org.example.nextchallenge.chat.handler.ChatHandler;
import org.example.nextchallenge.security.JwtHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class ChatWebSocketConfig implements WebSocketConfigurer {

    private final ChatHandler chatHandler;
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Handler 방식으로 WebSocket 엔드포인트 등록
        registry.addHandler(chatHandler, "/ws/chat")
                .addInterceptors(jwtHandshakeInterceptor)   // JWT 인증 연결
                .setAllowedOriginPatterns("*");             // CORS 허용
    }
}
