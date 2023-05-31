package ua.foxminded.schoolapp.dao;

import java.util.List;
import ua.foxminded.schoolapp.model.Course;

public interface CourseDao extends BaseDao<Course> {

    List<Course> findAllCourses();
}
