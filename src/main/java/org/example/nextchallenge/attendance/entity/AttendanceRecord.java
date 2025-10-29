package org.example.nextchallenge.attendance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.nextchallenge.lecture.entity.Lecture;
import org.example.nextchallenge.seat.entity.Seat;
import org.example.nextchallenge.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_records")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 출석한 학생
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 어떤 강의의 출석인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    // 어떤 세션(출석 코드)에 해당하는지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private AttendanceSession session;

    // 선택한 좌석 (nullable)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AttendanceStatus status = AttendanceStatus.ATTENDED; // ATTENDED / LATE / ABSENT

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();
}