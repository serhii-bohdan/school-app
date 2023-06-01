DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS students_courses CASCADE;

CREATE TABLE groups (
  group_id SERIAL PRIMARY KEY,
  group_name VARCHAR(5) NOT NULL
);

CREATE TABLE students (
  student_id SERIAL PRIMARY KEY,
  first_name VARCHAR(25) NOT NULL,
  last_name VARCHAR(25) NOT NULL,
  group_id INTEGER REFERENCES groups(group_id)
);

CREATE TABLE courses (
  course_id SERIAL PRIMARY KEY,
  course_name VARCHAR(25) NOT NULL,
  course_description TEXT NOT NULL
);

CREATE TABLE students_courses (
  student_courses_id SERIAL PRIMARY KEY,
  student_id INTEGER REFERENCES students(student_id) ON DELETE CASCADE NOT NULL,
  course_id INTEGER REFERENCES courses(course_id) ON DELETE CASCADE NOT NULL
);
