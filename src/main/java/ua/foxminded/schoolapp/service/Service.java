package ua.foxminded.schoolapp.service;

import java.util.List;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

/**
 * The Service interface provides methods for managing school-related
 * operations, such as retrieving groups, students, and courses, adding or
 * deleting students, and assigning students to courses.
 * 
 * @author Serhii Bohdan
 */
public interface Service {

    /**
     * Retrieves a list of groups that have the specified number of students.
     *
     * @param amountOfStudents the number of students in a group
     * @return a list of groups with the given number of students
     */
    List<Group> getGroupsWithGivenNumberStudents(int amountOfStudents);

    /**
     * Retrieves a list of students related to the specified course.
     *
     * @param courseName the name of the course
     * @return a list of students related to the course
     */
    List<Student> getStudentsRelatedToCourse(String courseName);

    /**
     * Adds a new student to the general list of students.
     *
     * @param firstName the first name of the student
     * @param lastName  the last name of the student
     * @param groupId   the ID of the group to which the student will be added
     * @return true if the student is successfully added, false otherwise
     */
    boolean addNewStudent(String firstName, String lastName, int groupId);

    /**
     * Deletes the student with the specified ID.
     *
     * @param studentId the ID of the student to be deleted
     * @return true if the student is successfully deleted, false otherwise
     */
    boolean deleteStudentById(int studentId);

    /**
     * Adds a student to the specified course.
     *
     * @param firstName  the first name of the student
     * @param lastName   the last name of the student
     * @param courseName the name of the course to which the student will be added
     * @return true if the student is successfully added to the course, false
     *         otherwise
     */
    boolean addStudentToCourse(String firstName, String lastName, String courseName);

    /**
     * Deletes a student from the specified course.
     *
     * @param firstName  the first name of the student
     * @param lastName   the last name of the student
     * @param courseName the name of the course from which the student will be
     *                   deleted
     * @return true if the student is successfully deleted from the course, false
     *         otherwise
     */
    boolean deleteStudentFromCourse(String firstName, String lastName, String courseName);

    /**
     * Retrieves the student with the specified ID.
     *
     * @param studentId the ID of the student to retrieve
     * @return the student with the given ID, or null if not found
     */
    Student getStudentById(int studentId);

    /**
     * Retrieves a list of all students.
     *
     * @return a list of all students
     */
    List<Student> getAllStudents();

}
