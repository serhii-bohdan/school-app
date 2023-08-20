package ua.foxminded.schoolapp.service.logic;

import java.util.List;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Student;

/**
 * The CourseService interface provides operations for managing courses.
 *
 * @author Serhii Bohdan
 */
public interface CourseService {

    /**
     * Initializes the courses by generating them and saving them.
     */
    void initCourses();

    /**
     * Retrieves a list of all courses.
     *
     * @return a list of all courses
     */
    List<Course> getAllCourses();

    /**
     * Retrieves a list of courses for the specified student.
     *
     * @param student the student for which to retrieve the courses
     * @return a list of courses for the student
     */
    List<Course> getCoursesForStudent(Student student);

}
