package org.example.nextchallenge.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

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
                .signWith(key, SignatureAlgorithm.HS256) // Specify algorithm
                .compact();
    }

    // 토큰에서 Claims 추출 (서명 검증 포함)
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            getClaims(token); // Claims 추출 시 서명 및 만료일 자동 검증
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            // 잘못된 JWT 서명
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            // 만료된 JWT 토큰
            System.out.println("Expired JWT token: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            // 지원되지 않는 JWT 토큰
            System.out.println("Unsupported JWT token: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // JWT 클레임 문자열이 비어있음
            System.out.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }

    // userId 추출
    public Long getUserId(String token) {
        return getClaims(token).get("userId", Long.class);
    }

    // loginId 추출
    public String getLoginId(String token) {
        return getClaims(token).getSubject();
    }

    // role 추출
    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // Authentication 객체에서 role 추출
    public String getRoleFromAuth(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) return null;
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")); // 여러 권한이 있을 수 있으므로 쉼표로 연결
    }
}
