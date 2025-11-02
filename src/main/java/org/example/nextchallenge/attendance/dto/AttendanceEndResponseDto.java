package org.example.nextchallenge.attendance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttendanceEndResponseDto {
    private Long lectureId;
    private String status;
    private String endTime;
}
