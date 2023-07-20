package ua.foxminded.schoolapp.dao;

import java.util.List;
import ua.foxminded.schoolapp.model.Student;

/**
 * The StudentDao interface provides operations for accessing and manipulating
 * Student entities. It extends the {@link BaseDao} interface.
 *
 * @author Serhii Bohdan
 */
public interface StudentDao extends BaseDao<Student> {

    /**
     * Retrieves all students related to a specific course from the database.
     *
     * @param courseName the name of the course
     * @return a list of Student objects related to the course
     */
    List<Student> findStudentsRelatedToCourse(String courseName);

    /**
     * Retrieves a student by their ID from the database.
     *
     * @param studentId the ID of the student
     * @return the Student object with the specified ID, or null if not found
     */
    Student findStudentById(int studentId);

    /**
     * Deletes a student by their ID from the database.
     *
     * @param studentId the ID of the student to delete
     * @return the number of rows deleted
     */
    int deleteStudentById(int studentId);

    /**
     * Checks if a student is enrolled in a specific course.
     *
     * @param firstName  the first name of the student
     * @param lastName   the last name of the student
     * @param courseName the name of the course
     * @return true if the student is enrolled in the course, false otherwise
     */
    boolean isStudentOnCourse(String firstName, String lastName, String courseName);

    /**
     * Adds a student to a specific course by their first name, last name, and
     * course name.
     *
     * @param firstName  the first name of the student
     * @param lastName   the last name of the student
     * @param courseName the name of the course
     * @return the number of rows inserted
     */
    int addStudentToCourse(String firstName, String lastName, String courseName);

    /**
     * Adds a student to a specific course by their ID and course ID.
     *
     * @param studentId the ID of the student
     * @param courseId  the ID of the course
     * @return the number of rows inserted
     */
    int addStudentToCourse(int studentId, int courseId);

    /**
     * Deletes a student from a specific course by their first name, last name, and
     * course name.
     *
     * @param firstName  the first name of the student
     * @param lastName   the last name of the student
     * @param courseName the name of the course
     * @return the number of rows deleted
     */
    int deleteStudentFromCourse(String firstName, String lastName, String courseName);

}
