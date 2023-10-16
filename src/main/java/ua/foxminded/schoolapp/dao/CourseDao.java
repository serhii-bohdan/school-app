package ua.foxminded.schoolapp.dao;

import java.util.Optional;
import ua.foxminded.schoolapp.model.Course;

/**
 * The CourseDao interface provides operations for accessing and manipulating
 * Course entities. It extends the {@link BaseDao} interface.
 *
 * @author Serhii Bohdan
 */
public interface CourseDao extends BaseDao<Course> {

    /**
     * Finds a course in the database by its name.
     *
     * @param courseName the name of the course to find
     * @return an {@link Optional} containing the found course, or empty if not
     *         found
     */
    Optional<Course> findCourseByName(String courseName);

}
