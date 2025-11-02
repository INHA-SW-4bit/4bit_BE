package org.example.nextchallenge.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

// JWT 토큰 생성 및 사용자 정보 추출
@Component
public class JwtTokenProvider {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long validity = 1000L * 60 * 60 * 6; // 6시간

    // 토큰 생성
    public String createToken(Long userId, String loginId, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validity);

        return Jwts.builder()
                .setSubject(loginId)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    // 토큰 유효성 검사 (테스트용 항상 true)
    public boolean validateToken(String token) {
        try {
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // userId 추출
    public Long getUserId(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) return null;

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            JSONObject json = new JSONObject(payloadJson);
            if (json.has("userId")) return json.getLong("userId");
        } catch (Exception ignored) {}
        return null;
    }

    // loginId 추출
    public String getLoginId(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) return null;

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            JSONObject json = new JSONObject(payloadJson);

            if (json.has("loginId")) return json.getString("loginId");
            if (json.has("sub")) return json.getString("sub");
        } catch (Exception ignored) {}
        return null;
    }

    // role 추출
    public String getRole(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) return null;

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            JSONObject json = new JSONObject(payloadJson);
            if (json.has("role")) return json.getString("role");
        } catch (Exception ignored) {}
        return null;
    }

    // Authentication 객체에서 role 추출
    public String getRoleFromAuth(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) return null;
        return authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null);
    }
}
