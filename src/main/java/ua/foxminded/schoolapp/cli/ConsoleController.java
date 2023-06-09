package ua.foxminded.schoolapp.cli;

import java.util.List;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.Service;

public class ConsoleController implements Controller {

    private Service service;
    private View view;

    public ConsoleController(Service service, View view) {
        this.service = service;
        this.view = view;
    }

    public void run() {
        boolean isRunning = true;
        view.showMenu();

        while (isRunning) {
            int option = view.getIntNumberFromUser("Select an option: ");

            if (option == 1) {
                findAllGroupsWithLessOrEqualStudentsNumber();
            } else if (option == 2) {
                findAllStudentsRelatedToCourseWithGivenName();
            } else if (option == 3) {
                addNewStudent();
            } else if (option == 4) {
                deleteStudent();
            } else if (option == 5) {
                addStudentToCourse();
            } else if (option == 6) {
                removeStudentFromOneOfTheirCourses();
            } else if (option == 0) {
                isRunning = false;
            } else {
                view.printMessage("There is no option that matches this number.\n");
            }
        }
    }

    private void findAllGroupsWithLessOrEqualStudentsNumber() {
        view.printMessage("\nYou want to know groups with a given and smaller number of students.");
        int numberOfStudents = view.getIntNumberFromUser("Enter the number of students:\u00A0");
        List<Group> groups = service.getGroupsWithGivenNumberStudents(numberOfStudents);
        view.displayGroups(groups);
    }

    private void findAllStudentsRelatedToCourseWithGivenName() {
        view.printMessage("\nYou want to know the list of students related to the course.");
        String courseName = view.getWordFromUser("Enter the name of the course:\u00A0");
        List<Student> students = service.getStudentsRelatedToCourse(courseName);
        view.displayStudents(students);
    }

    private void addNewStudent() {
        view.printMessage("\nYou want to add a new student.");
        String firstName = view.getWordFromUser("Enter the student's first name:\u00A0");
        String lastName = view.getWordFromUser("Enter the student's last name:\u00A0");
        int groupId = view.getIntNumberFromUser(
                "Enter the ID of the group to which the student should belong (from 1 to 10):\u00A0");
        service.addNewStudent(firstName, lastName, groupId);
        view.printMessage("The student has been successfully added.\n");
    }

    private void deleteStudent() {
        view.printMessage("\nYou want to delete a student by their ID.");
        int studentId = view.getIntNumberFromUser("Enter your student ID:\u00A0");
        Student student = service.findStudentById(studentId);
        String confirmationFromUser = view.getConfirmationFromUserAboutDeletingStudent(student);

        if ("Y".equals(confirmationFromUser)) {
            service.deleteStudentById(studentId);
            view.printMessage("The student was successfully deleted.\n");
        } else if ("N".equals(confirmationFromUser)) {
            view.printMessage("The student was not expelled.\n");
        } else {
            view.printMessage("There is no such option.\n");
        }
    }

    private void addStudentToCourse() {
        view.printMessage("\nYou want to add a student (from the list) to the course.");
        view.displayStudents(service.getAllStudents());
        String firstName = view.getWordFromUser("Enter the student's first name:\u00A0");
        String lastName = view.getWordFromUser("Enter the student's last name:\u00A0");
        String courseName = view.getWordFromUser("Enter the name of the course:\u00A0");
        service.addStudentToCourse(firstName, lastName, courseName);
        view.printMessage("The student has been successfully added to the course.\n");
    }

    private void removeStudentFromOneOfTheirCourses() {
        view.printMessage("\nYou want to delete a student from a course.");
        view.displayStudents(service.getAllStudents());
        String firstName = view.getWordFromUser("Enter the student's first name:\u00A0");
        String lastName = view.getWordFromUser("Enter the student's last name:\u00A0");
        String courseName = view.getWordFromUser("Enter the name of the course:\u00A0");
        service.deleteStudentFromCourse(firstName, lastName, courseName);
        view.printMessage("The student has been successfully deleted from the course.\n");
    }

}
