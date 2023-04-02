drop table if exists courses;

create table courses
(
    course_id integer primary key,
    course_name text not null,
    course_description text not null
);