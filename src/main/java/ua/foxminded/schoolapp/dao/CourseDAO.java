package ua.foxminded.schoolapp.dao;

import java.util.List;
import ua.foxminded.schoolapp.entity.Course;

public interface CourseDAO extends CrudDAO<Course> {

    List<Course> findAvailableCourses();
}
