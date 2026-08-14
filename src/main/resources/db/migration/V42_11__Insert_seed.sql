
INSERT INTO account (email, password, role) VALUES
                                                ('admin@hei.school', 'admin123', 'ADMIN'),
                                                ('teacher1@hei.school', 'teacher123', 'TEACHER'),
                                                ('teacher2@hei.school', 'teacher123', 'TEACHER'),
                                                ('student1@hei.school', 'student123', 'STUDENT'),
                                                ('student2@hei.school', 'student123', 'STUDENT'),
                                                ('student3@hei.school', 'student123', 'STUDENT');



INSERT INTO promotion (label, start_year) VALUES
                                              ('Promotion K', '2025-01-01'),
                                              ('Promotion L', '2026-01-01');


INSERT INTO group_ (ref, promotion_id)
SELECT 'K2', id FROM promotion WHERE label = 'Promotion K';

INSERT INTO group_ (ref, promotion_id)
SELECT 'K3', id FROM promotion WHERE label = 'Promotion K';

INSERT INTO group_ (ref, promotion_id)
SELECT 'L1', id FROM promotion WHERE label = 'Promotion L';


INSERT INTO semester (label, "order", promotion_id)
SELECT 'S1', 1, id FROM promotion WHERE label = 'Promotion K';

INSERT INTO semester (label, "order", promotion_id)
SELECT 'S2', 2, id FROM promotion WHERE label = 'Promotion K';

INSERT INTO semester (label, "order", promotion_id)
SELECT 'S1', 1, id FROM promotion WHERE label = 'Promotion L';


INSERT INTO teacher (name, account_id)
SELECT 'Rakoto', id FROM account
WHERE email = 'teacher1@hei.school';

INSERT INTO teacher (name, account_id)
SELECT 'Rabe', id FROM account
WHERE email = 'teacher2@hei.school';

INSERT INTO student (name, first_name, ref, account_id, promotion_id)
SELECT
    'Andriamihaja',
    'Jean',
    'STD001',
    a.id,
    p.id
FROM account a, promotion p
WHERE a.email = 'student1@hei.school'
  AND p.label = 'Promotion K';

INSERT INTO student (name, first_name, ref, account_id, promotion_id)
SELECT
    'Rakotomalala',
    'Paul',
    'STD002',
    a.id,
    p.id
FROM account a, promotion p
WHERE a.email = 'student2@hei.school'
  AND p.label = 'Promotion K';

INSERT INTO student (name, first_name, ref, account_id, promotion_id)
SELECT
    'Randria',
    'Marie',
    'STD003',
    a.id,
    p.id
FROM account a, promotion p
WHERE a.email = 'student3@hei.school'
  AND p.label = 'Promotion L';

INSERT INTO course (ref, title, credits) VALUES
                                             ('PROG3', 'Programmation 3', 6),
                                             ('DB1', 'Base de données 1', 5),
                                             ('WEB2', 'Développement Web 2', 4);



INSERT INTO course_assignment
(course_id, teacher_id, group_id, semester_id)

SELECT
    c.id,
    t.id,
    g.id,
    s.id
FROM course c
         JOIN teacher t ON t.name = 'Rakoto'
         JOIN group_ g ON g.ref = 'K2'
         JOIN semester s ON s.label = 'S1'
         JOIN promotion p ON p.id = s.promotion_id
WHERE c.ref = 'PROG3'
  AND p.label = 'Promotion K';


INSERT INTO course_assignment
(course_id, teacher_id, group_id, semester_id)

SELECT
    c.id,
    t.id,
    g.id,
    s.id
FROM course c
         JOIN teacher t ON t.name = 'Rabe'
         JOIN group_ g ON g.ref = 'K2'
         JOIN semester s ON s.label = 'S1'
         JOIN promotion p ON p.id = s.promotion_id
WHERE c.ref = 'DB1'
  AND p.label = 'Promotion K';


INSERT INTO exam (date_exam, coefficient, course_assignment_id)

SELECT
    '2026-03-15 09:00:00',
    2.00,
    ca.id
FROM course_assignment ca
         JOIN course c ON c.id = ca.course_id
WHERE c.ref = 'PROG3';


INSERT INTO exam (date_exam, coefficient, course_assignment_id)

SELECT
    '2026-03-20 09:00:00',
    2.00,
    ca.id
FROM course_assignment ca
         JOIN course c ON c.id = ca.course_id
WHERE c.ref = 'DB1';



INSERT INTO grade (value, student_id, exam_id)

SELECT
    15.50,
    s.id,
    e.id
FROM student s
         JOIN account a ON a.id = s.account_id
         JOIN exam e ON e.date_exam = '2026-03-15 09:00:00'
WHERE a.email = 'student1@hei.school';


INSERT INTO grade (value, student_id, exam_id)

SELECT
    13.00,
    s.id,
    e.id
FROM student s
         JOIN account a ON a.id = s.account_id
         JOIN exam e ON e.date_exam = '2026-03-15 09:00:00'
WHERE a.email = 'student2@hei.school';


INSERT INTO grade (value, student_id, exam_id)

SELECT
    17.00,
    s.id,
    e.id
FROM student s
         JOIN account a ON a.id = s.account_id
         JOIN exam e ON e.date_exam = '2026-03-20 09:00:00'
WHERE a.email = 'student1@hei.school';


INSERT INTO student_group_history
(start_date, end_date, student_id, group_id)

SELECT
    '2026-01-10',
    NULL,
    s.id,
    g.id
FROM student s
         JOIN account a ON a.id = s.account_id
         JOIN group_ g ON g.ref = 'K2'
WHERE a.email = 'student1@hei.school';


INSERT INTO student_group_history
(start_date, end_date, student_id, group_id)

SELECT
    '2026-01-10',
    NULL,
    s.id,
    g.id
FROM student s
         JOIN account a ON a.id = s.account_id
         JOIN group_ g ON g.ref = 'K2'
WHERE a.email = 'student2@hei.school';