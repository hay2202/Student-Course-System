-- Create the database (if it doesn’t already exist)
CREATE DATABASE IF NOT EXISTS student_course_db;
USE student_course_db;

-- Student Table
CREATE TABLE IF NOT EXISTS student (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    special_key VARCHAR(100) NOT NULL UNIQUE
);

-- Course Table
CREATE TABLE IF NOT EXISTS course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

-- StudentCourse Table (with ID field)
CREATE TABLE IF NOT EXISTS student_course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT,
    course_id BIGINT,
    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES Student(id) ON DELETE CASCADE,
    CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES Course(id) ON DELETE CASCADE,
    UNIQUE(student_id, course_id)  -- Ensures a student can register for the same course only once
);


