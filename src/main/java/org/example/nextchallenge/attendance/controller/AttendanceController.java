package org.example.nextchallenge.attendance.controller;

import lombok.RequiredArgsConstructor;
import org.example.nextchallenge.attendance.dto.WifiConnectVerifyRequestDto;
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
            @RequestBody WifiConnectVerifyRequestDto requestDto
    ) {
        System.out.println("🎯 Controller 진입 성공");
        WifiConnectVerifyResponseDto response = attendanceService.verifyWifiConnection(lectureId, requestDto);
        return ResponseEntity.ok(response);
    }

}
