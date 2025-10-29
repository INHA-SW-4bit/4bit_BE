package org.example.nextchallenge.lecture.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.nextchallenge.attendance.entity.AttendanceRecord;
import org.example.nextchallenge.attendance.entity.AttendanceSession;
import org.example.nextchallenge.seat.entity.Seat;
import org.example.nextchallenge.user.entity.User;

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

    // 교수 한 명이 여러 강의 담당 → N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private User professor;

    // 강의에 속한 출석 기록
    @Builder.Default
    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttendanceRecord> attendanceRecords = new ArrayList<>();

    // 강의에 속한 출석 세션
    @Builder.Default
    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttendanceSession> attendanceSessions = new ArrayList<>();

    // 강의에 속한 좌석
    @Builder.Default
    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats = new ArrayList<>();
}
