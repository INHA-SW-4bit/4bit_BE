package org.example.nextchallenge.attendance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeatStudentDetailDto {
    private String name;
    private String studentId;
    private int grade;
    private String profileImageUrl;
    private String attendanceStatus; // ATTENDED, LATE, ABSENT
}
