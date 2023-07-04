package ua.foxminded.schoolapp.cli;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

/**
 * The SchoolView class implements the {@link View} interface and provides the
 * functionality to display information and interact with the user through the
 * console.
 *
 * @author Serhii Bohdan
 */
public class SchoolView implements View {

    /**
     * A constant representing a new line character.
     */
    public static final String NEW_LINE = "\n";

    /**
     * A constant representing a non-breaking space character.
     */
    public static final String NON_BREAKING_SPACE = "\u00A0";

    private Scanner scanner;

    /**
     * Constructs a new SchoolView with the specified scanner.
     *
     * @param scanner the scanner to be used for user input
     * @throws NullPointerException if the scanner is null
     */
    public SchoolView(Scanner scanner) {
        Objects.requireNonNull(scanner);
        this.scanner = scanner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMenu() {
        System.out.print("""
                            **************************
                            -----   SCHOOL APP   -----
                            **************************
                1. Find all groups with less or equal students’ number.
                2. Find all students related to the course with the given name.
                3. Add a new student.
                4. Delete a student.
                5. Add a student to the course.
                6. Remove the student from one of their courses.

                Enter 0 to exit the program.
                """);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printMessage(String message) {
        System.out.print(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntNumberFromUser(String message) {
        System.out.print(message);
        return getIntInput();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWordFromUser(String message) {
        System.out.print(message);
        return scanner.next();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfirmationFromUserAboutDeletingStudent(Student student) {
        String confirmationQuestion = String.format("Are you sure you want to delete a student %s %s?",
                student.getFirstName(), student.getLastName());
        System.out.print(confirmationQuestion + NEW_LINE + "Please confirm your actions (enter Y or N):" + NON_BREAKING_SPACE);
        return scanner.next();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayGroups(List<Group> groups) {
        StringBuilder formattedGroups = new StringBuilder("Groups:");

        for (Group group : groups) {
            formattedGroups.append(String.format(NEW_LINE + "%-6s %s", "", group.getGroupName()));
        }
        System.out.print(formattedGroups + NEW_LINE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayStudents(List<Student> students) {
        StringBuilder sformatedStuentes = new StringBuilder("Students:");

        for (Student student : students) {
            sformatedStuentes.append(String.format(NEW_LINE + "%-8s %s %s", "", student.getFirstName(), student.getLastName()));
        }
        System.out.print(sformatedStuentes + NEW_LINE);
    }

    /**
     * Gets an integer input from the scanner.
     *
     * @return the integer input from the scanner
     */
    private int getIntInput() {
        while (true) {

            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            } else {
                System.out.print("Invalid input. Please enter a valid integer value: ");
                scanner.next();
            }
        }
    }

}
