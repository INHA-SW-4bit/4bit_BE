package org.example.nextchallenge.lecture.service;

import lombok.RequiredArgsConstructor;
import org.example.nextchallenge.lecture.dto.LectureListResponseDto;
import org.example.nextchallenge.lecture.entity.Lecture;
import org.example.nextchallenge.lecture.repository.LectureRepository;
import org.example.nextchallenge.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;

    // 로그인한 유저의 역할에 따라 강의 목록 조회
    public LectureListResponseDto getLecturesByUser(Long userId, String role) {
        List<Lecture> lectures;

        if ("PROFESSOR".equalsIgnoreCase(role)) {
            // 교수: 자신이 담당하는 강의들
            lectures = lectureRepository.findByProfessorId(userId);
        } else {
            // 학생: 수강 중인 강의들
            lectures = lectureRepository.findByAttendanceRecords_User_Id(userId);
        }

        return LectureListResponseDto.builder()
                .lectures(
                        lectures.stream()
                                .map(l -> LectureListResponseDto.LectureItem.builder()
                                        .lectureId(l.getId())
                                        .lectureName(l.getLectureName())
                                        .professorName(l.getProfessor().getUsername())
                                        .build())
                                .collect(Collectors.toList())
                )
                .build();
    }
}
