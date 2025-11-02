INSERT INTO users (id, login_id, username, password, role, grade, profile_image_url, created_at)
VALUES
    (1, '20230724', '서영덕', '1234', 'PROFESSOR', NULL, NULL, NOW()),
    (2, '12223757', '인덕이', '1234', 'STUDENT', 2, NULL, NOW());

INSERT INTO lectures (id, lecture_name, professor_id, joined_at)
VALUES (1, '자료구조', 1, NOW());

INSERT INTO lecture_students (id, lecture_id, student_id, joined_at, status)
VALUES (1, 1, 2, NOW(), 'ACTIVE');

INSERT INTO seats (id, lecture_id, seat_row_number, seat_col_number, occupied, student_id)
VALUES
    (1, 1, 1, 1, 0, 2),
    (2, 1, 2, 1, 0, 2);