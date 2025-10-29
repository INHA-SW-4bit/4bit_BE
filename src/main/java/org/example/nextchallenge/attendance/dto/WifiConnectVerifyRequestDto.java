package org.example.nextchallenge.attendance.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WifiConnectVerifyRequestDto {
    private String publicIp;  // 교내 Wi-Fi로 접속 시 나가는 공인 IP
}
