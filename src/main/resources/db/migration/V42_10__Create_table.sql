CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS "account" (
                                         id VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
                                         email VARCHAR(255) NOT NULL UNIQUE,
                                         password VARCHAR(255) NOT NULL,
                                         role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS "promotion" (
                                           id VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
                                           label VARCHAR(100) NOT NULL,
                                           start_year TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS "group_" (
                                        id VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
                                        ref VARCHAR(50) NOT NULL,
                                        promotion_id VARCHAR NOT NULL REFERENCES promotion(id)
);

CREATE TABLE IF NOT EXISTS "semester" (
                                          id VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
                                          label VARCHAR(20) NOT NULL,
                                          "order" INT NOT NULL,
                                          promotion_id VARCHAR NOT NULL REFERENCES promotion(id)
);

CREATE TABLE IF NOT EXISTS "student" (
                                         id VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
                                         name VARCHAR(100) NOT NULL,
                                         first_name VARCHAR(100) NOT NULL,
                                         ref VARCHAR(50),
                                         account_id VARCHAR NOT NULL UNIQUE REFERENCES account(id),
                                         promotion_id VARCHAR REFERENCES promotion(id)
);

CREATE TABLE IF NOT EXISTS "teacher" (
                                         id VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
                                         name VARCHAR(100) NOT NULL,
                                         account_id VARCHAR NOT NULL UNIQUE REFERENCES account(id)
);

CREATE TABLE IF NOT EXISTS "course" (
                                        id VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
                                        ref VARCHAR(50) NOT NULL,
                                        title VARCHAR(150) NOT NULL,
                                        credits INT NOT NULL
);

CREATE TABLE IF NOT EXISTS "course_assignment" (
                                                   id VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
                                                   course_id VARCHAR NOT NULL REFERENCES course(id),
                                                   teacher_id VARCHAR NOT NULL REFERENCES teacher(id),
                                                   group_id VARCHAR NOT NULL REFERENCES group_(id),
                                                   semester_id VARCHAR NOT NULL REFERENCES semester(id)
);

CREATE TABLE IF NOT EXISTS "exam" (
                                      id VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
                                      date_exam TIMESTAMP NOT NULL,
                                      coefficient NUMERIC(4,2) NOT NULL,
                                      course_assignment_id VARCHAR NOT NULL REFERENCES course_assignment(id)
);

CREATE TABLE IF NOT EXISTS "grade" (
                                       id VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
                                       value NUMERIC(5,2) NOT NULL,
                                       student_id VARCHAR NOT NULL REFERENCES student(id),
                                       exam_id VARCHAR NOT NULL REFERENCES exam(id)
);

CREATE TABLE IF NOT EXISTS "grade_history" (
                                               id VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
                                               old_value NUMERIC(5,2),
                                               new_value NUMERIC(5,2),
                                               reason VARCHAR(255) NOT NULL,
                                               modified_at TIMESTAMP NOT NULL,
                                               grade_id VARCHAR NOT NULL REFERENCES grade(id),
                                               modified_by_account_id VARCHAR NOT NULL REFERENCES account(id)
);

CREATE TABLE IF NOT EXISTS "student_group_history" (
                                                       id VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
                                                       start_date TIMESTAMP NOT NULL,
                                                       end_date TIMESTAMP,
                                                       student_id VARCHAR NOT NULL REFERENCES student(id),
                                                       group_id VARCHAR NOT NULL REFERENCES group_(id)
);