package ua.foxminded.schoolapp.cli.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.springframework.stereotype.Controller;
import ua.foxminded.schoolapp.cli.SchoolController;
import ua.foxminded.schoolapp.cli.SchoolView;
import ua.foxminded.schoolapp.dto.CourseDto;
import ua.foxminded.schoolapp.dto.GroupDto;
import ua.foxminded.schoolapp.dto.StudentDto;
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
 * To interact with the School App, the user can run the application by calling
 * the {@link #runSchoolApp()} method. This method presents a menu to the user
 * with various options such as finding groups with a certain number of
 * students, adding or deleting students, managing courses, and more. The user
 * can input their choice by entering a corresponding number, and the
 * application will perform the requested action accordingly.
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
     * Constructs a SchoolController with the specified ServiceFacade and
     * SchoolView.
     *
     * @param serviceFacade the ServiceFacade to be used
     * @param view          the SchoolView to be used
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
                addStudentToCourse();
            } else if (option == 4) {
                deleteStudentFromOneOfTheirCourses();
            } else if (option == 5) {
                addNewGroup();
            } else if (option == 6) {
                updateGroupInfo();
            } else if (option == 7) {
                deleteGroup();
            } else if (option == 8) {
                addNewStudent();
            } else if (option == 9) {
                updateStudentInfo();
            } else if (option == 10) {
                deleteStudent();
            } else if (option == 11) {
                addNewCourse();
            } else if (option == 12) {
                updateCourseInfo();
            } else if (option == 13) {
                deleteCourseByName();
            } else if (option == 0) {
                isRunning = false;
            } else {
                view.printMessage("There is no option that matches this number." + NEW_LINE);
            }
        }
    }

    private void findAllGroupsWithLessOrEqualStudentsNumber() {
        view.printMessage(NEW_LINE + "You want to know groups with a given and smaller number of students.");
        int numberOfStudents = view.getIntNumberFromUser(NEW_LINE + "Enter the number of students:" + NON_BREAKING_SPACE);
        Map<GroupDto, Integer> groupsWithTheirNumberOfStudents = serviceFacade.getGroupsWithGivenNumberOfStudents(numberOfStudents);

        if (Objects.isNull(groupsWithTheirNumberOfStudents)) {
            view.printMessage("""
                    The entered number of students is not correct.
                    The number must be greater than or equal to zero.""" + NEW_LINE);
        } else if (groupsWithTheirNumberOfStudents.isEmpty()) {
            view.printMessage("The list of groups is empty." + NEW_LINE);
        } else {
            view.displayGroupsWithTheirNumberOfStudents(groupsWithTheirNumberOfStudents);
        }
    }

    private void findAllStudentsRelatedToCourseWithGivenName() {
        view.printMessage(NEW_LINE + "You want to know the list of students related to the course. All available courses:");
        view.displayCourses(serviceFacade.getAllCourses());
        String courseName = view.getSentenceFromUser(NEW_LINE + "Enter the name of the course:" + NON_BREAKING_SPACE);
        Map<StudentDto, Set<CourseDto>> studentsWithTheirCourses = serviceFacade.getStudentsWithCoursesByCourseName(courseName);

        if (Objects.isNull(studentsWithTheirCourses)) {
            view.printMessage("A course with that name does not exist." + NEW_LINE);
        } else if (studentsWithTheirCourses.isEmpty()) {
            view.printMessage("The list of students is empty." + NEW_LINE);
        } else {
            view.displayStudentsWithTheirCourses(studentsWithTheirCourses);
        }
    }

    private void addStudentToCourse() {
        view.printMessage(NEW_LINE + "You want to add a student (from the list) to the course." + NEW_LINE);
        view.displayStudentsWithTheirCourses(serviceFacade.getAllStudentsWithTheirCourses());
        String firstName = view.getSentenceFromUser(NEW_LINE + "Enter the student's first name:" + NON_BREAKING_SPACE);
        String lastName = view.getSentenceFromUser("Enter the student's last name:" + NON_BREAKING_SPACE);
        String courseName = view.getSentenceFromUser("Enter the name of the course:" + NON_BREAKING_SPACE);
        boolean studentIsAddedToCourse = serviceFacade.addStudentToCourse(firstName, lastName, courseName);

        if (studentIsAddedToCourse) {
            view.printMessage("The student has been successfully added to the course." + NEW_LINE);
        } else {
            view.printMessage("""
                    The student has not been added to the course. Possible causes of failure:
                    1. Invalid student name entered.
                    2. The course name you entered is invalid.
                    3. The student is already registered for the course.""" + NEW_LINE);
        }
    }

    private void deleteStudentFromOneOfTheirCourses() {
        view.printMessage(NEW_LINE + "You want to delete a student from a course." + NEW_LINE);
        view.displayStudentsWithTheirCourses(serviceFacade.getAllStudentsWithTheirCourses());
        String firstName = view.getSentenceFromUser(NEW_LINE + "Enter the student's first name:" + NON_BREAKING_SPACE);
        String lastName = view.getSentenceFromUser("Enter the student's last name:" + NON_BREAKING_SPACE);
        String courseName = view.getSentenceFromUser("Enter the name of the course:" + NON_BREAKING_SPACE);
        boolean studentDeletedFromCourse = serviceFacade.deleteStudentFromCourse(firstName, lastName, courseName);

        if (studentDeletedFromCourse) {
            view.printMessage("The student has been successfully deleted from the course." + NEW_LINE);
        } else {
            view.printMessage("""
                    The student was not deleted from the course. Possible causes of failure:
                    1. Invalid student name entered.
                    2. The course name you entered is invalid.
                    3. The student is not registered on the course.""" + NEW_LINE);
        }
    }

    private void addNewGroup() {
        view.printMessage(NEW_LINE + "You want to add a new group. All available groups:");
        view.displayGroups(serviceFacade.getAllGroups());
        String groupName = view.getSentenceFromUser(NEW_LINE + "Enter a name for the new group:" + NON_BREAKING_SPACE);
        boolean groupIsAdded = serviceFacade.addNewGroup(groupName);

        if (groupIsAdded) {
            view.printMessage("New group added successfully." + NEW_LINE);
        } else {
            view.printMessage("""
                    No new group has been added. Possible causes of failure:
                    1. The group name does not match the pattern.
                    2. A group with this name already exists.""" + NEW_LINE);
        }
    }

    private void updateGroupInfo() {
        view.printMessage(NEW_LINE + "You want to update the group information. All available groups:");
        view.displayGroups(serviceFacade.getAllGroups());
        String groupNameToUpdate = view
                .getSentenceFromUser(NEW_LINE + "Enter the name of the group you want to update:" + NON_BREAKING_SPACE);
        String newGroupName = view.getSentenceFromUser("Enter a new group name:" + NON_BREAKING_SPACE);
        boolean groupUpdated = serviceFacade.updateGroup(groupNameToUpdate, newGroupName);

        if (groupUpdated) {
            view.printMessage("The group information was successfully updated." + NEW_LINE);
        } else {
            view.printMessage("""
                    The group information has not been updated. Possible causes of failure:
                    1. The name of the group you want to update is invalid.
                    2. A group with the new name already exists.
                    3. The new name does not match the pattern.""" + NEW_LINE);
        }
    }

    private void deleteGroup() {
        view.printMessage(NEW_LINE + "You want to delete a group by its name. All available groups:");
        view.displayGroups(serviceFacade.getAllGroups());
        String groupName = view.getSentenceFromUser(NEW_LINE + "Enter the name of the group you want to delete:" + NON_BREAKING_SPACE);
        GroupDto group = serviceFacade.getGroupByName(groupName);

        if (Objects.nonNull(group)) {
            String confirmationFromUser = view.getConfirmationAboutDeletingGroup(group);

            if ("Y".equals(confirmationFromUser)) {
                serviceFacade.deleteGroupByName(groupName);
                view.printMessage("The group was successfully deleted." + NEW_LINE);
            } else if ("N".equals(confirmationFromUser)) {
                view.printMessage("The group was not deleted." + NEW_LINE);
            } else {
                view.printMessage("There is no such option." + NEW_LINE);
            }

        } else {
            view.printMessage("There is no group with this name." + NEW_LINE);
        }
    }

    private void addNewStudent() {
        view.printMessage(NEW_LINE + "You want to add a new student. All available groups:");
        view.displayGroups(serviceFacade.getAllGroups());
        String firstName = view.getSentenceFromUser(NEW_LINE + "Enter the student's first name:" + NON_BREAKING_SPACE);
        String lastName = view.getSentenceFromUser("Enter the student's last name:" + NON_BREAKING_SPACE);
        String groupName = view.getSentenceFromUser(
                "Enter the name of the group to which the student should belong:" + NON_BREAKING_SPACE);
        boolean newStudentIsAdded = serviceFacade.addNewStudent(firstName, lastName, groupName);

        if (newStudentIsAdded) {
            view.printMessage("The student has been successfully added." + NEW_LINE);
        } else {
            view.printMessage("""
                    No new student was added. Possible causes of failure:
                    1. A student with the entered name already exists.
                    2. The student's first or last name contains more than 25 characters.
                    3. There is no group with the entered name.""" + NEW_LINE);
        }
    }

    private void updateStudentInfo() {
        view.printMessage(NEW_LINE + "You want to update the student information. All available students with their groups:");
        Map<StudentDto, GroupDto> studentsWithTheirGroups = serviceFacade.getAllStudentsWithTheirGroups();
        view.displayStudentsWithTheirGroups(studentsWithTheirGroups);
        String studentFirstNameToUpdate = view.getSentenceFromUser(NEW_LINE
                + "Enter the fitst name of the student whose information you want to update:" + NON_BREAKING_SPACE);
        String studentLastNameToUpdate = view.getSentenceFromUser(
                "Enter the last name of the student whose information you want to update:" + NON_BREAKING_SPACE);
        String newStudentFirstName = view.getSentenceFromUser("Enter new student first name:" + NON_BREAKING_SPACE);
        String newStudentLastName = view.getSentenceFromUser("Enter new student last name:" + NON_BREAKING_SPACE);
        String newGroupName = view.getSentenceFromUser("Enter a new group name:" + NON_BREAKING_SPACE);
        boolean studentIsUpdated = serviceFacade.updateStudent(studentFirstNameToUpdate, studentLastNameToUpdate,
                newStudentFirstName, newStudentLastName, newGroupName);

        if (studentIsUpdated) {
            view.printMessage("Student information has been successfully updated." + NEW_LINE);
        } else {
            view.printMessage("""
                    Student information has not been updated. Possible causes of failure:
                    1. The student whose data you want to update does not exist.
                    2. The updated full name belongs to another student.
                    3. The updated student's first or last name contains more than 25 characters.
                    4. There is no group with the entered name.""" + NEW_LINE);
        }
    }

    private void deleteStudent() {
        view.printMessage(NEW_LINE + "You want to delete a student by their ID.");
        int studentId = view.getIntNumberFromUser(NEW_LINE + "Enter your student ID:" + NON_BREAKING_SPACE);
        StudentDto student = serviceFacade.getStudentById(studentId);

        if (Objects.nonNull(student)) {
            String confirmationFromUser = view.getConfirmationAboutDeletingStudent(student);

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

    private void addNewCourse() {
        view.printMessage(NEW_LINE + "You want to add a new course. All available courses:");
        view.displayCourses(serviceFacade.getAllCourses());
        String courseName = view.getSentenceFromUser(NEW_LINE + "Enter a name for the new course:" + NON_BREAKING_SPACE);
        String description = view.getSentenceFromUser("Enter a description for the new course:" + NON_BREAKING_SPACE);
        boolean newCourseIsAdded = serviceFacade.addNewCourse(courseName, description);

        if (newCourseIsAdded) {
            view.printMessage("New course added successfully." + NEW_LINE);
        } else {
            view.printMessage("""
                    No new course was added. Possible causes of failure:
                    1. A course with this name or description may already exist.
                    2. The course name contains more than 25 characters.""" + NEW_LINE);
        }
    }

    private void updateCourseInfo() {
        view.printMessage(NEW_LINE + "You want to update the course information. All available courses:");
        view.displayCourses(serviceFacade.getAllCourses());
        String courseNameToUpdate = view.getSentenceFromUser(NEW_LINE + "Enter the name of the course you want to update:" + NON_BREAKING_SPACE);
        String newCourseName = view.getSentenceFromUser("Enter a new course name:" + NON_BREAKING_SPACE);
        String newDescription = view.getSentenceFromUser("Enter a new course description:" + NON_BREAKING_SPACE);
        boolean courseIsUpdated = serviceFacade.updateCourse(courseNameToUpdate, newCourseName, newDescription);

        if (courseIsUpdated) {
            view.printMessage("Course information has been successfully updated." + NEW_LINE);
        } else {
            view.printMessage("""
                    Course information has not been updated. Possible causes of failure:
                    1. You have entered a course name that does not exist.
                    2. The updated course name contains more than 25 characters.
                    3. The updated course name or description belong to another course.""" + NEW_LINE);
        }
    }

    private void deleteCourseByName() {
        view.printMessage(NEW_LINE + "You want to delete the course by its name. All available courses:");
        view.displayCourses(serviceFacade.getAllCourses());
        String courseName = view.getSentenceFromUser(
                NEW_LINE + "Enter the name of the course you want to delete:" + NON_BREAKING_SPACE);
        CourseDto course = serviceFacade.getCourseByName(courseName);

        if (Objects.nonNull(course)) {
            String confirmationFromUser = view.getConfirmationAboutDeletingCourse(course);

            if ("Y".equals(confirmationFromUser)) {
                serviceFacade.deleteCourseByName(courseName);
                view.printMessage("The course was successfully deleted." + NEW_LINE);
            } else if ("N".equals(confirmationFromUser)) {
                view.printMessage("The course was not deleted." + NEW_LINE);
            } else {
                view.printMessage("There is no such option." + NEW_LINE);
            }

        } else {
            view.printMessage("There is no course with this name." + NEW_LINE);
        }
    }

}
