package org.example.nextchallenge.seat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.nextchallenge.attendance.entity.AttendanceRecord;
import org.example.nextchallenge.lecture.entity.Lecture;
import org.example.nextchallenge.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seats")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 강의의 좌석인지 (강의 1:N 좌석)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @Column(name = "row_number", nullable = false)
    private int rowNumber;

    @Column(name = "col_number", nullable = false)
    private int colNumber;

    @Builder.Default
    @Column(nullable = false)
    private boolean occupied = false;

    // 학생이 해당 좌석에 배정되었을 경우
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User student;

    // 좌석에 대한 출석 기록
    @Builder.Default
    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttendanceRecord> attendanceRecords = new ArrayList<>();
}