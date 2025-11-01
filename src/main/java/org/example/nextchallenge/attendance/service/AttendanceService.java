package org.example.nextchallenge.attendance.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.nextchallenge.attendance.dto.CheckRequestDto;
import org.example.nextchallenge.attendance.dto.CheckResponseDto;
import org.example.nextchallenge.attendance.dto.WifiConnectVerifyResponseDto;
import org.example.nextchallenge.attendance.entity.AttendanceRecord;
import org.example.nextchallenge.attendance.entity.AttendanceSession;
import org.example.nextchallenge.attendance.repository.AttendanceRecordRepository;
import org.example.nextchallenge.attendance.repository.AttendanceSessionRepository;
import org.example.nextchallenge.lecture.entity.Lecture;
import org.example.nextchallenge.lecture.repository.LectureRepository;
import org.example.nextchallenge.seat.entity.Seat;
import org.example.nextchallenge.seat.repository.SeatRepository;
import org.example.nextchallenge.user.entity.User;
import org.example.nextchallenge.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final AttendanceSessionRepository sessionRepository;
    private final SeatRepository seatRepository;
    private final AttendanceRecordRepository recordRepository;

    /**
     * Wi-Fi 연결 검증
     */
    public WifiConnectVerifyResponseDto verifyWifiConnection(Long lectureId, HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }

        System.out.println("감지된 클라이언트 IP: " + clientIp);

        // 인하대 Wi-Fi 대역 예시 (165.246.*.*)
        boolean isValid = clientIp.startsWith("165.246.");

        return WifiConnectVerifyResponseDto.builder()
                .valid(isValid)
                .detectedIp(clientIp)
                .build();
    }

    /**
     * 출석 체크
     */
    @Transactional
    public CheckResponseDto checkAttendance(Long lectureId, CheckRequestDto request, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        Lecture lecture = lectureRepository.findById(lectureId).orElse(null);
        AttendanceSession session = sessionRepository.findByLectureAndAttendanceCode(lecture, request.getAttendanceCode()).orElse(null);
        Seat seat = seatRepository.findByLectureAndRowNumberAndColNumber(
                lecture,
                request.getRowNumber(),
                request.getColNumber()
        );


        if (seat != null && user != null) {
            seat.assignStudent(user);
        }

        AttendanceRecord record = AttendanceRecord.builder()
                .lecture(lecture)
                .user(user)
                .seat(seat)
                .session(session)
                .timestamp(LocalDateTime.now())
                .build();

        recordRepository.save(record);

        return CheckResponseDto.builder()
                .success(true)
                .message("출석이 완료되었습니다.")
                .RowNumber(seat.getRowNumber())
                .ColNumber(seat.getColNumber())
                .attendanceTime(LocalDateTime.now())
                .build();
    }
}
