package ua.foxminded.schoolapp.cli;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.Service;

class SchoolControllerTest {

    Controller controller;
    View viewMock;
    Service serviceMock;

    @BeforeEach
    void setUp() {
        viewMock = mock(View.class);
        serviceMock = mock(Service.class);
    }

    @Test
    void schoolController_shouldNullPointerException_whenServiceIsNull() {
        assertThrows(NullPointerException.class, () -> new SchoolController(null, viewMock));
    }

    @Test
    void schoolController_shouldNullPointerException_whenViewIsNull() {
        assertThrows(NullPointerException.class, () -> new SchoolController(serviceMock, null));
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAoutIncorrectnessEnteredNumber_whenSelectedFirstOptionAndGroupsListIsNull() {
        controller = new SchoolController(serviceMock, viewMock);
        int numberGreaterThanMaximumStudentsCountInGroup = 34;
        when(viewMock.getIntNumberFromUser("Select an option: ")).thenReturn(1, 0);
        when(viewMock.getIntNumberFromUser("Enter the number of students:\u00A0"))
                .thenReturn(numberGreaterThanMaximumStudentsCountInGroup);
        when(serviceMock.getGroupsWithGivenNumberStudents(numberGreaterThanMaximumStudentsCountInGroup))
                .thenReturn(null);

        controller.runSchoolApp();

        verify(viewMock).printMessage("""
                The entered number of students is not correct.
                The number of students should be between 0 and 30 inclusive.\n""");
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatGroupsListEmpty_whenSelectedFirstOptionAndGroupsListEmpty() {
        controller = new SchoolController(serviceMock, viewMock);
        int verySmallStudentsNumber = 10;
        when(viewMock.getIntNumberFromUser("Select an option: ")).thenReturn(1, 0);
        when(viewMock.getIntNumberFromUser("Enter the number of students:\u00A0")).thenReturn(verySmallStudentsNumber);
        when(serviceMock.getGroupsWithGivenNumberStudents(verySmallStudentsNumber)).thenReturn(new ArrayList<>());

        controller.runSchoolApp();

        verify(viewMock).printMessage("The list of groups is empty.\n");
    }

    @Test
    void runSchoolApp_shouldDisplayedGroupsNames_whenSelectedFirstOptionAndGroupsListContainsSomeGroups() {
        controller = new SchoolController(serviceMock, viewMock);
        int verySmallStudentsNumber = 10;
        List<Group> groups = new ArrayList<>();
        groups.add(new Group("FG-62"));
        groups.add(new Group("LK-62"));
        groups.add(new Group("QW-62"));
        when(viewMock.getIntNumberFromUser("Select an option: ")).thenReturn(1, 0);
        when(viewMock.getIntNumberFromUser("Enter the number of students:\u00A0")).thenReturn(verySmallStudentsNumber);
        when(serviceMock.getGroupsWithGivenNumberStudents(verySmallStudentsNumber)).thenReturn(groups);

        controller.runSchoolApp();

        verify(viewMock).displayGroups(groups);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatCourseNotExist_whenSelectedSecondOptionAndEnteredCourseNameNotExist() {
        controller = new SchoolController(serviceMock, viewMock);
        String nonExistentCourseName = "NonExistentCourseName";
        when(viewMock.getIntNumberFromUser("Select an option: ")).thenReturn(2, 0);
        when(viewMock.getWordFromUser("Enter the name of the course:\u00A0")).thenReturn(nonExistentCourseName);
        when(serviceMock.getStudentsRelatedToCourse(nonExistentCourseName)).thenReturn(null);

        controller.runSchoolApp();

        verify(viewMock).printMessage("A course with that name does not exist.\n");
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentsListEmpty_whenSelectedSecondOptionAndStudentsListEmpty() {
        controller = new SchoolController(serviceMock, viewMock);
        String existentCourseName = "ExistentCourseName";
        when(viewMock.getIntNumberFromUser("Select an option: ")).thenReturn(2, 0);
        when(viewMock.getWordFromUser("Enter the name of the course:\u00A0")).thenReturn(existentCourseName);
        when(serviceMock.getStudentsRelatedToCourse(existentCourseName)).thenReturn(new ArrayList<>());

        controller.runSchoolApp();

        verify(viewMock).printMessage("The list of students is empty.\n");
    }

    @Test
    void runSchoolApp_shouldDisplayedStudentsNames_whenSelectedSecondOptionAndEnteredExistentCourseName() {
        controller = new SchoolController(serviceMock, viewMock);
        String existentCourseName = "ExistentCourseName";
        List<Student> students = new ArrayList<>();
        students.add(new Student("FirstName_1", "LastName_1", 1));
        students.add(new Student("FirstName_2", "LastName_2", 1));
        students.add(new Student("FirstName_3", "LastName_3", 1));
        when(viewMock.getIntNumberFromUser("Select an option: ")).thenReturn(2, 0);
        when(viewMock.getWordFromUser("Enter the name of the course:\u00A0")).thenReturn(existentCourseName);
        when(serviceMock.getStudentsRelatedToCourse(existentCourseName)).thenReturn(students);

        controller.runSchoolApp();

        verify(viewMock).displayStudents(students);
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentWasSuccessfullyAdded_whenSelectedThirdOptionAndStudentWithEnteredDataSuccessfullAddedToDatabase() {
        controller = new SchoolController(serviceMock, viewMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        int studentGroupId = 1;
        when(viewMock.getIntNumberFromUser("Select an option: ")).thenReturn(3, 0);
        when(viewMock.getWordFromUser("Enter the student's first name:\u00A0")).thenReturn(studentFirstName);
        when(viewMock.getWordFromUser("Enter the student's last name:\u00A0")).thenReturn(studentLastName);
        when(viewMock.getIntNumberFromUser(
                "Enter the ID of the group to which the student should belong (from 1 to 10):\u00A0"))
                .thenReturn(studentGroupId);
        when(serviceMock.addNewStudent(studentFirstName, studentLastName, studentGroupId)).thenReturn(true);

        controller.runSchoolApp();

        verify(viewMock).printMessage("The student has been successfully added.\n");
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentNotAddedToDatabase_whenSelectedThirdOptionAndІtudentWithEnteredDataAlreadyPresentInDatabase() {
        controller = new SchoolController(serviceMock, viewMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        int studentGroupId = 1;
        when(viewMock.getIntNumberFromUser("Select an option: ")).thenReturn(3, 0);
        when(viewMock.getWordFromUser("Enter the student's first name:\u00A0")).thenReturn(studentFirstName);
        when(viewMock.getWordFromUser("Enter the student's last name:\u00A0")).thenReturn(studentLastName);
        when(viewMock.getIntNumberFromUser(
                "Enter the ID of the group to which the student should belong (from 1 to 10):\u00A0"))
                .thenReturn(studentGroupId);
        when(serviceMock.addNewStudent(studentFirstName, studentLastName, studentGroupId)).thenReturn(false);

        controller.runSchoolApp();

        verify(viewMock).printMessage("""
                No new student was added. Perhaps a student with such data already exists.
                Also check that the group ID is correct.\n""");
    }

    @Test
    void runSchoolApp_shouldDeletedStudentFromDatabase_whenSelectedFourthOptionAndStudentWithEnteredIdExistsAndUserConfirmedDeletion() {
        controller = new SchoolController(serviceMock, viewMock);
        int studentId = 1;
        Student student = new Student("FirstName", "LastName", 2);
        student.setId(studentId);
        String сonfirmationToDeleteStudent = "Y";
        when(viewMock.getIntNumberFromUser("Select an option: ")).thenReturn(4, 0);
        when(viewMock.getIntNumberFromUser("Enter your student ID:\u00A0")).thenReturn(studentId);
        when(serviceMock.getStudentById(studentId)).thenReturn(student);
        when(viewMock.getConfirmationFromUserAboutDeletingStudent(student)).thenReturn(сonfirmationToDeleteStudent);

        controller.runSchoolApp();

        verify(serviceMock).deleteStudentById(studentId);
        verify(viewMock).printMessage("The student was successfully deleted.\n");
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentNotDeletedFromDatabase_whenSelectedFourthOptionAndStudentWithEnteredIdExistsAndUserNotConfirmedDeletion() {
        controller = new SchoolController(serviceMock, viewMock);
        int studentId = 1;
        Student student = new Student("FirstName", "LastName", 2);
        student.setId(studentId);
        String disagreementToDeleteStudent = "N";
        when(viewMock.getIntNumberFromUser("Select an option: ")).thenReturn(4, 0);
        when(viewMock.getIntNumberFromUser("Enter your student ID:\u00A0")).thenReturn(studentId);
        when(serviceMock.getStudentById(studentId)).thenReturn(student);
        when(viewMock.getConfirmationFromUserAboutDeletingStudent(student)).thenReturn(disagreementToDeleteStudent);

        controller.runSchoolApp();

        verify(viewMock).printMessage("The student was not deleted.\n");
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatNoEnteredOption_whenSelectedFourthOptionAndStudentWithEnteredIdExistsAndUserEnteredNonExistentOption() {
        controller = new SchoolController(serviceMock, viewMock);
        int studentId = 1;
        Student student = new Student("FirstName", "LastName", 2);
        student.setId(studentId);
        String nonExistentOption = "NonExistentOption";
        when(viewMock.getIntNumberFromUser("Select an option: ")).thenReturn(4, 0);
        when(viewMock.getIntNumberFromUser("Enter your student ID:\u00A0")).thenReturn(studentId);
        when(serviceMock.getStudentById(studentId)).thenReturn(student);
        when(viewMock.getConfirmationFromUserAboutDeletingStudent(student)).thenReturn(nonExistentOption);

        controller.runSchoolApp();

        verify(viewMock).printMessage("There is no such option.\n");
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatNoExistStudentWithEnteredId_whenSelectedFourthOptionAndNoExistStudentWithEnteredId() {
        controller = new SchoolController(serviceMock, viewMock);
        int studentId = 1;
        when(viewMock.getIntNumberFromUser("Select an option: ")).thenReturn(4, 0);
        when(viewMock.getIntNumberFromUser("Enter your student ID:\u00A0")).thenReturn(studentId);
        when(serviceMock.getStudentById(studentId)).thenReturn(null);

        controller.runSchoolApp();

        verify(viewMock).printMessage("There is no student with this ID.\n");
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentSuccessfullyAddedToCourse_whenSelectedFifthOptionAndStudentSuccessfullyAddedToCourse() {
        controller = new SchoolController(serviceMock, viewMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        when(viewMock.getIntNumberFromUser("Select an option: ")).thenReturn(5, 0);
        when(viewMock.getWordFromUser("Enter the student's first name:\u00A0")).thenReturn(studentFirstName);
        when(viewMock.getWordFromUser("Enter the student's last name:\u00A0")).thenReturn(studentLastName);
        when(viewMock.getWordFromUser("Enter the name of the course:\u00A0")).thenReturn(courseName);
        when(serviceMock.addStudentToCourse(studentFirstName, studentLastName, courseName)).thenReturn(true);

        controller.runSchoolApp();

        verify(viewMock).printMessage("The student has been successfully added to the course.\n");
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentNotAddedToCourse_whenSelectedFifthOptionAndStudentAlreadyAttendingCourseOrEnteredIncorrectStudentName() {
        controller = new SchoolController(serviceMock, viewMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        when(viewMock.getIntNumberFromUser("Select an option: ")).thenReturn(5, 0);
        when(viewMock.getWordFromUser("Enter the student's first name:\u00A0")).thenReturn(studentFirstName);
        when(viewMock.getWordFromUser("Enter the student's last name:\u00A0")).thenReturn(studentLastName);
        when(viewMock.getWordFromUser("Enter the name of the course:\u00A0")).thenReturn(courseName);
        when(serviceMock.addStudentToCourse(studentFirstName, studentLastName, courseName)).thenReturn(false);

        controller.runSchoolApp();

        verify(viewMock).printMessage("""
                The student has not been added to the course. Perhaps this student
                does not exist, or he is already registered for this course. Also,
                check whether the name of the course is entered correctly.\n""");
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentSuccessfullyDeletedFromCourse_whenSelectedSixthOptionAndStudentSuccessfullyDeletedFromCourse() {
        controller = new SchoolController(serviceMock, viewMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        when(viewMock.getIntNumberFromUser("Select an option: ")).thenReturn(6, 0);
        when(viewMock.getWordFromUser("Enter the student's first name:\u00A0")).thenReturn(studentFirstName);
        when(viewMock.getWordFromUser("Enter the student's last name:\u00A0")).thenReturn(studentLastName);
        when(viewMock.getWordFromUser("Enter the name of the course:\u00A0")).thenReturn(courseName);
        when(serviceMock.deleteStudentFromCourse(studentFirstName, studentLastName, courseName)).thenReturn(true);

        controller.runSchoolApp();

        verify(viewMock).printMessage("The student has been successfully deleted from the course.\n");
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatStudentNotDeletedFromCourse_whenSelectedSixthOptionAndStudentNotPreviouslyAttendedCourseOrEnteredIncorrectStudentName() {
        controller = new SchoolController(serviceMock, viewMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        when(viewMock.getIntNumberFromUser("Select an option: ")).thenReturn(6, 0);
        when(viewMock.getWordFromUser("Enter the student's first name:\u00A0")).thenReturn(studentFirstName);
        when(viewMock.getWordFromUser("Enter the student's last name:\u00A0")).thenReturn(studentLastName);
        when(viewMock.getWordFromUser("Enter the name of the course:\u00A0")).thenReturn(courseName);
        when(serviceMock.addStudentToCourse(studentFirstName, studentLastName, courseName)).thenReturn(false);

        controller.runSchoolApp();

        verify(viewMock).printMessage("""
                The student was not deleted from the course. Perhaps this
                student does not exist or is not registered in the specified course.
                Also check the correctness of the entered course name.\n""");
    }

    @Test
    void runSchoolApp_shouldPrintedMessageAboutFactThatNoOptionThatCorrespondToEnteredNumber_whenEnteredOptionWhichNotExist() {
        controller = new SchoolController(serviceMock, viewMock);
        when(viewMock.getIntNumberFromUser("Select an option: ")).thenReturn(8, 0);

        controller.runSchoolApp();

        verify(viewMock).printMessage("There is no option that matches this number.\n");
    }

}
