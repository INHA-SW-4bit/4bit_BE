package org.example.nextchallenge.attendance.repository;

import org.example.nextchallenge.attendance.entity.AttendanceSession;
import org.example.nextchallenge.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.example.nextchallenge.attendance.entity.SessionStatus;

import java.util.Optional;

public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long> {
    Optional<AttendanceSession> findByLectureAndAttendanceCode(Lecture lecture, String attendanceCode);
    Optional<AttendanceSession> findByLectureAndStatus(Lecture lecture, SessionStatus status);
}
