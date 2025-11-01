package org.example.nextchallenge.seat.repository;

import org.example.nextchallenge.lecture.entity.Lecture;
import org.example.nextchallenge.seat.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Seat findByLectureAndRowNumberAndColNumber(Lecture lecture, int rowNumber, int colNumber);

}
