package org.example.nextchallenge.attendance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentSessionResponseDto {
    private String status;     // ACTIVE / CLOSED
    private String endTime;    // ISO-8601 형식
}
