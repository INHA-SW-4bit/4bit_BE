package org.example.nextchallenge.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 강의실 좌석 배치 + 출석 완료 좌석 학생 정보 전체 응답
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureAttendanceResponseDto {
    private List<List<String>> layout; // layoutJson 파싱 결과
    private List<SeatAttendanceDataDto> attendanceData; // 출석한 학생 좌석 정보
}
