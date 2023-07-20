package ua.foxminded.schoolapp.cli;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.ServiceFacade;

class SchoolControllerTest {

    static final String NEW_LINE = "\n";
    static final String NON_BREAKING_SPACE = "\u00A0";

    Controller controller;
    View viewMock;
    ServiceFacade serviceFacadeMock;

    @BeforeEach
    void setUp() {
        viewMock = mock(View.class);
        serviceFacadeMock = mock(ServiceFacade.class);
    }

    @Test
    void schoolController_shouldNullPointerException_whenServiceFacadeIsNull() {
        assertThrows(NullPointerException.class, () -> new SchoolController(null, viewMock));
    }

    @Test
    void schoolController_shouldNullPointerException_whenViewIsNull() {
        assertThrows(NullPointerException.class, () -> new SchoolController(serviceFacadeMock, null));
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutIncorrectnessEnteredNumber_whenSelectedFirstOptionAndMapWithGroupsAndTheirNumberOfStudentsIsNull() {
        controller = new SchoolController(serviceFacadeMock, viewMock);
        int numberGreaterThanMaximumStudentsCountInGroup = 34;
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Select an option: ")).thenReturn(1, 0);
        when(viewMock
                .getIntNumberFromUser(NEW_LINE + "Enter the number of students (from 10 to 30):" + NON_BREAKING_SPACE))
                .thenReturn(numberGreaterThanMaximumStudentsCountInGroup);
        when(serviceFacadeMock.getGroupsWithGivenNumberOfStudents(numberGreaterThanMaximumStudentsCountInGroup))
                .thenReturn(null);

        controller.runSchoolApp();

        verify(viewMock)
                .printMessage(NEW_LINE + "You want to know groups with a given and smaller number of students.");
        verify(viewMock).printMessage("""
                The entered number of students is not correct.
                The number of students should be between 10 and 30 inclusive.""" + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatGroupsListEmpty_whenSelectedFirstOptionAndMapWithGroupsAndTheirNumberOfStudentsEmpty() {
        controller = new SchoolController(serviceFacadeMock, viewMock);
        int verySmallStudentsNumber = 10;
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Select an option: ")).thenReturn(1, 0);
        when(viewMock.getIntNumberFromUser("Enter the number of students:" + NON_BREAKING_SPACE))
                .thenReturn(verySmallStudentsNumber);
        when(serviceFacadeMock.getGroupsWithGivenNumberOfStudents(verySmallStudentsNumber)).thenReturn(new HashMap<>());

        controller.runSchoolApp();

        verify(viewMock)
                .printMessage(NEW_LINE + "You want to know groups with a given and smaller number of students.");
        verify(viewMock).printMessage("The list of groups is empty." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldDisplayedGroupsNamesWithTheirNumberOfStudents_whenSelectedFirstOptionAndMapWithGroupsAndTheirNumberOfStudentsContainsSomeGroups() {
        controller = new SchoolController(serviceFacadeMock, viewMock);
        int studentsNumber = 18;
        Map<Group, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
        groupsWithTheirNumberOfStudents.put(new Group("FG-62"), 17);
        groupsWithTheirNumberOfStudents.put(new Group("LK-56"), 15);
        groupsWithTheirNumberOfStudents.put(new Group("QW-62"), 11);
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Select an option: ")).thenReturn(1, 0);
        when(viewMock
                .getIntNumberFromUser(NEW_LINE + "Enter the number of students (from 10 to 30):" + NON_BREAKING_SPACE))
                .thenReturn(studentsNumber);
        when(serviceFacadeMock.getGroupsWithGivenNumberOfStudents(studentsNumber))
                .thenReturn(groupsWithTheirNumberOfStudents);

        controller.runSchoolApp();

        verify(viewMock)
                .printMessage(NEW_LINE + "You want to know groups with a given and smaller number of students.");
        verify(viewMock).displayGroupsWithTheirNumberOfStudents(groupsWithTheirNumberOfStudents);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatCourseNotExist_whenSelectedSecondOptionAndEnteredCourseNameNotExist() {
        controller = new SchoolController(serviceFacadeMock, viewMock);
        String nonExistentCourseName = "NonExistentCourseName";
        List<Course> allCourses = new ArrayList<>();
        allCourses.add(new Course("CourseName_1", "Decription_1"));
        allCourses.add(new Course("CourseName_2", "Decription_2"));
        allCourses.add(new Course("CourseName_3", "Decription_3"));
        allCourses.add(new Course("CourseName_4", "Decription_4"));
        allCourses.add(new Course("CourseName_5", "Decription_5"));
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Select an option: ")).thenReturn(2, 0);
        when(viewMock.getWordFromUser(NEW_LINE + "Enter the name of the course:" + NON_BREAKING_SPACE))
                .thenReturn(nonExistentCourseName);
        when(serviceFacadeMock.getAllCourses()).thenReturn(allCourses);
        when(serviceFacadeMock.getStudentsWithCoursesByCourseName(nonExistentCourseName)).thenReturn(null);

        controller.runSchoolApp();

        verify(viewMock)
                .printMessage(NEW_LINE + "You want to know the list of students related to the course." + NEW_LINE);
        verify(viewMock).displayCourses(allCourses);
        verify(viewMock).printMessage("A course with that name does not exist." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentsListEmpty_whenSelectedSecondOptionAndMapWithStudentsAndTheirCoursesEmpty() {
        controller = new SchoolController(serviceFacadeMock, viewMock);
        String existentCourseName = "CourseName_1";
        List<Course> allCourses = new ArrayList<>();
        allCourses.add(new Course("CourseName_1", "Decription_1"));
        allCourses.add(new Course("CourseName_2", "Decription_2"));
        allCourses.add(new Course("CourseName_3", "Decription_3"));
        allCourses.add(new Course("CourseName_4", "Decription_4"));
        allCourses.add(new Course("CourseName_5", "Decription_5"));
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Select an option: ")).thenReturn(2, 0);
        when(viewMock.getWordFromUser(NEW_LINE + "Enter the name of the course:" + NON_BREAKING_SPACE))
                .thenReturn(existentCourseName);
        when(serviceFacadeMock.getAllCourses()).thenReturn(allCourses);
        when(serviceFacadeMock.getStudentsWithCoursesByCourseName(existentCourseName)).thenReturn(new HashMap<>());

        controller.runSchoolApp();

        verify(viewMock)
                .printMessage(NEW_LINE + "You want to know the list of students related to the course." + NEW_LINE);
        verify(viewMock).displayCourses(allCourses);
        verify(viewMock).printMessage("The list of students is empty." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldDisplayedStudentsNamesWithTheirCourses_whenSelectedSecondOptionAndEnteredExistentCourseName() {
        controller = new SchoolController(serviceFacadeMock, viewMock);
        String existentCourseName = "CourseName_1";
        Map<Student, List<Course>> studentWithTheirCourses = new HashMap<>();
        Student student = new Student("FirstName_1", "LastName_1", 1);
        Course firstCourse = new Course("CourseName_1", "Description_1");
        Course secondCourse = new Course("CourseName_2", "Description_2");
        List<Course> coursesForStudent = new ArrayList<>();
        coursesForStudent.add(firstCourse);
        coursesForStudent.add(secondCourse);
        studentWithTheirCourses.put(student, coursesForStudent);
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Select an option: ")).thenReturn(2, 0);
        when(viewMock.getWordFromUser(NEW_LINE + "Enter the name of the course:" + NON_BREAKING_SPACE))
                .thenReturn(existentCourseName);
        when(serviceFacadeMock.getAllCourses()).thenReturn(coursesForStudent);
        when(serviceFacadeMock.getStudentsWithCoursesByCourseName(existentCourseName))
                .thenReturn(studentWithTheirCourses);

        controller.runSchoolApp();

        verify(viewMock)
                .printMessage(NEW_LINE + "You want to know the list of students related to the course." + NEW_LINE);
        verify(viewMock).displayCourses(coursesForStudent);
        verify(viewMock).displayStudentsWithTheirCourses(studentWithTheirCourses);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentWasSuccessfullyAdded_whenSelectedThirdOptionAndStudentWithEnteredDataSuccessfullAddedToDatabase() {
        controller = new SchoolController(serviceFacadeMock, viewMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        int studentGroupId = 1;
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Select an option: ")).thenReturn(3, 0);
        when(viewMock.getWordFromUser(NEW_LINE + "Enter the student's first name:" + NON_BREAKING_SPACE))
                .thenReturn(studentFirstName);
        when(viewMock.getWordFromUser("Enter the student's last name:" + NON_BREAKING_SPACE))
                .thenReturn(studentLastName);
        when(viewMock.getIntNumberFromUser(
                "Enter the ID of the group to which the student should belong (from 1 to 10):" + NON_BREAKING_SPACE))
                .thenReturn(studentGroupId);
        when(serviceFacadeMock.addNewStudent(studentFirstName, studentLastName, studentGroupId)).thenReturn(true);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to add a new student.");
        verify(viewMock).printMessage("The student has been successfully added." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentNotAddedToDatabase_whenSelectedThirdOptionAndStudentWithEnteredDataAlreadyPresentInDatabase() {
        controller = new SchoolController(serviceFacadeMock, viewMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        int studentGroupId = 1;
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Select an option: ")).thenReturn(3, 0);
        when(viewMock.getWordFromUser(NEW_LINE + "Enter the student's first name:" + NON_BREAKING_SPACE))
                .thenReturn(studentFirstName);
        when(viewMock.getWordFromUser("Enter the student's last name:" + NON_BREAKING_SPACE))
                .thenReturn(studentLastName);
        when(viewMock.getIntNumberFromUser(
                "Enter the ID of the group to which the student should belong (from 1 to 10):" + NON_BREAKING_SPACE))
                .thenReturn(studentGroupId);
        when(serviceFacadeMock.addNewStudent(studentFirstName, studentLastName, studentGroupId)).thenReturn(false);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to add a new student.");
        verify(viewMock).printMessage("""
                No new student was added. Perhaps a student with such data already exists.
                Also check that the group ID is correct.""" + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldDeletedStudentFromDatabase_whenSelectedFourthOptionAndStudentWithEnteredIdExistsAndUserConfirmedDeletion() {
        controller = new SchoolController(serviceFacadeMock, viewMock);
        int studentId = 1;
        Student student = new Student("FirstName", "LastName", 2);
        student.setId(studentId);
        String сonfirmationToDeleteStudent = "Y";
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Select an option: ")).thenReturn(4, 0);
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Enter your student ID:" + NON_BREAKING_SPACE))
                .thenReturn(studentId);
        when(serviceFacadeMock.getStudentById(studentId)).thenReturn(student);
        when(viewMock.getConfirmationFromUserAboutDeletingStudent(student)).thenReturn(сonfirmationToDeleteStudent);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to delete a student by their ID.");
        verify(serviceFacadeMock).deleteStudentById(studentId);
        verify(viewMock).printMessage("The student was successfully deleted." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentNotDeletedFromDatabase_whenSelectedFourthOptionAndStudentWithEnteredIdExistsAndUserNotConfirmedDeletion() {
        controller = new SchoolController(serviceFacadeMock, viewMock);
        int studentId = 1;
        Student student = new Student("FirstName", "LastName", 2);
        student.setId(studentId);
        String disagreementToDeleteStudent = "N";
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Select an option: ")).thenReturn(4, 0);
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Enter your student ID:" + NON_BREAKING_SPACE))
                .thenReturn(studentId);
        when(serviceFacadeMock.getStudentById(studentId)).thenReturn(student);
        when(viewMock.getConfirmationFromUserAboutDeletingStudent(student)).thenReturn(disagreementToDeleteStudent);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to delete a student by their ID.");
        verify(viewMock).printMessage("The student was not deleted." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatNoEnteredOption_whenSelectedFourthOptionAndStudentWithEnteredIdExistsAndUserEnteredNonExistentOption() {
        controller = new SchoolController(serviceFacadeMock, viewMock);
        int studentId = 1;
        Student student = new Student("FirstName", "LastName", 2);
        student.setId(studentId);
        String nonExistentOption = "NonExistentOption";
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Select an option: ")).thenReturn(4, 0);
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Enter your student ID:" + NON_BREAKING_SPACE))
                .thenReturn(studentId);
        when(serviceFacadeMock.getStudentById(studentId)).thenReturn(student);
        when(viewMock.getConfirmationFromUserAboutDeletingStudent(student)).thenReturn(nonExistentOption);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to delete a student by their ID.");
        verify(viewMock).printMessage("There is no such option." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatNoExistStudentWithEnteredId_whenSelectedFourthOptionAndNoExistStudentWithEnteredId() {
        controller = new SchoolController(serviceFacadeMock, viewMock);
        int studentId = 1;
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Select an option: ")).thenReturn(4, 0);
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Enter your student ID:" + NON_BREAKING_SPACE))
                .thenReturn(studentId);
        when(serviceFacadeMock.getStudentById(studentId)).thenReturn(null);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to delete a student by their ID.");
        verify(viewMock).printMessage("There is no student with this ID." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentSuccessfullyAddedToCourse_whenSelectedFifthOptionAndStudentSuccessfullyAddedToCourse() {
        controller = new SchoolController(serviceFacadeMock, viewMock);
        Map<Student, List<Course>> studentWithTheirCourses = new HashMap<>();
        Student student = new Student("FirstName_1", "LastName_1", 1);
        Course firstCourse = new Course("CourseName_1", "Description_1");
        Course secondCourse = new Course("CourseName_2", "Description_2");
        List<Course> coursesForStudent = new ArrayList<>();
        coursesForStudent.add(firstCourse);
        coursesForStudent.add(secondCourse);
        studentWithTheirCourses.put(student, coursesForStudent);
        String studentFirstName = "FirstName_1";
        String studentLastName = "LastName_1";
        String courseName = "CourseName_3";
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Select an option: ")).thenReturn(5, 0);
        when(viewMock.getWordFromUser(NEW_LINE + "Enter the student's first name:" + NON_BREAKING_SPACE))
                .thenReturn(studentFirstName);
        when(viewMock.getWordFromUser("Enter the student's last name:" + NON_BREAKING_SPACE))
                .thenReturn(studentLastName);
        when(viewMock.getWordFromUser("Enter the name of the course:" + NON_BREAKING_SPACE)).thenReturn(courseName);
        when(serviceFacadeMock.getAllStudentsWithTheirCourses()).thenReturn(studentWithTheirCourses);
        when(serviceFacadeMock.addStudentToCourse(studentFirstName, studentLastName, courseName)).thenReturn(true);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to add a student (from the list) to the course." + NEW_LINE);
        viewMock.displayStudentsWithTheirCourses(studentWithTheirCourses);
        verify(viewMock).printMessage("The student has been successfully added to the course." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentNotAddedToCourse_whenSelectedFifthOptionAndStudentAlreadyAttendingCourseOrEnteredIncorrectStudentName() {
        controller = new SchoolController(serviceFacadeMock, viewMock);
        Map<Student, List<Course>> studentWithTheirCourses = new HashMap<>();
        Student student = new Student("FirstName_1", "LastName_1", 1);
        Course firstCourse = new Course("CourseName_1", "Description_1");
        Course secondCourse = new Course("CourseName_2", "Description_2");
        List<Course> coursesForStudent = new ArrayList<>();
        coursesForStudent.add(firstCourse);
        coursesForStudent.add(secondCourse);
        studentWithTheirCourses.put(student, coursesForStudent);
        String studentFirstName = "FirstName_1";
        String studentLastName = "LastName_1";
        String courseName = "CourseName_1";
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Select an option: ")).thenReturn(5, 0);
        when(viewMock.getWordFromUser(NEW_LINE + "Enter the student's first name:" + NON_BREAKING_SPACE))
                .thenReturn(studentFirstName);
        when(viewMock.getWordFromUser("Enter the student's last name:" + NON_BREAKING_SPACE))
                .thenReturn(studentLastName);
        when(viewMock.getWordFromUser("Enter the name of the course:" + NON_BREAKING_SPACE)).thenReturn(courseName);
        when(serviceFacadeMock.getAllStudentsWithTheirCourses()).thenReturn(studentWithTheirCourses);
        when(serviceFacadeMock.addStudentToCourse(studentFirstName, studentLastName, courseName)).thenReturn(false);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to add a student (from the list) to the course." + NEW_LINE);
        viewMock.displayStudentsWithTheirCourses(studentWithTheirCourses);
        verify(viewMock).printMessage("""
                The student has not been added to the course. Perhaps this student
                does not exist, or he is already registered for this course. Also,
                check whether the name of the course is entered correctly.""" + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentSuccessfullyDeletedFromCourse_whenSelectedSixthOptionAndStudentSuccessfullyDeletedFromCourse() {
        controller = new SchoolController(serviceFacadeMock, viewMock);
        Map<Student, List<Course>> studentWithTheirCourses = new HashMap<>();
        Student student = new Student("FirstName_1", "LastName_1", 1);
        Course firstCourse = new Course("CourseName_1", "Description_1");
        Course secondCourse = new Course("CourseName_2", "Description_2");
        List<Course> coursesForStudent = new ArrayList<>();
        coursesForStudent.add(firstCourse);
        coursesForStudent.add(secondCourse);
        studentWithTheirCourses.put(student, coursesForStudent);
        String studentFirstName = "FirstName_1";
        String studentLastName = "LastName_1";
        String courseName = "CourseName_1";
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Select an option: ")).thenReturn(6, 0);
        when(viewMock.getWordFromUser(NEW_LINE + "Enter the student's first name:" + NON_BREAKING_SPACE))
                .thenReturn(studentFirstName);
        when(viewMock.getWordFromUser("Enter the student's last name:" + NON_BREAKING_SPACE))
                .thenReturn(studentLastName);
        when(viewMock.getWordFromUser("Enter the name of the course:" + NON_BREAKING_SPACE)).thenReturn(courseName);
        when(serviceFacadeMock.getAllStudentsWithTheirCourses()).thenReturn(studentWithTheirCourses);
        when(serviceFacadeMock.deleteStudentFromCourse(studentFirstName, studentLastName, courseName)).thenReturn(true);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to delete a student from a course." + NEW_LINE);
        viewMock.displayStudentsWithTheirCourses(studentWithTheirCourses);
        verify(viewMock).printMessage("The student has been successfully deleted from the course." + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentNotDeletedFromCourse_whenSelectedSixthOptionAndStudentNotPreviouslyAttendedCourseOrEnteredIncorrectStudentName() {
        controller = new SchoolController(serviceFacadeMock, viewMock);
        Map<Student, List<Course>> studentWithTheirCourses = new HashMap<>();
        Student student = new Student("FirstName_1", "LastName_1", 1);
        Course firstCourse = new Course("CourseName_1", "Description_1");
        Course secondCourse = new Course("CourseName_2", "Description_2");
        List<Course> coursesForStudent = new ArrayList<>();
        coursesForStudent.add(firstCourse);
        coursesForStudent.add(secondCourse);
        studentWithTheirCourses.put(student, coursesForStudent);
        String studentFirstName = "FirstName_1";
        String studentLastName = "LastName_1";
        String courseName = "CourseName_3";
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Select an option: ")).thenReturn(6, 0);
        when(viewMock.getWordFromUser(NEW_LINE + "Enter the student's first name:" + NON_BREAKING_SPACE))
                .thenReturn(studentFirstName);
        when(viewMock.getWordFromUser("Enter the student's last name:" + NON_BREAKING_SPACE))
                .thenReturn(studentLastName);
        when(viewMock.getWordFromUser("Enter the name of the course:" + NON_BREAKING_SPACE)).thenReturn(courseName);
        when(serviceFacadeMock.getAllStudentsWithTheirCourses()).thenReturn(studentWithTheirCourses);
        when(serviceFacadeMock.addStudentToCourse(studentFirstName, studentLastName, courseName)).thenReturn(false);

        controller.runSchoolApp();

        verify(viewMock).printMessage(NEW_LINE + "You want to delete a student from a course." + NEW_LINE);
        viewMock.displayStudentsWithTheirCourses(studentWithTheirCourses);
        verify(viewMock).printMessage("""
                The student was not deleted from the course. Perhaps this
                student does not exist or is not registered in the specified course.
                Also check the correctness of the entered course name.""" + NEW_LINE);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatNoOptionThatCorrespondToEnteredNumber_whenEnteredOptionWhichNotExist() {
        controller = new SchoolController(serviceFacadeMock, viewMock);
        when(viewMock.getIntNumberFromUser(NEW_LINE + "Select an option: ")).thenReturn(8, 0);

        controller.runSchoolApp();

        verify(viewMock).printMessage("There is no option that matches this number." + NEW_LINE);
    }

}
