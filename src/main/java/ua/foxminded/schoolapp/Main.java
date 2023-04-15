package ua.foxminded.schoolapp;

import java.util.Scanner;
import ua.foxminded.schoolapp.cli.ConsoleManager;
import ua.foxminded.schoolapp.datageneration.DatabaseTableInitializer;

public class Main {

    private static DatabaseTableInitializer initializer = new DatabaseTableInitializer();
    private static ConsoleManager manager = new ConsoleManager();

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

                if(option == 1) {
                    System.out.print("Enter the number of students: ");
                    int numberOfStudents = scanner.nextInt();
                    System.out.println(manager.getGroupsWithGivenNumberStudents(numberOfStudents));
                } else if(option == 2) {
                    System.out.print("Enter the name of the course: ");
                    String courseName = scanner.next();
                    System.out.println(manager.getStudentsRelatedToCourse(courseName));
                } else if(option == 0) {
                    isRunning = false;
                } else {
                    System.out.println("There is no option that matches this number.");
                }
            }
        }
    }

}
