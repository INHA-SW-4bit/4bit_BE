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
    ["seat","seat","aisle","seat"],
    ["seat","seat","aisle","seat"],
    ["seat","X","aisle","seat"]
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
    (1, 1, 1, 1, 0),
    (2, 1, 1, 2, 0),
    (3, 1, 1, 4, 0),

    (4, 1, 2, 1, 0),
    (5, 1, 2, 2, 0),
    (6, 1, 2, 4, 0),

    (7, 1, 3, 1, 0),
    (8, 1, 3, 4, 0);
