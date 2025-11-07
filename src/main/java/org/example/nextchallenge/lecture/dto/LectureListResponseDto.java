package org.example.nextchallenge.lecture.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LectureListResponseDto {
    private List<LectureItem> lectures;

    @Getter
    @Builder
    public static class LectureItem {
        private Long lectureId;
        private String lectureName;
        private String professorName;
    }
}
