package org.example.nextchallenge.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor

public class CheckRequestDto {
    private int rowNumber;
    private int colNumber;
    private String attendanceCode;
}

