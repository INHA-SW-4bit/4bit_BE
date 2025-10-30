package org.example.nextchallenge.attendance.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.nextchallenge.attendance.dto.WifiConnectVerifyResponseDto;
import org.example.nextchallenge.attendance.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lectures/{lectureId}/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/wifi/verify")
    public ResponseEntity<WifiConnectVerifyResponseDto> verifyWifi(
            @PathVariable Long lectureId,
            HttpServletRequest request
    ) {
        System.out.println("Controller 진입 성공"); // Test용
        WifiConnectVerifyResponseDto response = attendanceService.verifyWifiConnection(lectureId, request);
        return ResponseEntity.ok(response);
    }

}
