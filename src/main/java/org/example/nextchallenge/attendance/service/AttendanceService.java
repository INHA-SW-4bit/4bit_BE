package org.example.nextchallenge.attendance.service;

import lombok.RequiredArgsConstructor;
import org.example.nextchallenge.attendance.dto.WifiConnectVerifyRequestDto;
import org.example.nextchallenge.attendance.dto.WifiConnectVerifyResponseDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    public WifiConnectVerifyResponseDto verifyWifiConnection(Long lectureId, WifiConnectVerifyRequestDto requestDto) {
        System.out.println("📡 받은 IP: " + requestDto.getPublicIp());

        String publicIp = requestDto.getPublicIp();

        if (publicIp == null || publicIp.isEmpty()) {
            return WifiConnectVerifyResponseDto.builder()
                    .valid(false)
                    .detectedIp(null)
                    .build();
        }

        // 인하대학교 eduroam 공용 IP 대역
        String allowedPrefix = "165.246.";
        boolean isValid = publicIp.startsWith(allowedPrefix);

        return WifiConnectVerifyResponseDto.builder()
                .valid(isValid)
                .detectedIp(publicIp)
                .build();
    }
}
