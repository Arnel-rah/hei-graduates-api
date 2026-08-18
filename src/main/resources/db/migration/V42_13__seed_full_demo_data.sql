DO $$
    DECLARE
        v_admin_id VARCHAR;
        v_teacher1_acc_id VARCHAR;
        v_teacher2_acc_id VARCHAR;
        v_teacher3_acc_id VARCHAR;
        v_teacher4_acc_id VARCHAR;
        v_student1_acc_id VARCHAR;
        v_student2_acc_id VARCHAR;
        v_student3_acc_id VARCHAR;
        v_student4_acc_id VARCHAR;
        v_student5_acc_id VARCHAR;
        v_student6_acc_id VARCHAR;
        v_student7_acc_id VARCHAR;
        v_student8_acc_id VARCHAR;
        v_student9_acc_id VARCHAR;
        v_student10_acc_id VARCHAR;
        v_student11_acc_id VARCHAR;
        v_student12_acc_id VARCHAR;
        v_promotion_id VARCHAR;
        v_academic_year_id VARCHAR;
        v_group_tn_id VARCHAR;
        v_group_el_id VARCHAR;
        v_group_ia_id VARCHAR;
        v_semester_id VARCHAR;
        v_course_reseaux_id VARCHAR;
        v_course_anglais_id VARCHAR;
        v_course_math_id VARCHAR;
        v_course_bdd_id VARCHAR;
        v_course_algo_id VARCHAR;
        v_course_web_id VARCHAR;
        v_teacher1_id VARCHAR;
        v_teacher2_id VARCHAR;
        v_teacher3_id VARCHAR;
        v_teacher4_id VARCHAR;
        v_student1_id VARCHAR;
        v_student2_id VARCHAR;
        v_student3_id VARCHAR;
        v_student4_id VARCHAR;
        v_student5_id VARCHAR;
        v_student6_id VARCHAR;
        v_student7_id VARCHAR;
        v_student8_id VARCHAR;
        v_student9_id VARCHAR;
        v_student10_id VARCHAR;
        v_student11_id VARCHAR;
        v_student12_id VARCHAR;
        v_ca_reseaux_tn_id VARCHAR;
        v_ca_anglais_tn_id VARCHAR;
        v_ca_math_tn_id VARCHAR;
        v_ca_bdd_tn_id VARCHAR;
        v_ca_reseaux_el_id VARCHAR;
        v_ca_algo_el_id VARCHAR;
        v_ca_web_el_id VARCHAR;
        v_ca_reseaux_ia_id VARCHAR;
        v_ca_math_ia_id VARCHAR;
        v_ca_algo_ia_id VARCHAR;
        v_exam_reseaux_tn_id VARCHAR;
        v_exam_anglais_tn_id VARCHAR;
        v_exam_math_tn_id VARCHAR;
        v_exam_bdd_tn_id VARCHAR;
        v_exam_reseaux_el_id VARCHAR;
        v_exam_algo_el_id VARCHAR;
        v_exam_web_el_id VARCHAR;
        v_exam_reseaux_ia_id VARCHAR;
        v_exam_math_ia_id VARCHAR;
        v_exam_algo_ia_id VARCHAR;
        v_password_hash TEXT := '$2b$10$8gNwzsYjuKcwIAyNNG8FPOBmDv2YCXdCiAN.FQnkVI6vOZ5LPPira';
    BEGIN
        INSERT INTO account (email, password, role)
        VALUES ('demo-admin@hei.mg', v_password_hash, 'ADMIN')
        RETURNING id INTO v_admin_id;

        INSERT INTO account (email, password, role)
        VALUES ('rasoa-demo@hei.mg', v_password_hash, 'TEACHER')
        RETURNING id INTO v_teacher1_acc_id;

        INSERT INTO account (email, password, role)
        VALUES ('rakoto-demo@hei.mg', v_password_hash, 'TEACHER')
        RETURNING id INTO v_teacher2_acc_id;

        INSERT INTO account (email, password, role)
        VALUES ('andry-demo@hei.mg', v_password_hash, 'TEACHER')
        RETURNING id INTO v_teacher3_acc_id;

        INSERT INTO account (email, password, role)
        VALUES ('soa-demo@hei.mg', v_password_hash, 'TEACHER')
        RETURNING id INTO v_teacher4_acc_id;

        INSERT INTO account (email, password, role)
        VALUES ('sadiarnel145@gmail.com', v_password_hash, 'STUDENT')
        RETURNING id INTO v_student1_acc_id;

        INSERT INTO account (email, password, role)
        VALUES ('faniry-demo@hei.mg', v_password_hash, 'STUDENT')
        RETURNING id INTO v_student2_acc_id;

        INSERT INTO account (email, password, role)
        VALUES ('echec-demo@hei.mg', v_password_hash, 'STUDENT')
        RETURNING id INTO v_student3_acc_id;

        INSERT INTO account (email, password, role)
        VALUES ('hery-demo@hei.mg', v_password_hash, 'STUDENT')
        RETURNING id INTO v_student4_acc_id;

        INSERT INTO account (email, password, role)
        VALUES ('malala-demo@hei.mg', v_password_hash, 'STUDENT')
        RETURNING id INTO v_student5_acc_id;

        INSERT INTO account (email, password, role)
        VALUES ('tojo-demo@hei.mg', v_password_hash, 'STUDENT')
        RETURNING id INTO v_student6_acc_id;

        INSERT INTO account (email, password, role)
        VALUES ('nomena-demo@hei.mg', v_password_hash, 'STUDENT')
        RETURNING id INTO v_student7_acc_id;

        INSERT INTO account (email, password, role)
        VALUES ('lala-demo@hei.mg', v_password_hash, 'STUDENT')
        RETURNING id INTO v_student8_acc_id;

        INSERT INTO account (email, password, role)
        VALUES ('rija-demo@hei.mg', v_password_hash, 'STUDENT')
        RETURNING id INTO v_student9_acc_id;

        INSERT INTO account (email, password, role)
        VALUES ('feno-demo@hei.mg', v_password_hash, 'STUDENT')
        RETURNING id INTO v_student10_acc_id;

        INSERT INTO account (email, password, role)
        VALUES ('sara-demo@hei.mg', v_password_hash, 'STUDENT')
        RETURNING id INTO v_student11_acc_id;

        INSERT INTO account (email, password, role)
        VALUES ('kanto-demo@hei.mg', v_password_hash, 'STUDENT')
        RETURNING id INTO v_student12_acc_id;

        INSERT INTO promotion (label, start_year)
        VALUES ('Promotion Demo 2024-2027', '2024-09-01')
        RETURNING id INTO v_promotion_id;

        INSERT INTO academic_year (label, "order", promotion_id, published)
        VALUES ('L2', 2, v_promotion_id, false)
        RETURNING id INTO v_academic_year_id;

        INSERT INTO group_ (ref, promotion_id)
        VALUES ('TN', v_promotion_id)
        RETURNING id INTO v_group_tn_id;

        INSERT INTO group_ (ref, promotion_id)
        VALUES ('EL', v_promotion_id)
        RETURNING id INTO v_group_el_id;

        INSERT INTO group_ (ref, promotion_id)
        VALUES ('IA', v_promotion_id)
        RETURNING id INTO v_group_ia_id;

        INSERT INTO semester (label, "order", promotion_id, academic_year_id)
        VALUES ('S4', 4, v_promotion_id, v_academic_year_id)
        RETURNING id INTO v_semester_id;

        INSERT INTO course (ref, title, credits, mandatory)
        VALUES ('RES401', 'Reseaux', 5, true)
        RETURNING id INTO v_course_reseaux_id;

        INSERT INTO course (ref, title, credits, mandatory)
        VALUES ('ANGL401', 'Anglais technique', 2, false)
        RETURNING id INTO v_course_anglais_id;

        INSERT INTO course (ref, title, credits, mandatory)
        VALUES ('MATH401', 'Mathematiques avancees', 4, true)
        RETURNING id INTO v_course_math_id;

        INSERT INTO course (ref, title, credits, mandatory)
        VALUES ('BDD401', 'Bases de donnees', 4, true)
        RETURNING id INTO v_course_bdd_id;

        INSERT INTO course (ref, title, credits, mandatory)
        VALUES ('ALGO401', 'Algorithmique', 5, true)
        RETURNING id INTO v_course_algo_id;

        INSERT INTO course (ref, title, credits, mandatory)
        VALUES ('WEB401', 'Developpement Web', 3, false)
        RETURNING id INTO v_course_web_id;

        INSERT INTO teacher (name, account_id)
        VALUES ('Rasoa', v_teacher1_acc_id)
        RETURNING id INTO v_teacher1_id;

        INSERT INTO teacher (name, account_id)
        VALUES ('Rakoto', v_teacher2_acc_id)
        RETURNING id INTO v_teacher2_id;

        INSERT INTO teacher (name, account_id)
        VALUES ('Andry', v_teacher3_acc_id)
        RETURNING id INTO v_teacher3_id;

        INSERT INTO teacher (name, account_id)
        VALUES ('Soa', v_teacher4_acc_id)
        RETURNING id INTO v_teacher4_id;

        INSERT INTO student (name, first_name, ref, account_id, promotion_id)
        VALUES ('Rahaingo', 'Nel', 'STU-DEMO-001', v_student1_acc_id, v_promotion_id)
        RETURNING id INTO v_student1_id;

        INSERT INTO student (name, first_name, ref, account_id, promotion_id)
        VALUES ('Ravelojaona', 'Faniry', 'STU-DEMO-002', v_student2_acc_id, v_promotion_id)
        RETURNING id INTO v_student2_id;

        INSERT INTO student (name, first_name, ref, account_id, promotion_id)
        VALUES ('Rabe', 'Sitraka', 'STU-DEMO-003', v_student3_acc_id, v_promotion_id)
        RETURNING id INTO v_student3_id;

        INSERT INTO student (name, first_name, ref, account_id, promotion_id)
        VALUES ('Rakotomalala', 'Hery', 'STU-DEMO-004', v_student4_acc_id, v_promotion_id)
        RETURNING id INTO v_student4_id;

        INSERT INTO student (name, first_name, ref, account_id, promotion_id)
        VALUES ('Andrianarivo', 'Malala', 'STU-DEMO-005', v_student5_acc_id, v_promotion_id)
        RETURNING id INTO v_student5_id;

        INSERT INTO student (name, first_name, ref, account_id, promotion_id)
        VALUES ('Razafindrakoto', 'Tojo', 'STU-DEMO-006', v_student6_acc_id, v_promotion_id)
        RETURNING id INTO v_student6_id;

        INSERT INTO student (name, first_name, ref, account_id, promotion_id)
        VALUES ('Rasoamanana', 'Nomena', 'STU-DEMO-007', v_student7_acc_id, v_promotion_id)
        RETURNING id INTO v_student7_id;

        INSERT INTO student (name, first_name, ref, account_id, promotion_id)
        VALUES ('Randriamampionona', 'Lala', 'STU-DEMO-008', v_student8_acc_id, v_promotion_id)
        RETURNING id INTO v_student8_id;

        INSERT INTO student (name, first_name, ref, account_id, promotion_id)
        VALUES ('Rabenirina', 'Rija', 'STU-DEMO-009', v_student9_acc_id, v_promotion_id)
        RETURNING id INTO v_student9_id;

        INSERT INTO student (name, first_name, ref, account_id, promotion_id)
        VALUES ('Ravelomanana', 'Feno', 'STU-DEMO-010', v_student10_acc_id, v_promotion_id)
        RETURNING id INTO v_student10_id;

        INSERT INTO student (name, first_name, ref, account_id, promotion_id)
        VALUES ('Andriamihaja', 'Sara', 'STU-DEMO-011', v_student11_acc_id, v_promotion_id)
        RETURNING id INTO v_student11_id;

        INSERT INTO student (name, first_name, ref, account_id, promotion_id)
        VALUES ('Rakotoarisoa', 'Kanto', 'STU-DEMO-012', v_student12_acc_id, v_promotion_id)
        RETURNING id INTO v_student12_id;

        INSERT INTO student_group_history (start_date, end_date, student_id, group_id)
        VALUES ('2026-01-01', NULL, v_student1_id, v_group_tn_id);

        INSERT INTO student_group_history (start_date, end_date, student_id, group_id)
        VALUES ('2026-01-01', NULL, v_student2_id, v_group_tn_id);

        INSERT INTO student_group_history (start_date, end_date, student_id, group_id)
        VALUES ('2026-01-01', NULL, v_student3_id, v_group_el_id);

        INSERT INTO student_group_history (start_date, end_date, student_id, group_id)
        VALUES ('2026-01-01', NULL, v_student4_id, v_group_tn_id);

        INSERT INTO student_group_history (start_date, end_date, student_id, group_id)
        VALUES ('2026-01-01', NULL, v_student5_id, v_group_el_id);

        INSERT INTO student_group_history (start_date, end_date, student_id, group_id)
        VALUES ('2026-01-01', NULL, v_student6_id, v_group_ia_id);

        INSERT INTO student_group_history (start_date, end_date, student_id, group_id)
        VALUES ('2026-01-01', NULL, v_student7_id, v_group_ia_id);

        INSERT INTO student_group_history (start_date, end_date, student_id, group_id)
        VALUES ('2026-01-01', NULL, v_student8_id, v_group_tn_id);

        INSERT INTO student_group_history (start_date, end_date, student_id, group_id)
        VALUES ('2026-01-01', NULL, v_student9_id, v_group_el_id);

        INSERT INTO student_group_history (start_date, end_date, student_id, group_id)
        VALUES ('2026-01-01', NULL, v_student10_id, v_group_ia_id);

        INSERT INTO student_group_history (start_date, end_date, student_id, group_id)
        VALUES ('2026-01-01', NULL, v_student11_id, v_group_tn_id);

        INSERT INTO student_group_history (start_date, end_date, student_id, group_id)
        VALUES ('2026-01-01', NULL, v_student12_id, v_group_el_id);

        INSERT INTO course_assignment (course_id, teacher_id, group_id, semester_id)
        VALUES (v_course_reseaux_id, v_teacher1_id, v_group_tn_id, v_semester_id)
        RETURNING id INTO v_ca_reseaux_tn_id;

        INSERT INTO course_assignment (course_id, teacher_id, group_id, semester_id)
        VALUES (v_course_anglais_id, v_teacher2_id, v_group_tn_id, v_semester_id)
        RETURNING id INTO v_ca_anglais_tn_id;

        INSERT INTO course_assignment (course_id, teacher_id, group_id, semester_id)
        VALUES (v_course_math_id, v_teacher3_id, v_group_tn_id, v_semester_id)
        RETURNING id INTO v_ca_math_tn_id;

        INSERT INTO course_assignment (course_id, teacher_id, group_id, semester_id)
        VALUES (v_course_bdd_id, v_teacher4_id, v_group_tn_id, v_semester_id)
        RETURNING id INTO v_ca_bdd_tn_id;

        INSERT INTO course_assignment (course_id, teacher_id, group_id, semester_id)
        VALUES (v_course_reseaux_id, v_teacher1_id, v_group_el_id, v_semester_id)
        RETURNING id INTO v_ca_reseaux_el_id;

        INSERT INTO course_assignment (course_id, teacher_id, group_id, semester_id)
        VALUES (v_course_algo_id, v_teacher3_id, v_group_el_id, v_semester_id)
        RETURNING id INTO v_ca_algo_el_id;

        INSERT INTO course_assignment (course_id, teacher_id, group_id, semester_id)
        VALUES (v_course_web_id, v_teacher2_id, v_group_el_id, v_semester_id)
        RETURNING id INTO v_ca_web_el_id;

        INSERT INTO course_assignment (course_id, teacher_id, group_id, semester_id)
        VALUES (v_course_reseaux_id, v_teacher1_id, v_group_ia_id, v_semester_id)
        RETURNING id INTO v_ca_reseaux_ia_id;

        INSERT INTO course_assignment (course_id, teacher_id, group_id, semester_id)
        VALUES (v_course_math_id, v_teacher3_id, v_group_ia_id, v_semester_id)
        RETURNING id INTO v_ca_math_ia_id;

        INSERT INTO course_assignment (course_id, teacher_id, group_id, semester_id)
        VALUES (v_course_algo_id, v_teacher4_id, v_group_ia_id, v_semester_id)
        RETURNING id INTO v_ca_algo_ia_id;

        INSERT INTO exam (date_exam, coefficient, course_assignment_id)
        VALUES ('2026-12-15 09:00:00', 1.0, v_ca_reseaux_tn_id)
        RETURNING id INTO v_exam_reseaux_tn_id;

        INSERT INTO exam (date_exam, coefficient, course_assignment_id)
        VALUES ('2026-12-16 09:00:00', 1.0, v_ca_anglais_tn_id)
        RETURNING id INTO v_exam_anglais_tn_id;

        INSERT INTO exam (date_exam, coefficient, course_assignment_id)
        VALUES ('2026-12-17 09:00:00', 1.0, v_ca_math_tn_id)
        RETURNING id INTO v_exam_math_tn_id;

        INSERT INTO exam (date_exam, coefficient, course_assignment_id)
        VALUES ('2026-12-18 09:00:00', 1.0, v_ca_bdd_tn_id)
        RETURNING id INTO v_exam_bdd_tn_id;

        INSERT INTO exam (date_exam, coefficient, course_assignment_id)
        VALUES ('2026-12-15 14:00:00', 1.0, v_ca_reseaux_el_id)
        RETURNING id INTO v_exam_reseaux_el_id;

        INSERT INTO exam (date_exam, coefficient, course_assignment_id)
        VALUES ('2026-12-16 14:00:00', 1.0, v_ca_algo_el_id)
        RETURNING id INTO v_exam_algo_el_id;

        INSERT INTO exam (date_exam, coefficient, course_assignment_id)
        VALUES ('2026-12-17 14:00:00', 1.0, v_ca_web_el_id)
        RETURNING id INTO v_exam_web_el_id;

        INSERT INTO exam (date_exam, coefficient, course_assignment_id)
        VALUES ('2026-12-15 16:00:00', 1.0, v_ca_reseaux_ia_id)
        RETURNING id INTO v_exam_reseaux_ia_id;

        INSERT INTO exam (date_exam, coefficient, course_assignment_id)
        VALUES ('2026-12-16 16:00:00', 1.0, v_ca_math_ia_id)
        RETURNING id INTO v_exam_math_ia_id;

        INSERT INTO exam (date_exam, coefficient, course_assignment_id)
        VALUES ('2026-12-17 16:00:00', 1.0, v_ca_algo_ia_id)
        RETURNING id INTO v_exam_algo_ia_id;

        INSERT INTO grade (value, student_id, exam_id)
        VALUES (15.5, v_student1_id, v_exam_reseaux_tn_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (12.0, v_student1_id, v_exam_anglais_tn_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (14.0, v_student1_id, v_exam_math_tn_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (16.5, v_student1_id, v_exam_bdd_tn_id);

        INSERT INTO grade (value, student_id, exam_id)
        VALUES (11.0, v_student2_id, v_exam_reseaux_tn_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (13.5, v_student2_id, v_exam_anglais_tn_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (9.5, v_student2_id, v_exam_math_tn_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (12.0, v_student2_id, v_exam_bdd_tn_id);

        INSERT INTO grade (value, student_id, exam_id)
        VALUES (16.0, v_student3_id, v_exam_reseaux_el_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (14.5, v_student3_id, v_exam_algo_el_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (13.0, v_student3_id, v_exam_web_el_id);

        INSERT INTO grade (value, student_id, exam_id)
        VALUES (18.0, v_student4_id, v_exam_reseaux_tn_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (15.0, v_student4_id, v_exam_anglais_tn_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (17.5, v_student4_id, v_exam_math_tn_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (16.0, v_student4_id, v_exam_bdd_tn_id);

        INSERT INTO grade (value, student_id, exam_id)
        VALUES (7.5, v_student5_id, v_exam_reseaux_el_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (10.0, v_student5_id, v_exam_algo_el_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (11.5, v_student5_id, v_exam_web_el_id);

        INSERT INTO grade (value, student_id, exam_id)
        VALUES (13.0, v_student6_id, v_exam_reseaux_ia_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (14.5, v_student6_id, v_exam_math_ia_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (12.5, v_student6_id, v_exam_algo_ia_id);

        INSERT INTO grade (value, student_id, exam_id)
        VALUES (9.0, v_student7_id, v_exam_reseaux_ia_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (8.5, v_student7_id, v_exam_math_ia_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (10.0, v_student7_id, v_exam_algo_ia_id);

        INSERT INTO grade (value, student_id, exam_id)
        VALUES (14.0, v_student8_id, v_exam_reseaux_tn_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (11.0, v_student8_id, v_exam_anglais_tn_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (13.5, v_student8_id, v_exam_math_tn_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (15.0, v_student8_id, v_exam_bdd_tn_id);

        INSERT INTO grade (value, student_id, exam_id)
        VALUES (17.0, v_student9_id, v_exam_reseaux_el_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (15.5, v_student9_id, v_exam_algo_el_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (16.0, v_student9_id, v_exam_web_el_id);

        INSERT INTO grade (value, student_id, exam_id)
        VALUES (12.5, v_student10_id, v_exam_reseaux_ia_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (11.0, v_student10_id, v_exam_math_ia_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (13.0, v_student10_id, v_exam_algo_ia_id);

        INSERT INTO grade (value, student_id, exam_id)
        VALUES (16.5, v_student11_id, v_exam_reseaux_tn_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (14.0, v_student11_id, v_exam_anglais_tn_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (15.5, v_student11_id, v_exam_math_tn_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (17.0, v_student11_id, v_exam_bdd_tn_id);

        INSERT INTO grade (value, student_id, exam_id)
        VALUES (8.0, v_student12_id, v_exam_reseaux_el_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (9.5, v_student12_id, v_exam_algo_el_id);
        INSERT INTO grade (value, student_id, exam_id)
        VALUES (10.5, v_student12_id, v_exam_web_el_id);
    END $$;