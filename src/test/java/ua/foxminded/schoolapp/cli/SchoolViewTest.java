package ua.foxminded.schoolapp.cli;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

class SchoolViewTest {

    View view;
    Scanner scannerMock;
    ByteArrayOutputStream baos;
    PrintStream printStreamMock;

    @BeforeEach
    void setUp() {
        scannerMock = mock(Scanner.class);
        baos = new ByteArrayOutputStream();
        printStreamMock = new PrintStream(baos);
        System.setOut(printStreamMock);
    }

    @Test
    void schoolView_shouldNullPointerException_whenScannerIsNull() {
        assertThrows(NullPointerException.class, () -> new SchoolView(null));
    }

    @Test
    void showMenu_shouldDisplayedProgramMenu_whenInvokeShowMenu() {
        view = new SchoolView(scannerMock);
        String expectedOutput = """
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
                \r\n""";

        view.showMenu();
        String actualOutput = baos.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void printMessage_shouldPrintedNullWord_whenMessageIsNull() {
        view = new SchoolView(scannerMock);
        String expectedOutput = "null\r\n";

        view.printMessage(null);
        String actualOutput = baos.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void printMessage_shouldPrintedOnlyCRLF_whenMessageIsEmptyString() {
        view = new SchoolView(scannerMock);
        String expectedOutput = "\r\n";

        view.printMessage("");
        String actualOutput = baos.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void printMessage_shouldPrintedWord_whenMessageIsOnlyOneWord() {
        view = new SchoolView(scannerMock);
        String expectedOutput = "Word\r\n";

        view.printMessage("Word");
        String actualOutput = baos.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void getIntNumberFromUser_shouldReturnedIntegerNumberOnFirstAttempt_whenScannerMockReturnIntegerInput() {
        view = new SchoolView(scannerMock);
        int expectedIntFromUser = 4;
        when(scannerMock.hasNextInt()).thenReturn(true);
        when(scannerMock.nextInt()).thenReturn(expectedIntFromUser);

        int actualIntFromUser = view.getIntNumberFromUser("Message");

        verify(scannerMock, times(1)).hasNextInt();
        verify(scannerMock, times(1)).nextInt();
        assertEquals(expectedIntFromUser, actualIntFromUser);
    }

    @Test
    void getIntNumberFromUser_shouldReturnedIntegerNumberOnSecondAttempt_whenScannerMockReturnIncorrectValueOnFirstAttempt() {
        view = new SchoolView(scannerMock);
        int expectedIntFromUser = 3;
        when(scannerMock.hasNextInt()).thenReturn(false, true);
        when(scannerMock.next()).thenReturn("Not integer");
        when(scannerMock.nextInt()).thenReturn(expectedIntFromUser);

        int actualIntFromUser = view.getIntNumberFromUser("Message");

        verify(scannerMock, times(2)).hasNextInt();
        verify(scannerMock, times(1)).next();
        verify(scannerMock, times(1)).nextInt();
        assertEquals(expectedIntFromUser, actualIntFromUser);
    }

    @Test
    void getWordFromUser_shouldReturnedEmptyString_whenScannerMockReturEmptyString() {
        view = new SchoolView(scannerMock);
        String expectedReturnedWord = "";
        when(scannerMock.next()).thenReturn(expectedReturnedWord);

        String actualReturnedWord = view.getWordFromUser("Message");

        assertEquals(expectedReturnedWord, actualReturnedWord);
    }

    @Test
    void getWordFromUser_shouldReturnedOnlySpaces_whenScannerMockReturnOnlySpaces() {
        view = new SchoolView(scannerMock);
        String expectedReturnedWord = "          ";
        when(scannerMock.next()).thenReturn(expectedReturnedWord);

        String actualReturnedWord = view.getWordFromUser("Message");

        assertEquals(expectedReturnedWord, actualReturnedWord);
    }

    @Test
    void getWordFromUser_shouldReturnedSimpleWord_whenScannerMockReturnSimpleWord() {
        view = new SchoolView(scannerMock);
        String expectedReturnedWord = "Art";
        when(scannerMock.next()).thenReturn(expectedReturnedWord);

        String actualReturnedWord = view.getWordFromUser("Message");

        assertEquals(expectedReturnedWord, actualReturnedWord);
    }

    @Test
    void getConfirmationFromUserAboutDeletingStudent_shouldNullPointerException_whenStudnetIsNull() {
        view = new SchoolView(scannerMock);

        assertThrows(NullPointerException.class, () -> view.getConfirmationFromUserAboutDeletingStudent(null));
    }

    @Test
    void getConfirmationFromUserAboutDeletingStudent_shouldConfirmationMessageWithNullAndNull_whenStudentFieldsNotInitialized() {
        view = new SchoolView(scannerMock);
        Student student = new Student();
        String expectedConfirmationMessage = "Are you sure you want to delete a student null null?\n"
                                           + "Please confirm your actions (enter Y or N): ";
        when(scannerMock.next()).thenReturn("Y");

        view.getConfirmationFromUserAboutDeletingStudent(student);
        String actualConfirmationMessage = baos.toString();

        assertEquals(expectedConfirmationMessage, actualConfirmationMessage);
    }

    @Test
    void getConfirmationFromUserAboutDeletingStudent_shouldConfirmationMessageWithStudentFirstNameAndLastName_whenStudentHasInitializedFfirstAndLastNamesFields() {
        view = new SchoolView(scannerMock);
        Student student = new Student("FirstName", "LastName", 1);
        String expectedConfirmationMessage = "Are you sure you want to delete a student FirstName LastName?\n"
                                           + "Please confirm your actions (enter Y or N): ";
        when(scannerMock.next()).thenReturn("Y");

        view.getConfirmationFromUserAboutDeletingStudent(student);
        String actualConfirmationMessage = baos.toString();

        assertEquals(expectedConfirmationMessage, actualConfirmationMessage);
    }

    @Test
    void getConfirmationFromUserAboutDeletingStudent_shouldReturnedOneCharacterConfirmation_whenScannerMockReturnOneCharacter() {
        view = new SchoolView(scannerMock);
        Student student = new Student("FirstName", "LastName", 1);
        String expectedConfirmationFromUser = "Y";
        when(scannerMock.next()).thenReturn(expectedConfirmationFromUser);

        String actualConfirmationFromUser = view.getConfirmationFromUserAboutDeletingStudent(student);

        assertEquals(expectedConfirmationFromUser, actualConfirmationFromUser);
    }

    @Test
    void getConfirmationFromUserAboutDeletingStudent_shouldReturnedNullConfirmation_whenScannerMockReturnNull() {
        view = new SchoolView(scannerMock);
        Student student = new Student("FirstName", "LastName", 1);
        String expectedConfirmationFromUser = null;
        when(scannerMock.next()).thenReturn(expectedConfirmationFromUser);

        String actualConfirmationFromUser = view.getConfirmationFromUserAboutDeletingStudent(student);

        assertEquals(expectedConfirmationFromUser, actualConfirmationFromUser);
    }

    @Test
    void displayGroups_shouldNullPointerException_whenGroupsListIsNull() {
        view = new SchoolView(scannerMock);

        assertThrows(NullPointerException.class, () -> view.displayGroups(null));
    }

    @Test
    void displayGroups_shouldNullPointerException_whenGroupsListContainsNull() {
        view = new SchoolView(scannerMock);
        List<Group> groups = new ArrayList<>();
        groups.add(null);

        assertThrows(NullPointerException.class, () -> view.displayGroups(groups));
    }

    @Test
    void displayGroups_shouldDisplayNothingBesidesGroupsWord_whenGroupsListEmpty() {
        view = new SchoolView(scannerMock);
        String expectedOutputMessage = "Groups:\n"
                                     + "\r\n";
        List<Group> groups = new ArrayList<>();

        view.displayGroups(groups);
        String actualOutputMessage = baos.toString();

        assertEquals(expectedOutputMessage, actualOutputMessage);
    }

    @Test
    void displayGroups_shouldDisplayedGroupsList_whenGroupsListContainsThreeGroupsWithCorrectNames() {
        view = new SchoolView(scannerMock);
        List<Group> groups = new ArrayList<>();
        groups.add(new Group("JR-84"));
        groups.add(new Group("QL-03"));
        groups.add(new Group("VA-72"));
        String expectedDisplayedGroups = "Groups:\n"
                                       + "       JR-84\n"
                                       + "       QL-03\n"
                                       + "       VA-72\n"
                                       + "\r\n";

        view.displayGroups(groups);
        String actualDisplayedGroups = baos.toString();

        assertEquals(expectedDisplayedGroups, actualDisplayedGroups);
    }

    @Test
    void displayGroups_shouldDisplayedGroupsList_whenGroupsListContainsThreeGroupsWithNotInitializedNames() {
        view = new SchoolView(scannerMock);
        List<Group> groups = new ArrayList<>();
        groups.add(new Group());
        groups.add(new Group());
        groups.add(new Group());
        String expectedDisplayedGroups = "Groups:\n"
                                       + "       null\n"
                                       + "       null\n"
                                       + "       null\n"
                                       + "\r\n";

        view.displayGroups(groups);
        String actualDisplayedGroups = baos.toString();

        assertEquals(expectedDisplayedGroups, actualDisplayedGroups);
    }

    @Test
    void displayStudents_shouldNullPointerException_whenStudentsListIsNull() {
        view = new SchoolView(scannerMock);

        assertThrows(NullPointerException.class, () -> view.displayStudents(null));
    }

    @Test
    void displayStudents_shouldNullPointerException_whenStudentsListContainsNull() {
        view = new SchoolView(scannerMock);
        List<Student> students = new ArrayList<>();
        students.add(null);

        assertThrows(NullPointerException.class, () -> view.displayStudents(students));
    }

    @Test
    void displayStudents_shouldDisplayNothingBesidesStudentsWord_whenStudentsListEmpty() {
        view = new SchoolView(scannerMock);
        String expectedOutputMessage = "Students:\n"
                                     + "\r\n";
        List<Student> students = new ArrayList<>();

        view.displayStudents(students);
        String actualOutputMessage = baos.toString();

        assertEquals(expectedOutputMessage, actualOutputMessage);
    }

    @Test
    void displayStudents_shouldDisplayedStudentsList_whenStudentsListContainsThreeStudentsWithCorrectNames() {
        view = new SchoolView(scannerMock);
        List<Student> students = new ArrayList<>();
        students.add(new Student("FirstName_1", "LastName_1", 1));
        students.add(new Student("FirstName_2", "LastName_2", 1));
        students.add(new Student("FirstName_3", "LastName_3", 1));
        String expectedDisplayedStudents = "Students:\n"
                                         + "         FirstName_1 LastName_1\n"
                                         + "         FirstName_2 LastName_2\n"
                                         + "         FirstName_3 LastName_3\n"
                                         + "\r\n";

        view.displayStudents(students);
        String actualDisplayedStudents = baos.toString();

        assertEquals(expectedDisplayedStudents, actualDisplayedStudents);
    }

    @Test
    void displayStudents_shouldDisplayedStudentsList_whenStudentsListContainsThreeStudentsWithNotInitializedNames() {
        view = new SchoolView(scannerMock);
        List<Student> students = new ArrayList<>();
        students.add(new Student(null, null, 1));
        students.add(new Student(null, null, 1));
        students.add(new Student(null, null, 1));
        String expectedDisplayedStudents = "Students:\n"
                                         + "         null null\n"
                                         + "         null null\n"
                                         + "         null null\n"
                                         + "\r\n";

        view.displayStudents(students);
        String actualDisplayedStudents = baos.toString();

        assertEquals(expectedDisplayedStudents, actualDisplayedStudents);
    }

    @AfterEach
    void tearDown() {
        System.setOut(System.out);
    }

}
