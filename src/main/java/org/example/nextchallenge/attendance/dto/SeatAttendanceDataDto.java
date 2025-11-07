package org.example.nextchallenge.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 출석 좌석 + 학생 정보를 묶는 DTO
 * 기존 SeatAttendanceStatusDto + SeatStudentDetailDto 조합
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatAttendanceDataDto {
    private SeatAttendanceStatusDto seat;
    private SeatStudentDetailDto student;
}
