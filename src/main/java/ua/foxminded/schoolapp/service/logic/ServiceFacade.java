package ua.foxminded.schoolapp.service.logic;

import java.util.List;
import java.util.Map;
import java.util.Set;
import ua.foxminded.schoolapp.dto.CourseDto;
import ua.foxminded.schoolapp.dto.GroupDto;
import ua.foxminded.schoolapp.dto.StudentDto;

/**
 * The ServiceFacade interface provides methods for accessing the school
 * application's services and functionalities.
 * <p>
 * This interface serves as a high-level interface for managing groups,
 * students, and courses within the application.
 *
 * @author Serhii Bohdan
 */
public interface ServiceFacade {

    /**
     * Initializes the schema of the school application by populating the groups,
     * students, courses, and student-course mapping tables with initial data if
     * they are empty. This method is typically called when the application starts
     * to ensure the initial data setup.
     */
    void initSchema();

    /**
     * Adds a new group with the given name.
     *
     * @param groupName the name of the group to add
     * @return {@code true} if the group is added successfully, {@code false}
     *         otherwise
     */
    boolean addNewGroup(String groupName);

    /**
     * Retrieves a group by its name.
     *
     * @param groupName the name of the group to retrieve
     * @return the group with the specified name, or {@code null} if no such group
     *         exists
     */
    GroupDto getGroupByName(String groupName);

    /**
     * Retrieves a list of all groups in the school application.
     *
     * @return a list of all groups
     */
    List<GroupDto> getAllGroups();

    /**
     * Updates the name of an existing group.
     *
     * @param groupNameToUpdate the current name of the group to update
     * @param newGroupName      the new name for the group
     * @return {@code true} if the group is updated successfully, {@code false}
     *         otherwise
     */
    boolean updateGroup(String groupNameToUpdate, String newGroupName);

    /**
     * Deletes a group by its name.
     *
     * @param groupName the name of the group to delete
     * @return {@code true} if the group is deleted successfully, {@code false}
     *         otherwise
     */
    boolean deleteGroupByName(String groupName);

    /**
     * Adds a new student with the given information.
     *
     * @param firstName the first name of the student
     * @param lastName  the last name of the student
     * @param groupName the name of the group to which the student belongs
     * @return {@code true} if the new student is added successfully, {@code false}
     *         otherwise
     */
    boolean addNewStudent(String firstName, String lastName, String groupName);

    /**
     * Retrieves a student by their ID.
     *
     * @param studentId the ID of the student to retrieve
     * @return the student with the specified ID, or {@code null} if no such student
     *         exists
     */
    StudentDto getStudentById(Integer studentId);

    /**
     * Updates a student's information.
     *
     * @param studentFirstNameToUpdate the first name of the student to update
     * @param studentLastNameToUpdate  the last name of the student to update
     * @param newFirstName             the new first name for the student
     * @param newLastName              the new last name for the student
     * @param newGroupName             the new group name for the student
     * @return {@code true} if the student is updated successfully, {@code false}
     *         otherwise
     */
    boolean updateStudent(String studentFirstNameToUpdate, String studentLastNameToUpdate, String newFirstName,
            String newLastName, String newGroupName);

    /**
     * Deletes a student by their ID.
     *
     * @param studentId the ID of the student to delete
     * @return {@code true} if the student is deleted successfully, {@code false}
     *         otherwise
     */
    boolean deleteStudentById(Integer studentId);

    /**
     * Adds a new course with the given information.
     *
     * @param courseName  the name of the course
     * @param description the description of the course
     * @return {@code true} if the new course is added successfully, {@code false}
     *         otherwise
     */
    boolean addNewCourse(String courseName, String description);

    /**
     * Retrieves a course by its name.
     *
     * @param courseName the name of the course to retrieve
     * @return the course with the specified name, or {@code null} if no such course
     *         exists
     */
    CourseDto getCourseByName(String courseName);

    /**
     * Retrieves a list of all courses in the school application.
     *
     * @return a list of all courses
     */
    List<CourseDto> getAllCourses();

    /**
     * Updates the information of an existing course.
     *
     * @param courseNameToUpdate the current name of the course to update
     * @param newCourseName      the new name for the course
     * @param newDescription     the new description for the course
     * @return {@code true} if the course is updated successfully, {@code false}
     *         otherwise
     */
    boolean updateCourse(String courseNameToUpdate, String newCourseName, String newDescription);

    /**
     * Deletes a course by its name.
     *
     * @param courseName the name of the course to delete
     * @return {@code true} if the course is deleted successfully, {@code false}
     *         otherwise
     */
    boolean deleteCourseByName(String courseName);

    /**
     * Retrieves groups with the given number of students.
     *
     * @param amountOfStudents the number of students for which to retrieve groups
     * @return a map of groups and their number of students
     */
    Map<GroupDto, Integer> getGroupsWithGivenNumberOfStudents(Integer amountOfStudents);

    /**
     * Retrieves students with their courses based on the given course name.
     *
     * @param courseName the name of the course for which to retrieve students
     * @return a map of students and their courses
     */
    Map<StudentDto, Set<CourseDto>> getStudentsWithCoursesByCourseName(String courseName);

    /**
     * Retrieves all students with their groups.
     *
     * @return a map of students and their groups
     */
    Map<StudentDto, GroupDto> getAllStudentsWithTheirGroups();

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
     * Retrieves all students with their courses.
     *
     * @return a map of students and their courses
     */
    Map<StudentDto, Set<CourseDto>> getAllStudentsWithTheirCourses();

}