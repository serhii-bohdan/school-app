package ua.foxminded.schoolapp.service.logic;

import java.util.List;
import java.util.Optional;
import ua.foxminded.schoolapp.dto.StudentDto;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

/**
 * The StudentService interface provides operations for managing students. It
 * allows for initializing students, adding, retrieving, updating, and deleting
 * students.
 * 
 * @author Serhii Bohdan
 */
public interface StudentService {

    /**
     * Initializes the students by generating them and saving them.
     *
     * @param groups the list of groups to generate students for
     */
    void initStudents(List<Group> groups);

    /**
     * Adds a new student with the information provided in the {@code newStudent}
     * parameter.
     *
     * @param newStudent the {@link StudentDto} containing the details of the new
     *                   student
     * @return an {@link Optional} containing the added student if the addition was
     *         successful, empty otherwise
     */
    Optional<Student> addStudent(StudentDto newStudent);

    /**
     * Retrieves the student with the specified ID.
     *
     * @param studentId the ID of the student to retrieve
     * @return an {@link Optional} containing the student with the specified ID, or
     *         empty if no such student exists
     */
    Optional<Student> getStudentById(Integer studentId);

    /**
     * Retrieves a student by their first name and last name.
     *
     * @param firstName the first name of the student
     * @param lastName  the last name of the student
     * @return an {@link Optional} containing the student with the specified full
     *         name, or empty if no such student exists
     */
    Optional<Student> getStudentByFullName(String firstName, String lastName);

    /**
     * Retrieves a list of all students.
     *
     * @return a list of all students
     */
    List<Student> getAllStudents();

    /**
     * Updates student information.
     *
     * @param updatedStudent the updated student object
     * @return an {@link Optional} containing the updated student, or empty if the
     *         update was unsuccessful
     */
    Optional<Student> updateStudent(Student updatedStudent);

    /**
     * Deletes the student with the specified ID.
     *
     * @param studentId the ID of the student to delete
     */
    void deleteStudentById(Integer studentId);

}
