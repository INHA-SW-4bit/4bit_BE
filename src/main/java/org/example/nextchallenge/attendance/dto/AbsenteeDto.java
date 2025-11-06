package org.example.nextchallenge.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbsenteeDto {
    private String department;     // 학과
    private int grade;             // 학년
    private String studentNumber;  // 학번
    private String name;           // 이름
    private String englishName;    // 영문명
}
