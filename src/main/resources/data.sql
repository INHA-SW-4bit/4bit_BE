INSERT INTO users (id, login_id, username, english_name, password, role, grade, department, profile_image_url, created_at)
VALUES
    (1, '20230724', '김인하', 'Inha Kim', '1234', 'PROFESSOR', NULL, NULL, NULL, NOW()),
    (2, '12223757', '인덕이', 'Indeok', '1234', 'STUDENT', 2, '컴퓨터공학과', NULL, NOW()),
    (3, '12223777', '인뇽이', 'Innyong', '1234', 'STUDENT', 3, '컴퓨터공학과', NULL, NOW());

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
    (2, 1, 3, NOW(), 'ACTIVE');

-- Lecture 1번 강의실 좌석 초기화
INSERT INTO seats (id, lecture_id, seat_row_number, seat_col_number, occupied)
VALUES
    -- 1행
    (1, 1, 1, 1, 0),
    (2, 1, 1, 2, 0),
    (3, 1, 1, 3, 0),
    (4, 1, 1, 4, 0),
    (5, 1, 1, 6, 0),
    (6, 1, 1, 7, 0),
    (7, 1, 1, 8, 0),
    (8, 1, 1, 9, 0),
    (9, 1, 1, 10, 0),
    (10, 1, 1, 12, 0),
    (11, 1, 1, 13, 0),

    -- 2행
    (12, 1, 2, 1, 0),
    (13, 1, 2, 2, 0),
    (14, 1, 2, 3, 0),
    (15, 1, 2, 4, 0),
    (16, 1, 2, 6, 0),
    (17, 1, 2, 7, 0),
    (18, 1, 2, 8, 0),
    (19, 1, 2, 9, 0),
    (20, 1, 2, 10, 0),
    (21, 1, 2, 12, 0),
    (22, 1, 2, 13, 0),

    -- 3행
    (23, 1, 3, 1, 0),
    (24, 1, 3, 2, 0),
    (25, 1, 3, 3, 0),
    (26, 1, 3, 4, 0),
    (27, 1, 3, 6, 0),
    (28, 1, 3, 7, 0),
    (29, 1, 3, 8, 0),
    (30, 1, 3, 9, 0),
    (31, 1, 3, 10, 0),
    (32, 1, 3, 12, 0),
    (33, 1, 3, 13, 0),

    -- 4행
    (34, 1, 4, 1, 0),
    (35, 1, 4, 2, 0),
    (36, 1, 4, 3, 0),
    (37, 1, 4, 4, 0),
    (38, 1, 4, 6, 0),
    (39, 1, 4, 7, 0),
    (40, 1, 4, 8, 0),
    (41, 1, 4, 9, 0),
    (42, 1, 4, 10, 0),
    (43, 1, 4, 12, 0),

    -- 5행
    (44, 1, 5, 2, 0),
    (45, 1, 5, 3, 0),
    (46, 1, 5, 4, 0),
    (47, 1, 5, 6, 0),
    (48, 1, 5, 7, 0),
    (49, 1, 5, 8, 0),
    (50, 1, 5, 9, 0),
    (51, 1, 5, 12, 0),
    (52, 1, 5, 13, 0);
