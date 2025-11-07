package org.example.nextchallenge.classroom.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 강의실의 좌석 배치 정보만 저장
    @Lob
    @Column(columnDefinition = "TEXT")
    private String layoutJson;
}
