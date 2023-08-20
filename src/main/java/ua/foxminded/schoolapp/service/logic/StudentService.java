package ua.foxminded.schoolapp.service.logic;

import java.util.List;
import ua.foxminded.schoolapp.model.Student;

/**
 * The StudentService interface provides operations for managing students.
 *
 * @author Serhii Bohdan
 */
public interface StudentService {

    /**
     * Initializes the students by generating them and saving them.
     */
    void initStudents();

    /**
     * Initializes the students-courses table by randomly assigning students to
     * courses.
     */
    void initStudentsCoursesTable();

    /**
     * Retrieves a list of students related to the course with the given name.
     *
     * @param courseName the name of the course
     * @return a list of students related to the course
     */
    List<Student> getStudentsRelatedToCourse(String courseName);

    /**
     * Adds a new student with the specified first name, last name, and group ID.
     *
     * @param firstName the first name of the student
     * @param lastName  the last name of the student
     * @param groupId   the ID of the group to which the student should belong
     * @return the number of affected rows (1 if a student was added, 0 otherwise)
     */
    int addStudent(String firstName, String lastName, int groupId);

    /**
     * Deletes the student with the specified ID.
     *
     * @param studentId the ID of the student to delete
     * @return the number of affected rows (1 if a student was deleted, 0 otherwise)
     */
    int deleteStudent(int studentId);

    /**
     * Adds the specified student to the course with the given name.
     *
     * @param firstName  the first name of the student
     * @param lastName   the last name of the student
     * @param courseName the name of the course
     * @return the number of affected rows (1 if the student was added to the
     *         course, 0 otherwise)
     */
    int addStudentToCourse(String firstName, String lastName, String courseName);

    /**
     * Deletes the specified student from the course with the given name.
     *
     * @param firstName  the first name of the student
     * @param lastName   the last name of the student
     * @param courseName the name of the course
     * @return the number of affected rows (1 if the student was deleted from the
     *         course, 0 otherwise)
     */
    int deleteStudentFromCourse(String firstName, String lastName, String courseName);

    /**
     * Retrieves the student with the specified ID.
     *
     * @param studentId the ID of the student to retrieve
     * @return the student with the specified ID, or null if no such student exists
     */
    Student getStudentById(int studentId);

    /**
     * Retrieves a list of all students.
     *
     * @return a list of all students
     */
    List<Student> getAllStudents();

}
