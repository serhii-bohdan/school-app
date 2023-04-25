package ua.foxminded.schoolapp;

import java.util.List;
import java.util.Scanner;
import ua.foxminded.schoolapp.cli.ConsoleManager;
import ua.foxminded.schoolapp.datageneration.DatabaseTableInitializer;
import ua.foxminded.schoolapp.entity.Group;
import ua.foxminded.schoolapp.entity.Student;
import ua.foxminded.schoolapp.cli.*;

public class Main {

    private static DatabaseTableInitializer initializer = new DatabaseTableInitializer();
    private static ConsoleManager manager = new ConsoleManager();
    private static Formatter formatter = new Formatter();

    public static void main(String[] args) {
        initializer.initialize();
        boolean isRunning = true;

        try (Scanner scanner = new Scanner(System.in)) {
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

            while (isRunning) {

                System.out.print("Select an option: ");
                int option = scanner.nextInt();

                if (option == 1) {
                    System.out.print("""
                            \nYou want to know groups with a given and smaller number of students.
                            Enter the number of students:\u00A0""");
                    int numberOfStudents = scanner.nextInt();
                    List<Group> groups = manager.getGroupsWithGivenNumberStudents(numberOfStudents);
                    System.out.println(formatter.formatGroups(groups) + "\n");
                } else if (option == 2) {
                    System.out.print("""
                            \nYou want to know the list of students related to the course.
                            Enter the name of the course:\u00A0""");
                    String courseName = scanner.next();
                    List<Student> students = manager.getStudentsRelatedToCourse(courseName);
                    System.out.println(formatter.formatStudentsRelatedToCourse(students) + "\n");
                } else if (option == 0) {
                    isRunning = false;
                } else {
                    System.out.println("There is no option that matches this number.");
                }
            }
        }
    }

}
