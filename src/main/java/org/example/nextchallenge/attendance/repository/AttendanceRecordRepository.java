package org.example.nextchallenge.attendance.repository;

import org.example.nextchallenge.attendance.entity.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    Optional<AttendanceRecord> findByLectureIdAndUserId(Long lectureId, Long userId);
    List<AttendanceRecord> findAllBySessionId(Long sessionId);
}
