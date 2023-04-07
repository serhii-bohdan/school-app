drop table if exists groups cascade;
drop table if exists students cascade;
drop table if exists courses cascade;
drop table if exists students_courses cascade;

create table groups
(
    group_id integer primary key,
  group_name varchar(5) not null
);

create table students
(
  student_id integer primary key,
  group_id integer references groups(group_id),
  first_name text not null,
  last_name text not null
);

create table courses
(
    course_id integer primary key,
    course_name text not null,
    course_description text not null
);

create table students_courses (
  student_courses_id serial primary key,
  fk_student_id integer references students(student_id),
  fk_course_id integer references courses(course_id)
);
