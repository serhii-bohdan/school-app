DO $$
DECLARE
  num_courses INTEGER;
  var_student_id INTEGER;
  var_course_id INTEGER;
BEGIN
  FOR var_student_id IN (SELECT student_id FROM students)
  LOOP
    num_courses := floor(random() * 3) + 1;
    FOR i IN 1..num_courses
    LOOP

      LOOP
        var_course_id := (SELECT course_id FROM courses ORDER BY random() LIMIT 1);
        exit when NOT EXISTS (SELECT 1 FROM students_courses WHERE fk_student_id = var_student_id AND fk_course_id = var_course_id);
      END LOOP;

      INSERT INTO students_courses (fk_student_id, fk_course_id)
      VALUES (var_student_id, var_course_id);
    END LOOP;
  END LOOP;
END $$;