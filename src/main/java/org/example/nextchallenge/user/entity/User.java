package org.example.nextchallenge.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.nextchallenge.lecture.entity.LectureStudent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false, unique = true, length = 50)
    private String loginId; // 학번 or 교수번호

    @Column(nullable = false, length = 50)
    private String username; // 이름

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role; // STUDENT or PROFESSOR

    private Integer grade;

    @Column(name = "profile_image_url", length = 255)
    private String profileImageUrl;

    @Builder.Default
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    //  학생이 수강 중인 강의 목록 (LectureStudent 통해 연결)
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LectureStudent> lectureStudents = new ArrayList<>();
}
