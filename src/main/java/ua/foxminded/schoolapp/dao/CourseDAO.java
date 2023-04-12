package ua.foxminded.schoolapp.dao;

import java.util.List;
import ua.foxminded.schoolapp.entity.Course;
import ua.foxminded.schoolapp.entity.Student;

public interface CourseDAO extends CrudDAO<Course> {

    List<Student> findStudentsRelatedToCourse(String courseName);
}
