SELECT students.first_name, courses.course_name
FROM students
INNER JOIN students_courses ON students.student_id = students_courses.fk_student_id
INNER JOIN courses ON courses.course_id = students_courses.fk_course_id;