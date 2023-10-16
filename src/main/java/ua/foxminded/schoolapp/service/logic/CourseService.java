package ua.foxminded.schoolapp.service.logic;

import java.util.List;
import java.util.Optional;
import ua.foxminded.schoolapp.model.Course;

/**
 * The CourseService interface provides operations for managing courses. It
 * allows for initializing courses, adding, retrieving, updating, and deleting
 * courses, as well as obtaining a list of all courses and finding courses by ID
 * or name.
 *
 * @author Serhii Bohdan
 */
public interface CourseService {

    /**
     * Initializes the courses by generating them and saving them.
     */
    void initCourses();

    /**
     * Adds a new course with the specified name and description.
     *
     * @param courseName  the name of the course to add
     * @param description the description of the course
     * @return an {@link Optional} containing the added course if successful, empty
     *         otherwise
     */
    Optional<Course> addCourse(String courseName, String description);

    /**
     * Retrieves a course by its ID.
     *
     * @param courseId the ID of the course to retrieve
     * @return an {@link Optional} containing the retrieved course, empty if not
     *         found
     */
    Optional<Course> getCourseById(Integer courseId);

    /**
     * Retrieves a course by its name.
     *
     * @param courseName the name of the course to retrieve
     * @return an {@link Optional} containing the retrieved course, empty if not
     *         found
     */
    Optional<Course> getCourseByName(String courseName);

    /**
     * Retrieves a list of all courses.
     *
     * @return a list of all courses
     */
    List<Course> getAllCourses();

    /**
     * Updates the information of an existing course.
     *
     * @param updatedCourse the updated course object
     * @return an {@link Optional} containing the updated course if successful,
     *         empty otherwise
     */
    Optional<Course> updateCourse(Course updatedCourse);

    /**
     * Deletes a course by its name.
     *
     * @param courseName the name of the course to delete
     */
    void deleteCourseByName(String courseName);

}