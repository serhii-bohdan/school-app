package ua.foxminded.schoolapp.cli.controller;

import java.util.List;
import ua.foxminded.schoolapp.cli.view.View;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

public class QueryController implements Controller {

    private Validator validator;
    private View view;

    public QueryController(Validator validator, View view) {
        this.validator = validator;
        this.view = view;
    }

    public void run() {
        boolean isRunning = true;
        view.showMenu();

        while (isRunning) {
            int option = view.getChoise();

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
        int numberOfStudents = view.getNumberOfStuentsFromUser();
        List<Group> groups = validator.getGroupsWithGivenNumberStudents(numberOfStudents);
        view.displayGroups(groups);
    }

    private void findAllStudentsRelatedToCourseWithGivenName() {
        view.printMessage("\nYou want to know the list of students related to the course.");
        String courseName = view.getCourseNameFromUser();
        List<Student> students = validator.getStudentsRelatedToCourse(courseName);
        view.displayStudents(students);
    }

    private void addNewStudent() {
        view.printMessage("\nYou want to add a new student.");
        String firstName = view.getStudentFirstNameFromUser();
        String lastName = view.getStudentLastNameFromUser();
        int groupId = view.getGroupIdFromUser();
        validator.addNewStudent(firstName, lastName, groupId);
        view.printMessage("The student has been successfully added.\n");
    }

    private void deleteStudent() {
        view.printMessage("\nYou want to delete a student by their ID.");
        int studentId = view.getStudentIdFromUser();
        Student student = validator.findStudentById(studentId);
        String confirmationFromUser = view.getConfirmationFromUserAboutDeletingStudent(student);

        if ("Y".equals(confirmationFromUser)) {
            validator.deleteStudentById(studentId);
            view.printMessage("The student was successfully deleted.\n");
        } else if ("N".equals(confirmationFromUser)) {
            view.printMessage("The student was not expelled.\n");
        } else {
            view.printMessage("There is no such option.\n");
        }
    }

    private void addStudentToCourse() {
        view.printMessage("\nYou want to add a student (from the list) to the course.");
        view.displayStudents(validator.getAllStudents());
        String firstName = view.getStudentFirstNameFromUser();
        String lastName = view.getStudentLastNameFromUser();
        String coerseName = view.getCourseNameFromUser();
        validator.addStudentToCourse(firstName, lastName, coerseName);
        view.printMessage("The student has been successfully added to the course.\n");
    }

    private void removeStudentFromOneOfTheirCourses() {
        view.printMessage("\nYou want to delete a student from a course.");
        view.displayStudents(validator.getAllStudents());
        String firstName = view.getStudentFirstNameFromUser();
        String lastName = view.getStudentLastNameFromUser();
        String coerseName = view.getCourseNameFromUser();
        validator.deleteStudentFromCourse(firstName, lastName, coerseName);
        view.printMessage("The student has been successfully deleted from the course.\n");
    }

}
