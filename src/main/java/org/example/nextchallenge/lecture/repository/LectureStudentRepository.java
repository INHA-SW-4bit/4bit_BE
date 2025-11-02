package org.example.nextchallenge.lecture.repository;

import org.example.nextchallenge.lecture.entity.LectureStudent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureStudentRepository extends JpaRepository<LectureStudent, Long> {
    List<LectureStudent> findByStudent_Id(Long studentId);
    List<LectureStudent> findByLecture_Id(Long lectureId);
}
