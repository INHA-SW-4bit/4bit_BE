package org.example.nextchallenge.attendance.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.nextchallenge.attendance.dto.*;
import org.example.nextchallenge.attendance.service.AttendanceService;
import org.example.nextchallenge.user.details.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lectures/{lectureId}/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    // 와이파이 인증 API
    @PostMapping("/wifi/verify")
    public ResponseEntity<WifiConnectVerifyResponseDto> verifyWifi(
            @PathVariable Long lectureId,
            HttpServletRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        System.out.println("Controller 진입 성공");
        WifiConnectVerifyResponseDto response = attendanceService.verifyWifiConnection(lectureId, request);
        return ResponseEntity.ok(response);
    }

    // 출석 체크 API
    @PostMapping("/check")
    public ResponseEntity<CheckResponseDto> checkAttendance(
            @PathVariable Long lectureId,
            @RequestBody CheckRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        // JWT 미적용 상태이므로 null 방어 코드 추가
        Long userId;

        if (userDetails == null) {
            userId = 1L; // 테스트용 기본 유저 ID
        } else {
            userId = userDetails.getUserId();
        }

        CheckResponseDto response = attendanceService.checkAttendance(lectureId, request, userId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/start")
    public ResponseEntity<CodeCreateResponseDto> createAttendanceSession(
            @PathVariable Long lectureId,
            @RequestBody CodeCreateRequestDto requestDto
    ) {
        CodeCreateResponseDto response = attendanceService.createAttendanceSession(lectureId, requestDto);
        return ResponseEntity.ok(response);
    }

    // 출석 종료 api
    @PostMapping("/end")
    public ResponseEntity<AttendanceEndResponseDto> endAttendance(@RequestBody AttendanceEndRequestDto requestDto) {
        AttendanceEndResponseDto response = attendanceService.endAttendanceSession(requestDto);
        return ResponseEntity.ok(response);
    }
}
