DELETE FROM students;
ALTER SEQUENCE students_student_id_seq RESTART WITH 1;

DELETE FROM groups;
ALTER SEQUENCE groups_group_id_seq RESTART WITH 1;

DELETE FROM courses;
ALTER SEQUENCE courses_course_id_seq RESTART WITH 1;

DELETE FROM students_courses;
