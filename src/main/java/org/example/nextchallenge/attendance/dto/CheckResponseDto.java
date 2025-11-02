package org.example.nextchallenge.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CheckResponseDto {
    private boolean success;
    private String message;
    private int RowNumber;
    private int ColNumber;
    private LocalDateTime attendanceTime;
}
