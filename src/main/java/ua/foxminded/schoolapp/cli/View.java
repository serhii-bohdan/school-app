package ua.foxminded.schoolapp.cli;

import java.util.List;
import ua.foxminded.schoolapp.model.*;

/**
 * The View interface defines the contract for displaying information and
 * interacting with the user.
 *
 * @author Serhii Bohdan
 */
public interface View {

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
     * Displays a list of groups.
     *
     * @param groups the list of groups to be displayed
     */
    void displayGroups(List<Group> groups);

    /**
     * Displays a list of students.
     *
     * @param students the list of students to be displayed
     */
    void displayStudents(List<Student> students);

}
