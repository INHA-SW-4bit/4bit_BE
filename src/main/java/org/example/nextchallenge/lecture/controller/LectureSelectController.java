package org.example.nextchallenge.lecture.controller;

import lombok.RequiredArgsConstructor;
import org.example.nextchallenge.lecture.dto.LectureListResponseDto;
import org.example.nextchallenge.lecture.service.LectureService;
import org.example.nextchallenge.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lectures")
public class LectureSelectController {

    private final LectureService lectureService;

    // 내 강의 목록 조회 (학생/교수 공용)
    @GetMapping("/list")
    public LectureListResponseDto getMyLectures(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        String role = userDetails.getRole().name();

        // service 내부에서 findByStudents_Id()로 바뀜
        return lectureService.getLecturesByUser(userId, role);
    }
}
