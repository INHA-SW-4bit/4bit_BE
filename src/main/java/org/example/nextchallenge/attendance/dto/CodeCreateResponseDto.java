package org.example.nextchallenge.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CodeCreateResponseDto {
    private Long lectureId;        // 어떤 강의의 코드인지
    private String attendanceCode; // 랜덤 100~999 출석 코드
    private String status;         // ACTIVE / ENDED
    private String startTime;
    private String endTime;        // ISO-8601 형식의 종료시각
}

