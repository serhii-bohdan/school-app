package ua.foxminded.schoolapp.service.logic;

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
    boolean validateAmountOfStudents(Integer amountOfStudents);

    /**
     * Validates the group ID.
     *
     * @param groupId the group ID to validate
     * @return {@code true} if the group ID is valid, {@code false} otherwise
     */
    boolean validateGroupId(Integer groupId);

    /**
     * Validates the existence of a group with the specified name.
     *
     * @param groupName the group name to validate
     * @return {@code true} if a group with the specified name exists, {@code false}
     *         otherwise
     */
    boolean validateGroupNameExistence(String groupName);

    /**
     * Validates the group name against a specified pattern.
     *
     * @param groupName the group name to validate
     * @return {@code true} if the group name matches the specified pattern,
     *         {@code false} otherwise
     */
    boolean validateGroupNamePattern(String groupName);

    /**
     * Validates the course name.
     *
     * @param courseName the course name to validate
     * @return {@code true} if the course name is valid, {@code false} otherwise
     */
    boolean validateCourseName(String courseName);

    /**
     * Validates the course description.
     *
     * @param courseDescription the course description to validate
     * @return {@code true} if the course description is valid, {@code false}
     *         otherwise
     */
    boolean validateDescription(String courseDescription);

    /**
     * Validates the student ID.
     *
     * @param studentId the student ID to validate
     * @return {@code true} if the student ID is valid, {@code false} otherwise
     */
    boolean validateStudentId(Integer studentId);

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
     * Checks if a student is registered for a course.
     *
     * @param firstName  the first name of the student
     * @param lastName   the last name of the student
     * @param courseName the name of the course
     * @return {@code true} if the student is registered for the course,
     *         {@code false} otherwise
     */
    boolean isStudentOnCourse(String firstName, String lastName, String courseName);

    /**
     * Validates the length of a name.
     *
     * @param name the name to validate
     * @return {@code true} if the name length is valid, {@code false} otherwise
     */
    boolean validateNameLength(String name);

}