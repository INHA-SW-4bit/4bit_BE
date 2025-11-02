package org.example.nextchallenge.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CodeCreateRequestDto {
    private Long lectureId;        // 출석 세션을 시작할 강의 ID
    private Integer durationMinutes; // 출석 유효 시간 (분)
}
