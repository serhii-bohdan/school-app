package ua.foxminded.schoolapp.service;

/**
 * The UserInputValidator interface provides methods for validating user input
 * in the school application.
 *
 * @author Serhii Bohdan
 */
public interface UserInputValidator {

    /**
     * Validates the amount of students.
     *
     * @param amountOfStudents the amount of students to validate
     * @return {@code true} if the amount of students is valid, {@code false}
     *         otherwise
     */
    boolean validateAmountOfStudents(int amountOfStudents);

    /**
     * Validates the course name.
     *
     * @param courseName the course name to validate
     * @return {@code true} if the course name is valid, {@code false} otherwise
     */
    boolean validateCourseName(String courseName);

    /**
     * Validates the student's full name.
     *
     * @param firstName the first name of the student to validate
     * @param lastName  the last name of the student to validate
     * @return {@code true} if the student's full name is valid, {@code false}
     *         otherwise
     */
    boolean validateStudentFullName(String firstName, String lastName);

    /**
     * Validates the group ID.
     *
     * @param groupId the group ID to validate
     * @return {@code true} if the group ID is valid, {@code false} otherwise
     */
    boolean validateGroupId(int groupId);

    /**
     * Validates the student ID.
     *
     * @param studentId the student ID to validate
     * @return {@code true} if the student ID is valid, {@code false} otherwise
     */
    boolean validateStudentId(int studentId);

    /**
     * Checks if a student is registered for a course.
     *
     * @param firstName  the first name of the student
     * @param lastName   the last name of the student
     * @param courseName the name of the course
     * @return {@code true} if the student is registered for the course,
     *         {@code false} otherwise
     */
    boolean isStudentOnCourse(String firstName, String lastName, String courseName);

}
