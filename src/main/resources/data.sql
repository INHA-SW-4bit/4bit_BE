INSERT INTO next_challenge_local.users (grade,created_at,english_name,login_id,username,department,password,profile_image_url,`role`) VALUES
	 (NULL,'2025-11-06 00:53:28','Inha Kim','20230724','김인하',NULL,'1234',NULL,'PROFESSOR'),
	 (2,'2025-11-06 00:53:28','Indeok','12223757','인덕이','컴퓨터공학과','1234',NULL,'STUDENT'),
	 (3,'2025-11-06 00:53:28','Innyong','12223777','인뇽이','컴퓨터공학과','1234',NULL,'STUDENT'),
	 (1,'2025-11-06 00:53:28','Kim Minji','12180001','김민지','컴퓨터공학과','1234',NULL,'STUDENT'),
	 (2,'2025-11-06 00:53:28','Park Jisoo','12190002','박지수','소프트웨어학과','1234',NULL,'STUDENT'),
	 (3,'2025-11-06 00:53:28','Lee Junho','12200003','이준호','정보통신공학과','1234',NULL,'STUDENT'),
	 (4,'2025-11-06 00:53:28','Choi Yuna','12210004','최유나','전자공학과','1234',NULL,'STUDENT'),
	 (1,'2025-11-06 00:53:28','Han Seojin','12220005','한서진','데이터사이언스학과','1234',NULL,'STUDENT'),
	 (2,'2025-11-06 00:53:28','Jung Taeyang','12230006','정태양','기계공학과','1234',NULL,'STUDENT'),
	 (3,'2025-11-06 00:53:28','Yoon Haerin','12240007','윤해린','산업경영공학과','1234',NULL,'STUDENT'),
	 (4,'2025-11-06 00:53:28','Kang Dohyun','12250008','강도현','인공지능학과','1234',NULL,'STUDENT');



-- Classroom 먼저 생성
INSERT INTO classroom (id, layout_json)
VALUES (
  1,
  '[
    ["seat","seat","seat","seat","aisle","seat","seat","seat","seat","seat","aisle","seat","seat"],
        ["seat","seat","seat","seat","aisle","seat","seat","seat","seat","seat","aisle","seat","seat"],
        ["seat","seat","seat","seat","aisle","seat","seat","seat","seat","seat","aisle","seat","seat"],
        ["seat","seat","seat","seat","aisle","seat","seat","seat","seat","seat","aisle","seat","X"],
        ["X","seat","seat","seat","aisle","seat","seat","seat","seat","X","aisle","seat","seat"]
  ]'
);

-- Lecture가 Classroom을 참조하도록 classroom_id 추가
INSERT INTO lectures (id, lecture_name, professor_id, classroom_id, joined_at)
VALUES (1, '자료구조', 1, 1, NOW());

INSERT INTO lecture_students (id, lecture_id, student_id, joined_at, status)
VALUES
    (1, 1, 2, NOW(), 'ACTIVE'),
    (2, 1, 3, NOW(), 'ACTIVE'),
    (3, 1, 4, NOW(), 'ACTIVE'),
    (4, 1, 5, NOW(), 'ACTIVE'),
    (5, 1, 6, NOW(), 'ACTIVE'),
    (6, 1, 7, NOW(), 'ACTIVE'),
    (7, 1, 8, NOW(), 'ACTIVE'),
    (8, 1, 9, NOW(), 'ACTIVE'),
    (9, 1, 10, NOW(), 'ACTIVE'),
    (10, 1, 11, NOW(), 'ACTIVE');



-- Lecture 1번 강의실 좌석 초기화
INSERT INTO seats (lecture_id, seat_row_number, seat_col_number, occupied, student_id)
VALUES
(1, 0, 1, 0, NULL),
(1, 0, 2, 0, NULL),
(1, 0, 3, 0, NULL),
(1, 0, 4, 0, NULL),
(1, 0, 5, 0, NULL),
(1, 0, 6, 0, NULL),
(1, 0, 7, 0, NULL),
(1, 0, 8, 0, NULL),
(1, 0, 9, 0, NULL),
(1, 0, 10, 0, NULL),
(1, 0, 11, 0, NULL),
(1, 0, 12, 0, NULL),
(1, 1, 0, 0, NULL),
(1, 1, 1, 0, NULL),
(1, 1, 2, 0, NULL),
(1, 1, 3, 0, NULL),
(1, 1, 4, 0, NULL),
(1, 1, 5, 0, NULL),
(1, 1, 6, 0, NULL),
(1, 1, 7, 0, NULL),
(1, 1, 8, 0, NULL),
(1, 1, 9, 0, NULL),
(1, 1, 10, 0, NULL),
(1, 1, 11, 0, NULL),
(1, 1, 12, 0, NULL),
(1, 2, 0, 0, NULL),
(1, 2, 1, 0, NULL),
(1, 2, 2, 0, NULL),
(1, 2, 3, 0, NULL),
(1, 2, 4, 0, NULL),
(1, 2, 5, 0, NULL),
(1, 2, 6, 0, NULL),
(1, 2, 7, 0, NULL),
(1, 2, 8, 0, NULL),
(1, 2, 9, 0, NULL),
(1, 2, 10, 0, NULL),
(1, 2, 11, 0, NULL),
(1, 2, 12, 0, NULL),
(1, 3, 0, 0, NULL),
(1, 3, 1, 0, NULL),
(1, 3, 2, 0, NULL),
(1, 3, 3, 0, NULL),
(1, 3, 4, 0, NULL),
(1, 3, 5, 0, NULL),
(1, 3, 6, 0, NULL),
(1, 3, 7, 0, NULL),
(1, 3, 8, 0, NULL),
(1, 3, 9, 0, NULL),
(1, 3, 10, 0, NULL),
(1, 3, 11, 0, NULL),
(1, 3, 12, 0, NULL),
(1, 4, 0, 0, NULL),
(1, 4, 1, 0, NULL),
(1, 4, 2, 0, NULL),
(1, 4, 3, 0, NULL),
(1, 4, 4, 0, NULL),
(1, 4, 5, 0, NULL),
(1, 4, 6, 0, NULL),
(1, 4, 7, 0, NULL),
(1, 4, 8, 0, NULL),
(1, 4, 9, 0, NULL),
(1, 4, 10, 0, NULL),
(1, 4, 11, 0, NULL),
(1, 4, 12, 0, NULL);
