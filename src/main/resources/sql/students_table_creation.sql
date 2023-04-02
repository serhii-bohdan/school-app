drop table if exists students;

create table students
(
  student_id integer primary key,
  group_id integer references groups(group_id),
  first_name text not null,
  last_name text not null
)