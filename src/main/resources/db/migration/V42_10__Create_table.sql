
CREATE TABLE account (
                         id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid()::text,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         role VARCHAR(20) NOT NULL
);

CREATE TABLE promotion (
                           id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid()::text,
                           label VARCHAR(100) NOT NULL,
                           start_year TIMESTAMP NOT NULL
);

CREATE TABLE group_ (
                        id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid()::text,
                        ref VARCHAR(50) NOT NULL,
                        promotion_id VARCHAR(36) NOT NULL REFERENCES promotion(id)
);

CREATE TABLE semester (
                          id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid()::text,
                          label VARCHAR(20) NOT NULL,
                          "order" INT NOT NULL,
                          promotion_id VARCHAR(36) NOT NULL REFERENCES promotion(id)
);

CREATE TABLE student (
                         id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid()::text,
                         name VARCHAR(100) NOT NULL,
                         first_name VARCHAR(100) NOT NULL,
                         ref VARCHAR(50),
                         account_id VARCHAR(36) NOT NULL UNIQUE REFERENCES account(id),
                         promotion_id VARCHAR(36) REFERENCES promotion(id)
);

CREATE TABLE teacher (
                         id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid()::text,
                         name VARCHAR(100) NOT NULL,
                         account_id VARCHAR(36) NOT NULL UNIQUE REFERENCES account(id)
);

CREATE TABLE course (
                        id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid()::text,
                        ref VARCHAR(50) NOT NULL,
                        title VARCHAR(150) NOT NULL,
                        credits INT NOT NULL
);

CREATE TABLE course_assignment (
                                   id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid()::text,
                                   course_id VARCHAR(36) NOT NULL REFERENCES course(id),
                                   teacher_id VARCHAR(36) NOT NULL REFERENCES teacher(id),
                                   group_id VARCHAR(36) NOT NULL REFERENCES group_(id),
                                   semester_id VARCHAR(36) NOT NULL REFERENCES semester(id)
);

CREATE TABLE exam (
                      id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid()::text,
                      date_exam TIMESTAMP NOT NULL,
                      coefficient NUMERIC(4,2) NOT NULL,
                      course_assignment_id VARCHAR(36) NOT NULL REFERENCES course_assignment(id)
);

CREATE TABLE grade (
                       id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid()::text,
                       value NUMERIC(5,2) NOT NULL,
                       student_id VARCHAR(36) NOT NULL REFERENCES student(id),
                       exam_id VARCHAR(36) NOT NULL REFERENCES exam(id)
);

CREATE TABLE grade_history (
                               id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid()::text,
                               old_value NUMERIC(5,2),
                               new_value NUMERIC(5,2),
                               reason VARCHAR(255) NOT NULL,
                               modified_at TIMESTAMP NOT NULL,
                               grade_id VARCHAR(36) NOT NULL REFERENCES grade(id),
                               modified_by_account_id VARCHAR(36) NOT NULL REFERENCES account(id)
);

CREATE TABLE student_group_history (
                                       id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid()::text,
                                       start_date TIMESTAMP NOT NULL,
                                       end_date TIMESTAMP,
                                       student_id VARCHAR(36) NOT NULL REFERENCES student(id),
                                       group_id VARCHAR(36) NOT NULL REFERENCES group_(id)
);