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
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found: " + lectureId));

        // 이미 ACTIVE 상태인 세션이 있는지 확인
        if (sessionRepository.findByLectureAndStatus(lecture, SessionStatus.ACTIVE).isPresent()) {
            throw new RuntimeException("이미 진행 중인 출석 세션이 있습니다. 기존 세션을 먼저 종료해주세요.");
        }

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

    /**
     * 모든 좌석 출석 상태 불러오기
     * */
    public List<SeatAttendanceStatusDto> getLectureRoomStatus(Long lectureId) {
        // 1. 전체 좌석 불러오기
        List<Seat> seats = seatRepository.findAllByLectureId(lectureId);

        // 2. 해당 강의의 모든 출석 기록 불러오기
        List<AttendanceRecord> attendanceRecords = recordRepository.findAllByLectureId(lectureId);

        // 3. 출석한 좌석 ID → 출석 상태 맵핑
        Map<Long, String> attendanceMap = attendanceRecords.stream()
                .collect(Collectors.toMap(
                        record -> record.getSeat().getId(),
                        record -> record.getStatus().name(),
                        (oldValue, newValue) -> newValue // 중복시 마지막 값 유지
                ));

        // 4. 좌석 + 출석상태 합쳐서 DTO로 변환
        return seats.stream()
                .map(seat -> SeatAttendanceStatusDto.builder()
                        .seatId(seat.getId())
                        .row(seat.getRowNumber())
                        .col(seat.getColNumber())
                        .attendanceStatus(attendanceMap.getOrDefault(seat.getId(), "ABSENT"))
                        .build())
                .toList();
    }

    /**
     * 특정좌석의 학생정보 가져오기
     * */
    public SeatStudentDetailDto getSeatStudentDetail(Long lectureId, Long seatId) {
        // 1. 좌석 정보 가져오기
        Seat seat = seatRepository.findById(seatId).orElseThrow();

        // 2. 해당 좌석에 출석한 기록 찾기 (가장 최근)
        AttendanceRecord record = recordRepository.findByLectureIdAndSeatId(lectureId, seatId)
                .orElse(null);

        if (record == null || record.getUser() == null) {
            return SeatStudentDetailDto.builder()
                    .name(null)
                    .studentId(null)
                    .grade(0)
                    .profileImageUrl(null)
                    .attendanceStatus("ABSENT")
                    .build();
        }

        // 3. 학생 정보 DTO로 반환
        User user = record.getUser();
        Integer grade = user.getGrade();

        return SeatStudentDetailDto.builder()
                .name(user.getUsername())
                .studentId(user.getLoginId())
                .grade(grade != null ? grade : 0)
                .profileImageUrl(user.getProfileImageUrl())
                .attendanceStatus(record.getStatus().name())
                .build();
    }
}
