package ua.foxminded.schoolapp.cli.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Controller;
import ua.foxminded.schoolapp.cli.SchoolController;
import ua.foxminded.schoolapp.cli.SchoolView;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.logic.ServiceFacade;

/**
 * The SchoolControllerImpl class implements the {@link SchoolController}
 * interface and provides the functionality to run the School App and handle
 * user interactions.
 * <p>
 * This class is annotated with {@code @Controller} to indicate that it is a
 * Spring controller.
 * <p>
 * The SchoolControllerImpl requires an instance of {@link ServiceFacade} for
 * accessing the business logic and an instance of {@link SchoolView} for user
 * interactions and displaying information.
 * <p>
 * The class contains methods to run the School App, handle user options from
 * the main menu, and interact with the user through the console. It provides
 * functionality to find groups with a given number of students, find students
 * related to a specific course, add new students, delete students, add students
 * to courses, and delete students from courses.
 *
 * @author Serhii Bohdan
 */
@Controller
public class SchoolControllerImpl implements SchoolController {

    /**
     * A constant representing a new line character.
     */
    private static final String NEW_LINE = "\n";

    /**
     * A constant representing a non-breaking space character.
     */
    private static final String NON_BREAKING_SPACE = "\u00A0";

    private final ServiceFacade serviceFacade;
    private final SchoolView view;

    /**
     * Constructs a SchoolController with the specified ServiceFacade and View.
     *
     * @param serviceFacade the ServiceFacade to be used
     * @param view          the View to be used
     */
    public SchoolControllerImpl(ServiceFacade serviceFacade, SchoolView view) {
        this.serviceFacade = serviceFacade;
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runSchoolApp() {
        boolean isRunning = true;

        while (isRunning) {
            int option = view.getIntNumberFromUser(NEW_LINE + "Select an option: ");

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
                deleteStudentFromOneOfTheirCourses();
            } else if (option == 0) {
                isRunning = false;
            } else {
                view.printMessage("There is no option that matches this number." + NEW_LINE);
            }
        }
    }

    private void findAllGroupsWithLessOrEqualStudentsNumber() {
        view.printMessage(NEW_LINE + "You want to know groups with a given and smaller number of students.");
        int numberOfStudents = view.getIntNumberFromUser(NEW_LINE + "Enter the number of students (from 10 to 30):" + NON_BREAKING_SPACE);
        Map<Group, Integer> groupsWithTheirNumberOfStudents = serviceFacade.getGroupsWithGivenNumberOfStudents(numberOfStudents);

        if (Objects.isNull(groupsWithTheirNumberOfStudents)) {
            view.printMessage("""
                    The entered number of students is not correct.
                    The number of students should be between 10 and 30 inclusive.""" + NEW_LINE);
        } else if (groupsWithTheirNumberOfStudents.isEmpty()) {
            view.printMessage("The list of groups is empty." + NEW_LINE);
        } else {
            view.displayGroupsWithTheirNumberOfStudents(groupsWithTheirNumberOfStudents);
        }
    }

    private void findAllStudentsRelatedToCourseWithGivenName() {
        view.printMessage(NEW_LINE + "You want to know the list of students related to the course." + NEW_LINE);
        view.displayCourses(serviceFacade.getAllCourses());
        String courseName = view.getWordFromUser(NEW_LINE + "Enter the name of the course:" + NON_BREAKING_SPACE);
        Map<Student, List<Course>> studentsWithTheirCourses = serviceFacade.getStudentsWithCoursesByCourseName(courseName);

        if (Objects.isNull(studentsWithTheirCourses)) {
            view.printMessage("A course with that name does not exist." + NEW_LINE);
        } else if (studentsWithTheirCourses.isEmpty()) {
            view.printMessage("The list of students is empty." + NEW_LINE);
        } else {
            view.displayStudentsWithTheirCourses(studentsWithTheirCourses);
        }
    }

