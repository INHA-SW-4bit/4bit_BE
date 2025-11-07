package org.example.nextchallenge.lecture.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.nextchallenge.attendance.entity.AttendanceRecord;
import org.example.nextchallenge.attendance.entity.AttendanceSession;
import org.example.nextchallenge.classroom.entity.Classroom;
import org.example.nextchallenge.seat.entity.Seat;
import org.example.nextchallenge.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lectures")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lecture_name", nullable = false, length = 50)
    private String lectureName;

    //  교수 한 명이 여러 강의 담당 → N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private User professor;

    // 학생 등록 관계 (LectureStudent를 통해 N:M 관리)
    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LectureStudent> lectureStudents = new ArrayList<>();

    //  출석 기록
    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AttendanceRecord> attendanceRecords = new ArrayList<>();

    //  출석 세션
    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AttendanceSession> attendanceSessions = new ArrayList<>();

    // 강의 좌석
    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Seat> seats = new ArrayList<>();

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id") // ✅ Classroom.id를 참조
    private Classroom classroom;
}
