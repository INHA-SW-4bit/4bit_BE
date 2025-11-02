package org.example.nextchallenge.attendance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeatAttendanceStatusDto {
    private Long seatId;
    private int row;
    private int col;
    private String attendanceStatus; // ATTENDED, LATE, ABSENT 그대로 문자열로 사용
}