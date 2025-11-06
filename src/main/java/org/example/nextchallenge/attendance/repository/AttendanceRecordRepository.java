package org.example.nextchallenge.attendance.repository;

import org.example.nextchallenge.attendance.entity.AttendanceRecord;
import org.example.nextchallenge.attendance.entity.AttendanceSession;
import org.example.nextchallenge.lecture.entity.Lecture;
import org.example.nextchallenge.seat.entity.Seat;
import org.example.nextchallenge.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    Optional<AttendanceRecord> findByLectureIdAndUserId(Long lectureId, Long userId);
    List<AttendanceRecord> findAllBySessionId(Long sessionId);
    List<AttendanceRecord> findAllByLectureId(Long lectureId);
    Optional<AttendanceRecord> findByLectureIdAndSeatId(Long lectureId, Long seatId);
    Optional<AttendanceRecord> findByLectureAndUserAndSession(Lecture lecture, User user, AttendanceSession session);
}
