package ua.foxminded.schoolapp.dao;

import java.util.List;
import ua.foxminded.schoolapp.model.Course;

public interface CourseDAO extends DAO<Course> {

    List<Course> findAllCourses();
}
