package ua.foxminded.schoolapp.dao;

import java.util.List;
import ua.foxminded.schoolapp.entity.Course;

public interface CourseDAO extends CRUD<Course> {

    List<Course> findAvailableCourses();
}
