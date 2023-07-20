package ua.foxminded.schoolapp.dao;

import java.util.List;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Student;

/**
 * The CourseDao interface provides operations for accessing and manipulating
 * Course entities. It extends the {@link BaseDao} interface.
 *
 * @author Serhii Bohdan
 */
public interface CourseDao extends BaseDao<Course> {

    /**
     * Retrieves all courses for a specific student from the database.
     *
     * @param student the student for which to find courses
     * @return a list of Course objects related to the student
     */
    List<Course> findCoursesForStudent(Student student);

}
