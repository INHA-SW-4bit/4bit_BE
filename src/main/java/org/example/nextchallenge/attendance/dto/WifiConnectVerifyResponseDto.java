package org.example.nextchallenge.attendance.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class WifiConnectVerifyResponseDto {
    private boolean valid;      // 교내 IP 범위 여부
    private String detectedIp;  // 요청에서 감지된 실제 IP
}
