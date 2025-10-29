package org.example.nextchallenge.attendance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.nextchallenge.lecture.entity.Lecture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attendance_sessions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class    AttendanceSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 강의의 세션인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @Column(name = "attendance_code", nullable = false, length = 3)
    private String attendanceCode; // 출석 코드 (3자리)

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SessionStatus status = SessionStatus.ACTIVE; // ACTIVE / CLOSED

    @Builder.Default
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime = LocalDateTime.now();

    @Builder.Default
    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes = 2;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    // 세션에 속한 출석 기록
    @Builder.Default
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttendanceRecord> attendanceRecords = new ArrayList<>();
}
