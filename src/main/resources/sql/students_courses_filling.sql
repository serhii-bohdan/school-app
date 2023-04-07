do $$
declare 
  num_courses integer;
  var_student_id integer;
  var_course_id integer;
begin 
  for var_student_id in (select student_id from students) 
  loop 
    num_courses := floor(random() * 3) + 1;
    for i in 1..num_courses 
    loop 

      loop
        var_course_id := (select course_id from courses order by random() limit 1); 
        exit when not exists (select 1 from students_courses where fk_student_id = var_student_id and fk_course_id = var_course_id);
      end loop;
      
      insert into students_courses (fk_student_id, fk_course_id) 
      values (var_student_id, var_course_id);
    end loop;
  end loop;
end $$;