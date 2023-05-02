package ua.foxminded.schoolapp.cli.view;

import java.util.List;
import java.util.Scanner;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

public class ConsoleView implements View {

    private Scanner scanner = new Scanner(System.in);

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

    public int getChoise() {
        System.out.print("Select an option: ");
        return scanner.nextInt();
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public int getNumberOfStuentsFromUser() {
        System.out.print("Enter the number of students:\u00A0");
        return scanner.nextInt();
    }

    public String getCourseNameFromUser() {
        System.out.print("Enter the name of the course:\u00A0");
        return scanner.next();
    }

    public String getStudentFirstNameFromUser() {
        System.out.print("Enter the student's first name:\u00A0");
        return scanner.next();
    }

    public String getStudentLastNameFromUser() {
        System.out.print("Enter the student's last name:\u00A0");
        return scanner.next();
    }

    public int getGroupIdFromUser() {
        System.out.print("Enter the ID of the group to which the student should belong (from 1 to 10):\u00A0");
        return scanner.nextInt();
    }

    public int getStudentIdFromUser() {
        System.out.print("Enter your student ID:\u00A0");
        return scanner.nextInt();
    }

    public String getConfirmationFromUserAboutDeletingStudent(Student student) {
        String confirmationQuestion = String.format("Are you sure you want to delete a student %s %s?",
                student.getFirstName(), student.getLastName());
        System.out.print(confirmationQuestion + "\nPlease confirm your actions (enter Y or N):\u00A0");
        return scanner.next();
    }

    public void displayGroups(List<Group> groups) {
        if (groups.isEmpty()) {
            System.out.println("The list of groups is empty.");
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
        }

        StringBuilder sformatedStuentes = new StringBuilder("Students:");

        for (Student student : students) {
            sformatedStuentes.append(String.format("\n%-8s %s %s", "", student.getFirstName(), student.getLastName()));
        }
        System.out.println(sformatedStuentes + "\n");
    }

}
