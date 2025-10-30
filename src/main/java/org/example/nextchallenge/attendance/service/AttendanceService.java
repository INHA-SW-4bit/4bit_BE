package org.example.nextchallenge.attendance.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.nextchallenge.attendance.dto.WifiConnectVerifyResponseDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    public WifiConnectVerifyResponseDto verifyWifiConnection(Long lectureId, HttpServletRequest request) {
        // IP 추출 로직
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }

        System.out.println("감지된 클라이언트 IP: " + clientIp); // 테스트용

        boolean isValid = clientIp.startsWith("165.246.");
        return WifiConnectVerifyResponseDto.builder()
                .valid(isValid)
                .detectedIp(clientIp)
                .build();
    }
}
