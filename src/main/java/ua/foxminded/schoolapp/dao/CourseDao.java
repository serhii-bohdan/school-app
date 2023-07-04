package ua.foxminded.schoolapp.dao;

import java.util.List;
import ua.foxminded.schoolapp.model.Course;

/**
 * The CourseDao interface provides operations for accessing and manipulating
 * Course entities. It extends the {@link BaseDao} interface.
 *
 * @author Serhii Bohdan
 */
public interface CourseDao extends BaseDao<Course> {

    /**
     * Retrieves all courses from the database.
     *
     * @return a list of all Course objects
     */
    List<Course> findAllCourses();

}
