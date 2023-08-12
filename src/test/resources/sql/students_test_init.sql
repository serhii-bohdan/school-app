INSERT INTO groups (group_name)
VALUES ('MQ-90');

INSERT INTO students (first_name, last_name, group_id)
VALUES ('FirstName_1', 'LastName_1', 1),
       ('FirstName_2', 'LastName_2', 1),
       ('FirstName_3', 'LastName_3', 1);

INSERT INTO courses (course_name, course_description)
VALUES ('CourseName', 'Description');

INSERT INTO students_courses (student_id, course_id)
VALUES (1, 1),
       (2, 1),
       (3, 1);