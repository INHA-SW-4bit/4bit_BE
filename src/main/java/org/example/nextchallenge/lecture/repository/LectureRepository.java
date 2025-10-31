package org.example.nextchallenge.lecture.repository;

import org.example.nextchallenge.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findByProfessorId(Long professorId); // 교수용
    List<Lecture> findByAttendanceRecords_User_Id(Long userId); // 학생용
}
