package org.example.nextchallenge.attendance.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.nextchallenge.attendance.dto.*;
import org.example.nextchallenge.attendance.entity.AttendanceRecord;
import org.example.nextchallenge.attendance.entity.AttendanceSession;
import org.example.nextchallenge.attendance.entity.SessionStatus;
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
import java.time.format.DateTimeFormatter;
import java.util.Random;

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


    /**
     교수 : 출석코드 생성*/
    @Transactional
    public CodeCreateResponseDto createAttendanceSession(Long lectureId, CodeCreateRequestDto dto) {

        Lecture lecture = lectureRepository.findById(lectureId).get();

        // 100~999 사이 랜덤 출석 코드
        String attendanceCode = String.valueOf(new Random().nextInt(900) + 100);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = now.plusMinutes(dto.getDurationMinutes());

        // 세션 생성 및 저장
        AttendanceSession session = AttendanceSession.builder()
                .lecture(lecture)
                .attendanceCode(attendanceCode)
                .status(SessionStatus.ACTIVE)
                .startTime(now)
                .endTime(endTime)
                .build();

        sessionRepository.save(session);

        return CodeCreateResponseDto.builder()
                .lectureId(lectureId)
                .attendanceCode(attendanceCode)
                .status("ACTIVE")
                .endTime(endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }

    /**
     * 교수 : 출석 종료
     * */
    @Transactional
    public AttendanceEndResponseDto endAttendanceSession(AttendanceEndRequestDto requestDto) {
        Lecture lecture = lectureRepository.findById(requestDto.getLectureId()).get();
        AttendanceSession session = sessionRepository.findByLectureAndStatus(lecture, SessionStatus.ACTIVE).get();

        // 상태 변경
        session.setStatus(SessionStatus.CLOSED);
        session.setEndTime(LocalDateTime.now());
        sessionRepository.save(session);

        // DTO 변환
        return AttendanceEndResponseDto.builder()
                .lectureId(session.getLecture().getId())
                .status(session.getStatus().toString())
                .endTime(session.getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }

}
