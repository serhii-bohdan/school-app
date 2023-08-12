INSERT INTO groups (group_name)
VALUES ('MQ-90');

INSERT INTO students (first_name, last_name, group_id)
VALUES ('FirstName', 'LastName', 1);

INSERT INTO courses (course_name, course_description)
VALUES ('CourseName_1', 'Description_1'),
       ('CourseName_2', 'Description_2'),
       ('CourseName_3', 'Description_3');

INSERT INTO students_courses (student_id, course_id)
VALUES (1, 1),
       (1, 2),
       (1, 3);