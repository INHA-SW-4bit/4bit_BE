package org.example.nextchallenge.lecture.repository;

import org.example.nextchallenge.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    // 교수가 담당하는 강의 조회
    List<Lecture> findByProfessorId(Long professorId);
}
