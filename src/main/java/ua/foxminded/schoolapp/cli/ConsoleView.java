package ua.foxminded.schoolapp.cli;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

public class ConsoleView implements View {

    private Scanner scanner;

    public ConsoleView(Scanner scanner) {
        Objects.requireNonNull(scanner);
        this.scanner = scanner;
    }

    public void showMenu() {
        System.out.println("""
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

    public void printMessage(String message) {
        System.out.println(message);
    }

    public int getIntNumberFromUser(String message) {
        System.out.print(message);
        return getIntInput();
    }

    public String getWordFromUser(String message) {
        System.out.print(message);
        return scanner.next();
    }

    public String getConfirmationFromUserAboutDeletingStudent(Student student) {
        String confirmationQuestion = String.format("Are you sure you want to delete a student %s %s?",
                student.getFirstName(), student.getLastName());
        System.out.print(confirmationQuestion + "\nPlease confirm your actions (enter Y or N):\u00A0");
        return scanner.next();
    }

    public void displayGroups(List<Group> groups) {
        if (groups.isEmpty()) {
            System.out.println("The list of groups is empty.\n");
            return;
        }

        StringBuilder formattedGroups = new StringBuilder("Groups:");

        for (Group group : groups) {
            formattedGroups.append(String.format("\n%-6s %s", "", group.getGroupName()));
        }
        System.out.println(formattedGroups + "\n");
    }

    public void displayStudents(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("The list of students is empty.");
            return;
        }

        StringBuilder sformatedStuentes = new StringBuilder("Students:");

        for (Student student : students) {
            sformatedStuentes.append(String.format("\n%-8s %s %s", "", student.getFirstName(), student.getLastName()));
        }
        System.out.println(sformatedStuentes + "\n");
    }

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