    private void addNewStudent() {
        view.printMessage(NEW_LINE + "You want to add a new student.");
        String firstName = view.getWordFromUser(NEW_LINE + "Enter the student's first name:" + NON_BREAKING_SPACE);
        String lastName = view.getWordFromUser("Enter the student's last name:" + NON_BREAKING_SPACE);
        int groupId = view.getIntNumberFromUser(
                "Enter the ID of the group to which the student should belong (from 1 to 10):" + NON_BREAKING_SPACE);
        boolean newStudentIsAdded = serviceFacade.addNewStudent(firstName, lastName, groupId);

        if (newStudentIsAdded) {
            view.printMessage("The student has been successfully added." + NEW_LINE);
        } else {
            view.printMessage("""
                    No new student was added. Perhaps a student with such data already exists.
                    Also check that the group ID is correct.""" + NEW_LINE);
        }
    }

    private void deleteStudent() {
        view.printMessage(NEW_LINE + "You want to delete a student by their ID.");
        int studentId = view.getIntNumberFromUser(NEW_LINE + "Enter your student ID:" + NON_BREAKING_SPACE);
        Student student = serviceFacade.getStudentById(studentId);

        if (Objects.nonNull(student)) {
            String confirmationFromUser = view.getConfirmationFromUserAboutDeletingStudent(student);

            if ("Y".equals(confirmationFromUser)) {
                serviceFacade.deleteStudentById(studentId);
                view.printMessage("The student was successfully deleted." + NEW_LINE);
            } else if ("N".equals(confirmationFromUser)) {
                view.printMessage("The student was not deleted." + NEW_LINE);
            } else {
                view.printMessage("There is no such option." + NEW_LINE);
            }
        } else {
            view.printMessage("There is no student with this ID." + NEW_LINE);
        }
    }

    private void addStudentToCourse() {
        view.printMessage(NEW_LINE + "You want to add a student (from the list) to the course." + NEW_LINE);
        view.displayStudentsWithTheirCourses(serviceFacade.getAllStudentsWithTheirCourses());
        String firstName = view.getWordFromUser(NEW_LINE + "Enter the student's first name:" + NON_BREAKING_SPACE);
        String lastName = view.getWordFromUser("Enter the student's last name:" + NON_BREAKING_SPACE);
        String courseName = view.getWordFromUser("Enter the name of the course:" + NON_BREAKING_SPACE);
        boolean studentIsAddedToCourse = serviceFacade.addStudentToCourse(firstName, lastName, courseName);

        if (studentIsAddedToCourse) {
            view.printMessage("The student has been successfully added to the course." + NEW_LINE);
        } else {
            view.printMessage("""
                    The student has not been added to the course. Perhaps this student
                    does not exist, or he is already registered for this course. Also,
                    check whether the name of the course is entered correctly.""" + NEW_LINE);
        }
    }

    private void deleteStudentFromOneOfTheirCourses() {
        view.printMessage(NEW_LINE + "You want to delete a student from a course." + NEW_LINE);
        view.displayStudentsWithTheirCourses(serviceFacade.getAllStudentsWithTheirCourses());
        String firstName = view.getWordFromUser(NEW_LINE + "Enter the student's first name:" + NON_BREAKING_SPACE);
        String lastName = view.getWordFromUser("Enter the student's last name:" + NON_BREAKING_SPACE);
        String courseName = view.getWordFromUser("Enter the name of the course:" + NON_BREAKING_SPACE);
        boolean studentDeletedFromCourse = serviceFacade.deleteStudentFromCourse(firstName, lastName, courseName);

        if (studentDeletedFromCourse) {
            view.printMessage("The student has been successfully deleted from the course." + NEW_LINE);
        } else {
            view.printMessage("""
                    The student was not deleted from the course. Perhaps this
                    student does not exist or is not registered in the specified course.
                    Also check the correctness of the entered course name.""" + NEW_LINE);
        }
    }

}
