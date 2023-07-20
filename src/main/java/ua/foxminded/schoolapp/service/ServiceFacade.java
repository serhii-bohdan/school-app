package ua.foxminded.schoolapp.service;

import java.util.List;
import java.util.Map;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

/**
 * The ServiceFacade interface provides methods for accessing the school
 * application's services and functionalities.
 *
 * @author Serhii Bohdan
 */
public interface ServiceFacade {

    /**
     * Retrieves groups with the given number of students.
     *
     * @param amountOfStudents the number of students for which to retrieve groups
     * @return a map of groups and their number of students
     */
    Map<Group, Integer> getGroupsWithGivenNumberOfStudents(int amountOfStudents);

    /**
     * Retrieves students with their courses based on the given course name.
     *
     * @param courseName the name of the course for which to retrieve students
     * @return a map of students and their courses
     */
    Map<Student, List<Course>> getStudentsWithCoursesByCourseName(String courseName);

    /**
     * Adds a new student with the given information.
     *
     * @param firstName the first name of the student
     * @param lastName  the last name of the student
     * @param groupId   the ID of the group to which the student belongs
     * @return {@code true} if the new student is added successfully, {@code false}
     *         otherwise
     */
    boolean addNewStudent(String firstName, String lastName, int groupId);

    /**
     * Deletes a student with the specified ID.
     *
     * @param studentId the ID of the student to delete
     * @return {@code true} if the student is deleted successfully, {@code false}
     *         otherwise
     */
    boolean deleteStudentById(int studentId);

    /**
     * Adds a student to a course with the given information.
     *
     * @param firstName  the first name of the student
     * @param lastName   the last name of the student
     * @param courseName the name of the course to add the student to
     * @return {@code true} if the student is added to the course successfully,
     *         {@code false} otherwise
     */
    boolean addStudentToCourse(String firstName, String lastName, String courseName);

    /**
     * Deletes a student from a course with the given information.
     *
     * @param firstName  the first name of the student
     * @param lastName   the last name of the student
     * @param courseName the name of the course to delete the student from
     * @return {@code true} if the student is deleted from the course successfully,
     *         {@code false} otherwise
     */
    boolean deleteStudentFromCourse(String firstName, String lastName, String courseName);

    /**
     * Retrieves a student with the specified ID.
     *
     * @param studentId the ID of the student to retrieve
     * @return the student with the specified ID, or {@code null} if no such student
     *         exists
     */
    Student getStudentById(int studentId);

    /**
     * Retrieves all students with their courses.
     *
     * @return a map of students and their courses
     */
    Map<Student, List<Course>> getAllStudentsWithTheirCourses();

    /**
     * Retrieves all courses.
     *
     * @return a list of all courses
     */
    List<Course> getAllCourses();

}
