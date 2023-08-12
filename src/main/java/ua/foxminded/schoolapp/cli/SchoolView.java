package ua.foxminded.schoolapp.cli;

import java.util.List;
import java.util.Map;
import ua.foxminded.schoolapp.model.*;

/**
 * The SchoolView interface defines the contract for displaying information and
 * interacting with the user.
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
     * Gets a word from the user.
     *
     * @param message the message prompt for the user
     * @return the word entered by the user
     */
    String getWordFromUser(String message);

    /**
     * Gets the confirmation from the user about deleting a student.
     *
     * @param student the student to be deleted
     * @return the user's confirmation (Y or N)
     */
    String getConfirmationFromUserAboutDeletingStudent(Student student);

    /**
     * Displays the groups with their corresponding number of students.
     *
     * @param groupsWithTheirNumberOfStudents a map of groups with their number of students
     */
    void displayGroupsWithTheirNumberOfStudents(Map<Group, Integer> groupsWithTheirNumberOfStudents);

    /**
     * Displays the students with their corresponding courses.
     *
     * @param studentsWithTheirCourses a map of students with their courses
     */
    void displayStudentsWithTheirCourses(Map<Student, List<Course>> studentsWithTheirCourses);

    /**
     * Displays a list of courses.
     *
     * @param courses a list of courses
     */
    void displayCourses(List<Course> courses);

}
