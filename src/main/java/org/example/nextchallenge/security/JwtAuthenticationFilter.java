package org.example.nextchallenge.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// HTTP 요청마다 실행되는 JWT 인증 필터
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {


        // 1) WebSocket 경로는 JWT 필터링 제외
        if (request.getRequestURI().startsWith("/ws")) {
            chain.doFilter(request, response);
            return;
        }

        // 2) Authorization 헤더에서 토큰 추출
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        // 3) JWT 유효성 검사
        if (!jwtTokenProvider.validateToken(token)) {
            chain.doFilter(request, response);
            return;
        }

        // 4) 사용자 정보 조회 및 SecurityContext 설정
        String loginId = jwtTokenProvider.getLoginId(token);
        var userDetails = userDetailsService.loadUserByUsername(loginId);

        var auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 5) 다음 필터로 전달
        chain.doFilter(request, response);
    }
}
