
CREATE DATABASE IF NOT EXISTS studyplanner;
USE studyplanner;


CREATE TABLE students (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100)
);


CREATE TABLE SignUp (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        student_id INT NOT NULL,
                        sign_up_date DATE NOT NULL,
                        status VARCHAR(20) DEFAULT 'activo',
                        FOREIGN KEY (student_id) REFERENCES students(id)
);


CREATE TABLE Task (
                      task_id INT AUTO_INCREMENT PRIMARY KEY,
                      student_id INT,
                      title VARCHAR(100),
                      description TEXT,
                      status VARCHAR(20),
                      dueDate DATE,
                      FOREIGN KEY (student_id) REFERENCES students(id)
);

CREATE TABLE TaskStats (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           student_id INT,
                           total_tasks INT,
                           completed_tasks INT,
                           pending_tasks INT,
                           last_updated DATE,
                           FOREIGN KEY (student_id) REFERENCES students(id)
);

CREATE TABLE CalendarEvent (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               student_id INT,
                               title VARCHAR(100),
                               description TEXT,
                               event_date DATE,
                               event_time TIME,
                               FOREIGN KEY (student_id) REFERENCES students(id)
);


CREATE TABLE TaskHistory (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             task_id INT,
                             changed_by INT,
                             change_type VARCHAR(50), -- paivita, poista
                             change_date DATETIME,
                             old_status VARCHAR(20),
                             new_status VARCHAR(20),
                             FOREIGN KEY (task_id) REFERENCES Task(task_id),
                             FOREIGN KEY (changed_by) REFERENCES students(id)
);