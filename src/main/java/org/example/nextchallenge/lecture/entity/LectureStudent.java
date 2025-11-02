package org.example.nextchallenge.lecture.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.nextchallenge.user.entity.User;

@Entity
@Table(name = "lecture_students")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 강의
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    // 학생
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    // 수강신청일
    @Builder.Default
    @Column(nullable = false)
    private java.time.LocalDateTime joinedAt = java.time.LocalDateTime.now();


    // 상태 (추후 확장용)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private LectureStatus status = LectureStatus.ACTIVE;

    public enum LectureStatus {
        ACTIVE, DROPPED
    }
}
