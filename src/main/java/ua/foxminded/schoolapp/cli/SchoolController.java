package ua.foxminded.schoolapp.cli;

import java.util.List;
import java.util.Objects;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.Service;

public class SchoolController implements Controller {

    private Service service;
    private View view;

    public SchoolController(Service service, View view) {
        Objects.requireNonNull(service);
        Objects.requireNonNull(view);
        this.service = service;
        this.view = view;
    }

    public void runSchoolApp() {
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
        int numberOfStudents = view.getIntNumberFromUser("Enter the number of students (from 10 to 30):\u00A0");
        List<Group> groups = service.getGroupsWithGivenNumberStudents(numberOfStudents);

        if (Objects.isNull(groups)) {
            view.printMessage("""
                    The entered number of students is not correct.
                    The number of students should be between 10 and 30 inclusive.\n""");
        } else if (groups.isEmpty()) {
            view.printMessage("The list of groups is empty.\n");
        } else {
            view.displayGroups(groups);
        }
    }

    private void findAllStudentsRelatedToCourseWithGivenName() {
        view.printMessage("\nYou want to know the list of students related to the course.");
        String courseName = view.getWordFromUser("Enter the name of the course:\u00A0");
        List<Student> students = service.getStudentsRelatedToCourse(courseName);

        if (Objects.isNull(students)) {
            view.printMessage("A course with that name does not exist.\n");
        } else if (students.isEmpty()) {
            view.printMessage("The list of students is empty.\n");
        } else {
            view.displayStudents(students);
        }
    }

    private void addNewStudent() {
        view.printMessage("\nYou want to add a new student.");
        String firstName = view.getWordFromUser("Enter the student's first name:\u00A0");
        String lastName = view.getWordFromUser("Enter the student's last name:\u00A0");
        int groupId = view.getIntNumberFromUser(
                "Enter the ID of the group to which the student should belong (from 1 to 10):\u00A0");
        boolean newStudentIsAdded = service.addNewStudent(firstName, lastName, groupId);

        if (newStudentIsAdded) {
            view.printMessage("The student has been successfully added.\n");
        } else {
            view.printMessage("""
                    No new student was added. Perhaps a student with such data already exists.
                    Also check that the group ID is correct.\n""");
        }
    }

    private void deleteStudent() {
        view.printMessage("\nYou want to delete a student by their ID.");
        int studentId = view.getIntNumberFromUser("Enter your student ID:\u00A0");
        Student student = service.getStudentById(studentId);

        if(Objects.nonNull(student)) {
            String confirmationFromUser = view.getConfirmationFromUserAboutDeletingStudent(student);

            if ("Y".equals(confirmationFromUser)) {
                service.deleteStudentById(studentId);
                view.printMessage("The student was successfully deleted.\n");
            } else if ("N".equals(confirmationFromUser)) {
                view.printMessage("The student was not deleted.\n");
            } else {
                view.printMessage("There is no such option.\n");
            }
        } else {
            view.printMessage("There is no student with this ID.\n");
        }
    }

    private void addStudentToCourse() {
        view.printMessage("\nYou want to add a student (from the list) to the course.");
        view.displayStudents(service.getAllStudents());
        String firstName = view.getWordFromUser("Enter the student's first name:\u00A0");
        String lastName = view.getWordFromUser("Enter the student's last name:\u00A0");
        String courseName = view.getWordFromUser("Enter the name of the course:\u00A0");
        boolean studentIsAddedToCourse = service.addStudentToCourse(firstName, lastName, courseName);

        if (studentIsAddedToCourse) {
            view.printMessage("The student has been successfully added to the course.\n");
        } else {
            view.printMessage("""
                    The student has not been added to the course. Perhaps this student
                    does not exist, or he is already registered for this course. Also,
                    check whether the name of the course is entered correctly.\n""");
        }
    }

    private void removeStudentFromOneOfTheirCourses() {
        view.printMessage("\nYou want to delete a student from a course.");
        view.displayStudents(service.getAllStudents());
        String firstName = view.getWordFromUser("Enter the student's first name:\u00A0");
        String lastName = view.getWordFromUser("Enter the student's last name:\u00A0");
        String courseName = view.getWordFromUser("Enter the name of the course:\u00A0");
        boolean studentDeletedFromCourse = service.deleteStudentFromCourse(firstName, lastName, courseName);

        if(studentDeletedFromCourse) {
            view.printMessage("The student has been successfully deleted from the course.\n");
        } else {
            view.printMessage("""
                    The student was not deleted from the course. Perhaps this
                    student does not exist or is not registered in the specified course.
                    Also check the correctness of the entered course name.\n""");
        }
    }

}
