CREATE DATABASE StudyPlanner;
USE StudyPlanner;

-- Tabla: Student
CREATE TABLE Student (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         username VARCHAR(50) NOT NULL UNIQUE,
                         password VARCHAR(100) NOT NULL
);

-- Tabla: Course
CREATE TABLE Course (
                        course_id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        description TEXT,
                        student_id INT,
                        FOREIGN KEY (student_id) REFERENCES Student(id)
);

-- Tabla: Task
CREATE TABLE Task (
                      task_id INT AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(100) NOT NULL,
                      description TEXT,
                      due_date DATE,
                      status ENUM('pending', 'in_progress', 'completed') DEFAULT 'pending',
                      student_id INT,
                      course_id INT,
                      FOREIGN KEY (student_id) REFERENCES Student(id),
                      FOREIGN KEY (course_id) REFERENCES Course(course_id)
);

-- Tabla: Assignment
CREATE TABLE Assignment (
                            assignment_id INT AUTO_INCREMENT PRIMARY KEY,
                            due_date DATE,
                            status ENUM('pending', 'submitted', 'graded') DEFAULT 'pending',
                            course_id INT,
                            FOREIGN KEY (course_id) REFERENCES Course(course_id)
);