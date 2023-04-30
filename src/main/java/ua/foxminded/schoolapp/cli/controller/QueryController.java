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
                int numberOfStudents = view.getNumberOfStuentsFromUser();
                List<Group> groups = validator.getGroupsWithGivenNumberStudents(numberOfStudents);
                view.displayGroups(groups);
            } else if (option == 2) {
                String courseName = view.getCourseNameFromUser();
                List<Student> students = validator.getStudentsRelatedToCourse(courseName);
                view.displayStudents(students);
            } else if (option == 3) {
                String firstName = view.getStudentFirstNameFromUser();
                String lastName = view.getStudentLastNameFromUser();
                int groupId = view.getGroupIdFromUser();
                validator.addNewStudent(firstName, lastName, groupId);
                view.printMessage("The student has been successfully added.");
            } else if (option == 0) {
                isRunning = false;
            } else {
                System.out.println("There is no option that matches this number.");
            }

        }

    }

}
