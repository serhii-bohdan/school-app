package ua.foxminded.schoolapp.cli;

import java.util.List;
import java.util.Map;
import java.util.Set;
import ua.foxminded.schoolapp.dto.CourseDto;
import ua.foxminded.schoolapp.dto.GroupDto;
import ua.foxminded.schoolapp.dto.StudentDto;

/**
 * The SchoolView interface defines the contract for displaying information and
 * interacting with the user.
 * <p>
 * This interface provides methods to display menus, messages, and various
 * pieces of information related to groups, students, and courses. Implementing
 * classes should provide functionality to interact with the user and receive
 * input from the console.
 *
 * @author Serhii Bohdan
 */
public interface SchoolView {

    /**
     * Displays the main menu.
     */
    void showMenu();

    /**
     * Prints a message to the console.
     *
     * @param message the message to be printed
     */
    void printMessage(String message);

    /**
     * Gets an integer number from the user.
     *
     * @param message the message prompt for the user
     * @return the integer number entered by the user
     */
    int getIntNumberFromUser(String message);

    /**
     * Gets a sentence from the user.
     *
     * @param message the message prompt for the user
     * @return the sentence entered by the user
     */
    String getSentenceFromUser(String message);

    /**
     * Gets the confirmation from the user about deleting a student.
     *
     * @param student the student to be deleted
     * @return the user's confirmation (Y or N)
     */
    String getConfirmationAboutDeletingStudent(StudentDto studentDto);

    /**
     * Gets the confirmation from the user about deleting a group.
     *
     * @param group the group to be deleted
     * @return the user's confirmation (Y or N)
     */
    String getConfirmationAboutDeletingGroup(GroupDto group);

    /**
     * Gets the confirmation from the user about deleting a course.
     *
     * @param course the course to be deleted
     * @return the user's confirmation (Y or N)
     */
    String getConfirmationAboutDeletingCourse(CourseDto course);

    /**
     * Displays groups to the console.
     *
     * @param groups a list of group objects to be displayed
     */
    void displayGroups(List<GroupDto> groups);

    /**
     * Displays groups with their corresponding number of students.
     *
     * @param groupsWithTheirNumberOfStudents a map of groups with their number of
     *                                        students
     */
    void displayGroupsWithTheirNumberOfStudents(Map<GroupDto, Integer> groupsWithTheirNumberOfStudents);

    /**
     * Displays students with their groups.
     *
     * @param studentsWithTheirGroups a map of students with their groups
     */
    void displayStudentsWithTheirGroups(Map<StudentDto, GroupDto> studentsWithTheirGroups);

    /**
     * Displays students with their corresponding courses.
     *
     * @param studentsWithTheirCourses a map of students with their courses
     */
    void displayStudentsWithTheirCourses(Map<StudentDto, Set<CourseDto>> studentsWithTheirCourses);

    /**
     * Displays a list of courses.
     *
     * @param courses a list of courses
     */
    void displayCourses(List<CourseDto> courses);

}