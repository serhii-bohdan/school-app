package ua.foxminded.schoolapp.cli.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.schoolapp.dto.CourseDto;
import ua.foxminded.schoolapp.dto.GroupDto;
import ua.foxminded.schoolapp.dto.StudentDto;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Student;

@SpringBootTest(classes = { SchoolViewImpl.class })
class SchoolViewImplTest {

    private ByteArrayOutputStream baos;
    private PrintStream printStreamMock;

    @MockBean
    private Scanner scannerMock;

    @Autowired
    private SchoolViewImpl view;

    @BeforeEach
    void setUp() {
        baos = new ByteArrayOutputStream();
        printStreamMock = new PrintStream(baos);
        System.setOut(printStreamMock);
    }

    @Test
    void showMenu_shouldDisplayedAplicationMenu_whenInvokeShowMenu() {
        String expectedOutput = """
                             **************************
                             -----   SCHOOL APP   -----
                             **************************
                 1. Find all groups with less or equal students’ number.
                 2. Find all students related to the course with the given name.
                 3. Add a student to the course.
                 4. Remove the student from one of their courses.
                 5. Add a new group.
                 6. Update group information.
                 7. Delete a group.
                 8. Add a new student.
                 9. Update student information.
                10. Delete a student.
                11. Add a new course.
                12. Update course information.
                13. Delete a course.

                Enter 0 to exit the program.
                """;

        view.showMenu();
        String actualOutput = baos.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void printMessage_shouldPrintedNullWord_whenMessageIsNull() {
        String expectedOutput = "null";

        view.printMessage(null);
        String actualOutput = baos.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void printMessage_shouldPrintedOnlyCRLF_whenMessageIsEmptyString() {
        String expectedOutput = "";

        view.printMessage("");
        String actualOutput = baos.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void printMessage_shouldPrintedWord_whenMessageIsOnlyOneWord() {
        String expectedOutput = "Word";

        view.printMessage("Word");
        String actualOutput = baos.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void getIntNumberFromUser_shouldReturnedIntegerNumberOnFirstAttempt_whenScannerMockReturnIntegerInput() {
        Integer expectedIntFromUser = 4;
        String message = "Message";
        when(scannerMock.nextLine()).thenReturn(String.valueOf(expectedIntFromUser));

        Integer actualIntFromUser = view.getIntNumberFromUser(message);

        verify(scannerMock, times(1)).nextLine();
        assertEquals(expectedIntFromUser, actualIntFromUser);
    }

    @Test
    void getIntNumberFromUser_shouldReturnedIntegerNumberOnSecondAttempt_whenInputIsNotNumber() {
        Integer expectedIntFromUser = 3;
        when(scannerMock.nextLine()).thenReturn("Not integer", String.valueOf(expectedIntFromUser));

        Integer actualIntFromUser = view.getIntNumberFromUser("Message");

        verify(scannerMock, times(2)).nextLine();
        assertEquals(expectedIntFromUser, actualIntFromUser);
    }

    @Test
    void getIntNumberFromUser_shouldReturnedZero_whenScannerThrowNoSuchElementException() {
        Integer expectedIntFromUser = 0;
        when(scannerMock.nextLine()).thenThrow(NoSuchElementException.class);

        Integer actualIntFromUser = view.getIntNumberFromUser("Message");

        verify(scannerMock, times(1)).nextLine();
        assertEquals(expectedIntFromUser, actualIntFromUser);
    }

    @Test
    void getSentenceFromUser_shouldReturnedEmptyString_whenScannerMockReturEmptyString() {
        String expectedReturnedWord = "";
        when(scannerMock.nextLine()).thenReturn(expectedReturnedWord);

        String actualReturnedWord = view.getSentenceFromUser("Message");

        verify(scannerMock, times(1)).nextLine();
        assertEquals(expectedReturnedWord, actualReturnedWord);
    }

    @Test
    void getSentenceFromUser_shouldReturnedOnlySpaces_whenScannerMockReturnOnlySpaces() {
        String expectedReturnedWord = "          ";
        when(scannerMock.nextLine()).thenReturn(expectedReturnedWord);

        String actualReturnedWord = view.getSentenceFromUser("Message");

        verify(scannerMock, times(1)).nextLine();
        assertEquals(expectedReturnedWord, actualReturnedWord);
    }

    @Test
    void getSentenceFromUser_shouldReturnedSimpleWord_whenScannerMockReturnSimpleWord() {
        String expectedReturnedWord = "Art";
        when(scannerMock.nextLine()).thenReturn(expectedReturnedWord);

        String actualReturnedWord = view.getSentenceFromUser("Message");

        assertEquals(expectedReturnedWord, actualReturnedWord);
    }

    @Test
    void getSentenceFromUser_shouldReturnedSentence_whenScannerMockReturnSentence() {
        String expectedReturnedWord = "A simple sentence that contains several words.";
        when(scannerMock.nextLine()).thenReturn(expectedReturnedWord);

        String actualReturnedWord = view.getSentenceFromUser("Message");

        assertEquals(expectedReturnedWord, actualReturnedWord);
    }

    @Test
    void getConfirmationAboutDeletingStudent_shouldNullPointerException_whenStudnetIsNull() {
        assertThrows(NullPointerException.class, () -> view.getConfirmationAboutDeletingStudent(null));
    }

    @Test
    void getConfirmationAboutDeletingStudent_shouldConfirmationMessageWithNullAndNull_whenStudentFieldsNotInitialized() {
        StudentDto student = new StudentDto();
        String expectedConfirmationMessage = "Are you sure you want to delete a student null null?\n"
                + "Please confirm your actions (enter Y or N): ";
        when(scannerMock.nextLine()).thenReturn("Y");

        view.getConfirmationAboutDeletingStudent(student);
        String actualConfirmationMessage = baos.toString();

        assertEquals(expectedConfirmationMessage, actualConfirmationMessage);
    }

    @Test
    void getConfirmationAboutDeletingStudent_shouldConfirmationMessageWithStudentFirstNameAndLastName_whenStudentHasInitializedFfirstAndLastNamesFields() {
        StudentDto student = new StudentDto("FirstName", "LastName");
        String expectedConfirmationMessage = "Are you sure you want to delete a student FirstName LastName?\n"
                + "Please confirm your actions (enter Y or N): ";
        when(scannerMock.nextLine()).thenReturn("Y");

        view.getConfirmationAboutDeletingStudent(student);
        String actualConfirmationMessage = baos.toString();

        assertEquals(expectedConfirmationMessage, actualConfirmationMessage);
    }

    @Test
    void getConfirmationAboutDeletingStudent_shouldReturnedOneCharacterConfirmation_whenScannerMockReturnOneCharacter() {
        StudentDto student = new StudentDto("FirstName", "LastName");
        String expectedConfirmationFromUser = "Y";
        when(scannerMock.nextLine()).thenReturn(expectedConfirmationFromUser);

        String actualConfirmationFromUser = view.getConfirmationAboutDeletingStudent(student);

        assertEquals(expectedConfirmationFromUser, actualConfirmationFromUser);
    }

    @Test
    void getConfirmationAboutDeletingStudent_shouldReturnedNullConfirmation_whenScannerMockReturnNull() {
        StudentDto student = new StudentDto("FirstName", "LastName");
        String expectedConfirmationFromUser = null;
        when(scannerMock.nextLine()).thenReturn(expectedConfirmationFromUser);

        String actualConfirmationFromUser = view.getConfirmationAboutDeletingStudent(student);

        assertEquals(expectedConfirmationFromUser, actualConfirmationFromUser);
    }

    @Test
    void getConfirmationAboutDeletingGroup_shouldNullPointerException_whenGroupIsNull() {
        assertThrows(NullPointerException.class, () -> view.getConfirmationAboutDeletingGroup(null));
    }

    @Test
    void getConfirmationAboutDeletingGroup_shouldConfirmationMessageWithNull_whenGroupNameNotInitialized() {
        GroupDto group = new GroupDto();
        String expectedConfirmationMessage = "Are you sure you want to delete a group null?\n"
                + "WARNING: Students who belong to this group will also be deleted.\n"
                + "Please confirm your actions (enter Y or N): ";
        when(scannerMock.nextLine()).thenReturn("Y");

        view.getConfirmationAboutDeletingGroup(group);
        String actualConfirmationMessage = baos.toString();

        assertEquals(expectedConfirmationMessage, actualConfirmationMessage);
    }

    @Test
    void getConfirmationAboutDeletingGroup_shouldConfirmationMessageWithCorrectGroupName_whenGroupNameCorrectlyInitialized() {
        GroupDto group = new GroupDto("KQ-98");
        String expectedConfirmationMessage = "Are you sure you want to delete a group KQ-98?\n"
                + "WARNING: Students who belong to this group will also be deleted.\n"
                + "Please confirm your actions (enter Y or N): ";
        when(scannerMock.nextLine()).thenReturn("Y");

        view.getConfirmationAboutDeletingGroup(group);
        String actualConfirmationMessage = baos.toString();

        assertEquals(expectedConfirmationMessage, actualConfirmationMessage);
    }

    @Test
    void getConfirmationAboutDeletingGroup_shouldReturnedOneCharacterConfirmation_whenScannerMockReturnOneCharacter() {
        GroupDto group = new GroupDto("KQ-98");
        String expectedConfirmationFromUser = "Y";
        when(scannerMock.nextLine()).thenReturn(expectedConfirmationFromUser);

        String actualConfirmationFromUser = view.getConfirmationAboutDeletingGroup(group);

        assertEquals(expectedConfirmationFromUser, actualConfirmationFromUser);
    }

    @Test
    void getConfirmationAboutDeletingGroup_shouldReturnedEmptyConfirmation_whenScannerMockReturnEmptyString() {
        GroupDto group = new GroupDto("KQ-98");
        String expectedConfirmationFromUser = "";
        when(scannerMock.nextLine()).thenReturn(expectedConfirmationFromUser);

        String actualConfirmationFromUser = view.getConfirmationAboutDeletingGroup(group);

        assertEquals(expectedConfirmationFromUser, actualConfirmationFromUser);
    }

    @Test
    void getConfirmationAboutDeletingGroup_shouldReturnedNullConfirmation_whenScannerMockReturnNull() {
        GroupDto group = new GroupDto("KQ-98");
        String expectedConfirmationFromUser = null;
        when(scannerMock.nextLine()).thenReturn(expectedConfirmationFromUser);

        String actualConfirmationFromUser = view.getConfirmationAboutDeletingGroup(group);

        assertEquals(expectedConfirmationFromUser, actualConfirmationFromUser);
    }

    @Test
    void getConfirmationAboutDeletingCourse_shouldNullPointerException_whenCourseIsNull() {
        assertThrows(NullPointerException.class, () -> view.getConfirmationAboutDeletingCourse(null));
    }

    @Test
    void getConfirmationAboutDeletingCourse_shouldConfirmationMessageWithNull_whenCourseNameNotInitialized() {
        CourseDto course = new CourseDto();
        String expectedConfirmationMessage = "Are you sure you want to delete a course null?\n"
                + "Please confirm your actions (enter Y or N): ";
        when(scannerMock.nextLine()).thenReturn("Y");

        view.getConfirmationAboutDeletingCourse(course);
        String actualConfirmationMessage = baos.toString();

        assertEquals(expectedConfirmationMessage, actualConfirmationMessage);
    }

    @Test
    void getConfirmationAboutDeletingCourse_shouldConfirmationMessageWithCorrectCourseName_whenCourseNameCorrectlyInitialized() {
        CourseDto course = new CourseDto("CourseName", "Description");
        String expectedConfirmationMessage = "Are you sure you want to delete a course CourseName?\n"
                + "Please confirm your actions (enter Y or N): ";
        when(scannerMock.nextLine()).thenReturn("Y");

        view.getConfirmationAboutDeletingCourse(course);
        String actualConfirmationMessage = baos.toString();

        assertEquals(expectedConfirmationMessage, actualConfirmationMessage);
    }

    @Test
    void getConfirmationAboutDeletingCourse_shouldReturnedOneCharacterConfirmation_whenScannerMockReturnOneCharacter() {
        CourseDto course = new CourseDto("CourseName", "Description");
        String expectedConfirmationFromUser = "Y";
        when(scannerMock.nextLine()).thenReturn(expectedConfirmationFromUser);

        String actualConfirmationFromUser = view.getConfirmationAboutDeletingCourse(course);

        assertEquals(expectedConfirmationFromUser, actualConfirmationFromUser);
    }

    @Test
    void getConfirmationAboutDeletingCourse_shouldReturnedOneEmptyConfirmation_whenScannerMockReturnEmptyString() {
        CourseDto course = new CourseDto("CourseName", "Description");
        String expectedConfirmationFromUser = "";
        when(scannerMock.nextLine()).thenReturn(expectedConfirmationFromUser);

        String actualConfirmationFromUser = view.getConfirmationAboutDeletingCourse(course);

        assertEquals(expectedConfirmationFromUser, actualConfirmationFromUser);
    }

    @Test
    void getConfirmationAboutDeletingCourse_shouldReturnedNullConfirmation_whenScannerMockReturnNull() {
        CourseDto course = new CourseDto("CourseName", "Description");
        String expectedConfirmationFromUser = null;
        when(scannerMock.nextLine()).thenReturn(expectedConfirmationFromUser);

        String actualConfirmationFromUser = view.getConfirmationAboutDeletingCourse(course);

        assertEquals(expectedConfirmationFromUser, actualConfirmationFromUser);
    }

    @Test
    void displayGroups_shouldNullPointerException_whenGroupsListIsNull() {
        assertThrows(NullPointerException.class, () -> view.displayGroups(null));
    }

    @Test
    void displayGroups_shouldNullPointerException_whenGroupInListIsNull() {
        List<GroupDto> groupsList = new ArrayList<>();
        groupsList.add(null);

        assertThrows(NullPointerException.class, () -> view.displayGroups(groupsList));
    }

    @Test
    void displayGroups_shouldDisplayedGroupNameNull_whenGroupNameInGroupsListIsNull() {
        GroupDto group = new GroupDto(null);
        List<GroupDto> groupsList = new ArrayList<>();
        groupsList.add(group);
        String expectedDisplayedGroups = """

                                       +------+
                                       | null |
                                       +------+
                """;

        view.displayGroups(groupsList);
        String actualDisplayedGroups = baos.toString();

        assertEquals(expectedDisplayedGroups, actualDisplayedGroups);
    }

    @Test
    void displayGroups_shouldNothingDisplayed_whenGroupsListIsEmpty() {
        List<GroupDto> groupsList = new ArrayList<>();
        String expectedDisplayedGroups = """

                """;

        view.displayGroups(groupsList);
        String actualDisplayedGroups = baos.toString();

        assertEquals(expectedDisplayedGroups, actualDisplayedGroups);
    }

    @Test
    void displayGroups_shouldDisplayedGroupNamesTableWithoutNames_whenGroupNamesInGroupsListAreEmpty() {
        GroupDto firstGroup = new GroupDto("");
        GroupDto secondGroup = new GroupDto("");
        List<GroupDto> groupsList = new ArrayList<>();
        groupsList.add(firstGroup);
        groupsList.add(secondGroup);
        String expectedDisplayedGroups = """

                                       +--+
                                       |  |
                                       +--+
                                       |  |
                                       +--+
                """;

        view.displayGroups(groupsList);
        String actualDisplayedGroups = baos.toString();

        assertEquals(expectedDisplayedGroups, actualDisplayedGroups);
    }

    @Test
    void displayGroups_shouldDisplayedStraightGroupNamesTable_whenGroupNamesHaveSameLengthInGroupsList() {
        GroupDto firstGroup = new GroupDto("HS-22");
        GroupDto secondGroup = new GroupDto("BV-63");
        GroupDto thirdGroup = new GroupDto("DT-10");
        List<GroupDto> groupsList = new ArrayList<>();
        groupsList.add(firstGroup);
        groupsList.add(secondGroup);
        groupsList.add(thirdGroup);
        String expectedDisplayedGroups = """

                                       +-------+
                                       | HS-22 |
                                       +-------+
                                       | BV-63 |
                                       +-------+
                                       | DT-10 |
                                       +-------+
                """;

        view.displayGroups(groupsList);
        String actualDisplayedGroups = baos.toString();

        assertEquals(expectedDisplayedGroups, actualDisplayedGroups);
    }

    @Test
    void displayGroups_shouldDisplayedDistortedGroupNamesTable_whenGroupNamesHaveDifferentLengthInGroupsList() {
        GroupDto firstGroup = new GroupDto("H-22");
        GroupDto secondGroup = new GroupDto("BV-633062");
        GroupDto thirdGroup = new GroupDto("DT--10");
        List<GroupDto> groupsList = new ArrayList<>();
        groupsList.add(firstGroup);
        groupsList.add(secondGroup);
        groupsList.add(thirdGroup);
        String expectedDisplayedGroups = """

                                       +--------+
                                       | H-22 |
                                       +------+
                                       | BV-633062 |
                                       +-----------+
                                       | DT--10 |
                                       +--------+
                """;

        view.displayGroups(groupsList);
        String actualDisplayedGroups = baos.toString();

        assertEquals(expectedDisplayedGroups, actualDisplayedGroups);
    }

    @Test
    void displayGroupsWithTheirNumberOfStudents_shouldNullPointerException_whenMapWithGroupsWithTheirNumberOfStudentsIsNull() {
        assertThrows(NullPointerException.class, () -> view.displayGroupsWithTheirNumberOfStudents(null));
    }

    @Test
    void displayGroupsWithTheirNumberOfStudents_shouldNullPointerException_whenGroupInMapIsNull() {
        Map<GroupDto, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
        groupsWithTheirNumberOfStudents.put(null, 20);

        assertThrows(NullPointerException.class,
                () -> view.displayGroupsWithTheirNumberOfStudents(groupsWithTheirNumberOfStudents));
    }

    @Test
    void displayGroupsWithTheirNumberOfStudents_shouldNullPointerException_whenNumberOfStudentsInMapIsNull() {
        Map<GroupDto, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
        groupsWithTheirNumberOfStudents.put(new GroupDto("LS-43"), null);

        assertThrows(NullPointerException.class,
                () -> view.displayGroupsWithTheirNumberOfStudents(groupsWithTheirNumberOfStudents));
    }

    @Test
    void displayGroupsWithTheirNumberOfStudents_shouldNullPointerException_whenGroupNameInMapIsNull() {
        GroupDto group = new GroupDto(null);
        Map<GroupDto, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
        groupsWithTheirNumberOfStudents.put(group, 20);

        assertThrows(NullPointerException.class,
                () -> view.displayGroupsWithTheirNumberOfStudents(groupsWithTheirNumberOfStudents));
    }

    @Test
    void displayGroupsWithTheirNumberOfStudents_shouldNullPointerException_whenNumberOfStudentsIsNull() {
        GroupDto group = new GroupDto("FS-25");
        Map<GroupDto, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
        groupsWithTheirNumberOfStudents.put(group, null);

        assertThrows(NullPointerException.class,
                () -> view.displayGroupsWithTheirNumberOfStudents(groupsWithTheirNumberOfStudents));
    }

    @Test
    void displayGroupsWithTheirNumberOfStudents_shouldDisplayedOnlySentence_whenGroupsWithTheirNumberOfStudentsMapEmpty() {
        Map<GroupDto, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
        String expectedDisplayedGroups = "Groups with their number of students:\n";

        view.displayGroupsWithTheirNumberOfStudents(groupsWithTheirNumberOfStudents);
        String actualDisplayedGroups = baos.toString();

        assertEquals(expectedDisplayedGroups, actualDisplayedGroups);
    }

    @Test
    void displayGroupsWithTheirNumberOfStudents_shouldDisplayedDistortedTableGroupsWithTheirNumberOfStudents_whenGroupsInMapHaveNamesWithDifferentLengths() {
        Map<GroupDto, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
        GroupDto firstGroup = new GroupDto("JRJF-84");
        GroupDto secondGroup = new GroupDto("QFL-03");
        GroupDto thirdGroup = new GroupDto("VA-72");
        groupsWithTheirNumberOfStudents.put(firstGroup, 16);
        groupsWithTheirNumberOfStudents.put(secondGroup, 14);
        groupsWithTheirNumberOfStudents.put(thirdGroup, 10);
        String expectedDisplayedGroups = """
                Groups with their number of students:
                                       +-------+----+
                                       | JRJF-84 | 16 |
                                       +---------+----+
                                       | QFL-03 | 14 |
                                       +--------+----+
                                       | VA-72 | 10 |
                                       +-------+----+
                """;

        view.displayGroupsWithTheirNumberOfStudents(groupsWithTheirNumberOfStudents);
        String actualDisplayedGroups = baos.toString();

        assertEquals(expectedDisplayedGroups, actualDisplayedGroups);
    }

    @Test
    void displayGroupsWithTheirNumberOfStudents_shouldDisplayedGroupsWithTheirNumberOfStudents_whenNumberOfStudentsInMapHaveDifferentLengths() {
        Map<GroupDto, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
        GroupDto firstGroup = new GroupDto("JR-84");
        GroupDto secondGroup = new GroupDto("QF-03");
        GroupDto thirdGroup = new GroupDto("VA-72");
        groupsWithTheirNumberOfStudents.put(firstGroup, 6343);
        groupsWithTheirNumberOfStudents.put(secondGroup, 23);
        groupsWithTheirNumberOfStudents.put(thirdGroup, 1);
        String expectedDisplayedGroups = """
                Groups with their number of students:
                                       +-------+------+
                                       | QF-03 |   23 |
                                       +-------+------+
                                       | VA-72 |    1 |
                                       +-------+------+
                                       | JR-84 | 6343 |
                                       +-------+------+
                """;

        view.displayGroupsWithTheirNumberOfStudents(groupsWithTheirNumberOfStudents);
        String actualDisplayedGroups = baos.toString();

        assertEquals(expectedDisplayedGroups, actualDisplayedGroups);
    }

    @Test
    void displayGroupsWithTheirNumberOfStudents_shouldDisplayedGroupsWithTheirNumberOfStudents_whenMapContainsThreeCorrectGroupsWithTheirNumberOfStudents() {
        Map<GroupDto, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
        GroupDto firstGroup = new GroupDto("JR-84");
        GroupDto secondGroup = new GroupDto("QL-03");
        GroupDto thirdGroup = new GroupDto("VA-72");
        groupsWithTheirNumberOfStudents.put(firstGroup, 16);
        groupsWithTheirNumberOfStudents.put(secondGroup, 14);
        groupsWithTheirNumberOfStudents.put(thirdGroup, 10);
        String expectedDisplayedGroups = """
                Groups with their number of students:
                                       +-------+----+
                                       | QL-03 | 14 |
                                       +-------+----+
                                       | VA-72 | 10 |
                                       +-------+----+
                                       | JR-84 | 16 |
                                       +-------+----+
                """;

        view.displayGroupsWithTheirNumberOfStudents(groupsWithTheirNumberOfStudents);
        String actualDisplayedGroups = baos.toString();

        assertEquals(expectedDisplayedGroups, actualDisplayedGroups);
    }

    @Test
    void displayStudentsWithTheirGroups_shouldNullPointerException_whenStudentsWithTheirGroupsMapIsNull() {
        assertThrows(NullPointerException.class, () -> view.displayStudentsWithTheirGroups(null));
    }

    @Test
    void displayStudentsWithTheirGroups_shouldNullPointerException_whenStudentIsNullInMap() {
        StudentDto student = null;
        GroupDto group = new GroupDto("KD-54");
        Map<StudentDto, GroupDto> studentsWithTheirGroups = new HashMap<>();
        studentsWithTheirGroups.put(student, group);

        assertThrows(NullPointerException.class, () -> view.displayStudentsWithTheirGroups(studentsWithTheirGroups));
    }

    @Test
    void displayStudentsWithTheirGroups_shouldNullPointerException_whenGrouIsNullInMap() {
        StudentDto student = new StudentDto("FirstName", "LastName");
        GroupDto group = null;
        Map<StudentDto, GroupDto> studentsWithTheirGroups = new HashMap<>();
        studentsWithTheirGroups.put(student, group);

        assertThrows(NullPointerException.class, () -> view.displayStudentsWithTheirGroups(studentsWithTheirGroups));
    }

    @Test
    void displayStudentsWithTheirGroups_shouldDisplayedTwoNullsInsteadOfFullStudentNameInTable_whenStudentFirstNameAndLastNameAreNulls() {
        StudentDto student = new StudentDto(null, null);
        GroupDto group = new GroupDto("KD-54");
        Map<StudentDto, GroupDto> studentsWithTheirGroups = new HashMap<>();
        studentsWithTheirGroups.put(student, group);
        String expectedDisplayedStudentsWithGroups = """

                                  +-----------+-------+
                                  | null null | KD-54 |
                                  +-----------+-------+
                """;

        view.displayStudentsWithTheirGroups(studentsWithTheirGroups);
        String actualDisplayedStudentsWithGroups = baos.toString();

        assertEquals(expectedDisplayedStudentsWithGroups, actualDisplayedStudentsWithGroups);
    }

    @Test
    void displayStudentsWithTheirGroups_shouldDisplayedNullInsteadOfGroupNameInTable_whenGroupNameIsNull() {
        StudentDto student = new StudentDto("FirstName", "LastName");
        GroupDto group = new GroupDto(null);
        Map<StudentDto, GroupDto> studentsWithTheirGroups = new HashMap<>();
        studentsWithTheirGroups.put(student, group);
        String expectedDisplayedStudentsWithGroups = """

                                  +--------------------+------+
                                  | FirstName LastName | null |
                                  +--------------------+------+
                """;

        view.displayStudentsWithTheirGroups(studentsWithTheirGroups);
        String actualDisplayedStudentsWithGroups = baos.toString();

        assertEquals(expectedDisplayedStudentsWithGroups, actualDisplayedStudentsWithGroups);
    }

    @Test
    void displayStudentsWithTheirGroups_shouldNothingDisplayed_whenStudentsWithTheirGroupsMapIsEmpty() {
        Map<StudentDto, GroupDto> studentsWithTheirGroups = new HashMap<>();
        String expectedDisplayedStudentsWithGroups = """

                """;

        view.displayStudentsWithTheirGroups(studentsWithTheirGroups);
        String actualDisplayedStudentsWithGroups = baos.toString();

        assertEquals(expectedDisplayedStudentsWithGroups, actualDisplayedStudentsWithGroups);
    }

    @Test
    void displayStudentsWithTheirGroups_shouldDisplayedEmptyTable_whenStudentFirstNameAndLastNameAreEmptyAndGroupNameAlsoEmpty() {
        StudentDto student = new StudentDto("", "");
        GroupDto group = new GroupDto("");
        Map<StudentDto, GroupDto> studentsWithTheirGroups = new HashMap<>();
        studentsWithTheirGroups.put(student, group);
        String expectedDisplayedStudentsWithGroups = """

                                  +---+--+
                                  |   |  |
                                  +---+--+
                """;

        view.displayStudentsWithTheirGroups(studentsWithTheirGroups);
        String actualDisplayedStudentsWithGroups = baos.toString();

        assertEquals(expectedDisplayedStudentsWithGroups, actualDisplayedStudentsWithGroups);
    }

    @Test
    void displayStudentsWithTheirGroups_shouldDisplayedStraightTable_whenStudentFullNamesHaveDifferentLengthsAndGroupNamesHaveSameLengths() {
        StudentDto firstStudent = new StudentDto("FirstName", "LastName");
        StudentDto secondStudent = new StudentDto("", "");
        StudentDto thirdStudent = new StudentDto("               ", "            ");
        GroupDto firstGroup = new GroupDto("MN-90");
        GroupDto secondGroup = new GroupDto("SC-87");
        GroupDto thirdGroup = new GroupDto("KJ-56");
        Map<StudentDto, GroupDto> studentsWithTheirGroups = new HashMap<>();
        studentsWithTheirGroups.put(firstStudent, firstGroup);
        studentsWithTheirGroups.put(secondStudent, secondGroup);
        studentsWithTheirGroups.put(thirdStudent, thirdGroup);
        String expectedDisplayedStudentsWithGroups = """

                                  +------------------------------+-------+
                                  | FirstName LastName           | MN-90 |
                                  +------------------------------+-------+
                                  |                              | KJ-56 |
                                  +------------------------------+-------+
                                  |                              | SC-87 |
                                  +------------------------------+-------+
                """;

        view.displayStudentsWithTheirGroups(studentsWithTheirGroups);
        String actualDisplayedStudentsWithGroups = baos.toString();

        assertEquals(expectedDisplayedStudentsWithGroups, actualDisplayedStudentsWithGroups);
    }

    @Test
    void displayStudentsWithTheirGroups_shouldDisplayedDistortedTable_whenStudentFullNamesHaveDifferentLengthsAndGroupNamesAlsoHaveDifferentLengths() {
        StudentDto firstStudent = new StudentDto("FirstName_1", "LastName_1");
        StudentDto secondStudent = new StudentDto("FirstName____2", "LastName___2");
        StudentDto thirdStudent = new StudentDto("FirstName__3", "LastName__3");
        GroupDto firstGroup = new GroupDto("MNJGFV-90453");
        GroupDto secondGroup = new GroupDto("");
        GroupDto thirdGroup = new GroupDto("     ");
        Map<StudentDto, GroupDto> studentsWithTheirGroups = new HashMap<>();
        studentsWithTheirGroups.put(firstStudent, firstGroup);
        studentsWithTheirGroups.put(secondStudent, secondGroup);
        studentsWithTheirGroups.put(thirdStudent, thirdGroup);
        String expectedDisplayedStudentsWithGroups = """

                                  +-----------------------------+--------------+
                                  | FirstName____2 LastName___2 |  |
                                  +-----------------------------+--+
                                  | FirstName__3 LastName__3    |       |
                                  +-----------------------------+-------+
                                  | FirstName_1 LastName_1      | MNJGFV-90453 |
                                  +-----------------------------+--------------+
                """;

        view.displayStudentsWithTheirGroups(studentsWithTheirGroups);
        String actualDisplayedStudentsWithGroups = baos.toString();

        assertEquals(expectedDisplayedStudentsWithGroups, actualDisplayedStudentsWithGroups);
    }

    @Test
    void displayStudentsWithTheirCourses_shouldNullPointerException_whenMapWithStudentsAndTheirCoursesIsNull() {
        assertThrows(NullPointerException.class, () -> view.displayStudentsWithTheirCourses(null));
    }

    @Test
    void displayStudentsWithTheirCourses_shouldNullPointerException_whenStudentInMapIsNull() {
        CourseDto firstCourse = new CourseDto("CourseName_1", "Description_1");
        CourseDto secondCourse = new CourseDto("CourseName_2", "Description_2");
        Set<CourseDto> coursesForStudent = new HashSet<>();
        coursesForStudent.add(firstCourse);
        coursesForStudent.add(secondCourse);
        Map<StudentDto, Set<CourseDto>> studentsWithTheirCourses = new HashMap<>();
        studentsWithTheirCourses.put(null, coursesForStudent);

        assertThrows(NullPointerException.class, () -> view.displayStudentsWithTheirCourses(studentsWithTheirCourses));
    }

    @Test
    void displayStudentsWithTheirCourses_shouldDisplayedStudentWithNullFirstName_whenStudentFirstNameInMapIsNull() {
        StudentDto student = new StudentDto(null, "LastName");
        CourseDto firstCourse = new CourseDto("CourseName_1", "Description_1");
        CourseDto secondCourse = new CourseDto("CourseName_2", "Description_2");
        Set<CourseDto> coursesForStudent = new HashSet<>();
        coursesForStudent.add(firstCourse);
        coursesForStudent.add(secondCourse);
        Map<StudentDto, Set<CourseDto>> studentsWithTheirCourses = new HashMap<>();
        studentsWithTheirCourses.put(student, coursesForStudent);
        String expectedDisplayedStudentsWithCourses = """
                Students with their courses:
                +---------------+----------------------------+
                | null LastName | CourseName_1, CourseName_2 |
                +---------------+----------------------------+
                """;

        view.displayStudentsWithTheirCourses(studentsWithTheirCourses);
        String actualDisplayedStudentsWithCourses = baos.toString();

        assertEquals(expectedDisplayedStudentsWithCourses, actualDisplayedStudentsWithCourses);
    }

    @Test
    void displayStudentsWithTheirCourses_shouldNullPointerException_whenCoursesListForStudentInMapIsNull() {
        StudentDto student = new StudentDto("FirstName", "LastName");
        Map<StudentDto, Set<CourseDto>> studentsWithTheirCourses = new HashMap<>();
        studentsWithTheirCourses.put(student, null);

        assertThrows(NullPointerException.class, () -> view.displayStudentsWithTheirCourses(studentsWithTheirCourses));
    }

    @Test
    void displayStudentsWithTheirCourses_shouldNullPointerException_whenCourseInCoursesListForStudentIsNull() {
        StudentDto student = new StudentDto("FirstName", "LastName");
        CourseDto firstCourse = new CourseDto("CourseName_1", "Description_1");
        CourseDto secondCourse = new CourseDto("CourseName_2", "Description_2");
        Set<CourseDto> coursesForStudent = new HashSet<>();
        coursesForStudent.add(firstCourse);
        coursesForStudent.add(secondCourse);
        coursesForStudent.add(null);
        Map<StudentDto, Set<CourseDto>> studentsWithTheirCourses = new HashMap<>();
        studentsWithTheirCourses.put(student, coursesForStudent);

        assertThrows(NullPointerException.class, () -> view.displayStudentsWithTheirCourses(studentsWithTheirCourses));
    }

    @Test
    void displayStudentsWithTheirCourses_shouldDisplayedNullCourseName_whenCourseNameInCoursesListForStudentIsNull() {
        StudentDto student = new StudentDto("FirstName", "LastName");
        CourseDto firstCourse = new CourseDto("CourseName_1", "Description_1");
        CourseDto secondCourse = new CourseDto(null, "Description_2");
        Set<CourseDto> coursesForStudent = new HashSet<>();
        coursesForStudent.add(firstCourse);
        coursesForStudent.add(secondCourse);
        Map<StudentDto, Set<CourseDto>> studentsWithTheirCourses = new HashMap<>();
        studentsWithTheirCourses.put(student, coursesForStudent);
        String expectedDisplayedStudentsWithCourses = """
                Students with their courses:
                +--------------------+--------------------+
                | FirstName LastName | null, CourseName_1 |
                +--------------------+--------------------+
                """;

        view.displayStudentsWithTheirCourses(studentsWithTheirCourses);
        String actualDisplayedStudentsWithCourses = baos.toString();

        assertEquals(expectedDisplayedStudentsWithCourses, actualDisplayedStudentsWithCourses);
    }

    @Test
    void displayStudentsWithTheirCourses_shouldDisplayedOnlySentence_whenStudentsWithTheirCoursesMapEmpty() {
        Map<StudentDto, Set<CourseDto>> studentsWithTheirCourses = new HashMap<>();
        String expectedDisplayedStudentsWithCourses = """
                Students with their courses:
                """;

        view.displayStudentsWithTheirCourses(studentsWithTheirCourses);
        String actualDisplayedStudentsWithCourses = baos.toString();

        assertEquals(expectedDisplayedStudentsWithCourses, actualDisplayedStudentsWithCourses);
    }

    @Test
    void displayStudentsWithTheirCourses_shouldDisplayedCorrectTableWithStudentsAndTheirCourses_whenNamesOfSomeStudentsAreEmpty() {
        StudentDto firstStudent = new StudentDto("", "");
        StudentDto secondStudent = new StudentDto("", "LastName_2");
        StudentDto thirdStudent = new StudentDto("FirstName_3", "");
        CourseDto firstCourse = new CourseDto("CourseName_1", "Description_1");
        CourseDto secondCourse = new CourseDto("CourseName_2", "Description_2");
        CourseDto thirdCourse = new CourseDto("CourseName_3", "Description_3");
        CourseDto fourthCourse = new CourseDto("CourseName_4", "Description_4");
        Set<CourseDto> coursesForFirstStudent = new HashSet<>();
        Set<CourseDto> coursesForSecondStudent = new HashSet<>();
        Set<CourseDto> coursesForThirdStudent = new HashSet<>();
        coursesForFirstStudent.add(firstCourse);
        coursesForFirstStudent.add(secondCourse);
        coursesForSecondStudent.add(firstCourse);
        coursesForSecondStudent.add(thirdCourse);
        coursesForSecondStudent.add(fourthCourse);
        coursesForThirdStudent.add(firstCourse);
        coursesForThirdStudent.add(secondCourse);
        coursesForThirdStudent.add(thirdCourse);
        coursesForThirdStudent.add(fourthCourse);
        Map<StudentDto, Set<CourseDto>> studentsWithTheirCourses = new HashMap<>();
        studentsWithTheirCourses.put(firstStudent, coursesForFirstStudent);
        studentsWithTheirCourses.put(secondStudent, coursesForSecondStudent);
        studentsWithTheirCourses.put(thirdStudent, coursesForThirdStudent);
        String expectedDisplayedStudentsWithCourses = """
                Students with their courses:
                +--------------+--------------------------------------------------------+
                | FirstName_3  | CourseName_1, CourseName_2, CourseName_3, CourseName_4 |
                +--------------+--------------------------------------------------------+
                |  LastName_2  | CourseName_1, CourseName_3, CourseName_4               |
                +--------------+--------------------------------------------------------+
                |              | CourseName_1, CourseName_2                             |
                +--------------+--------------------------------------------------------+
                """;

        view.displayStudentsWithTheirCourses(studentsWithTheirCourses);
        String actualDisplayedStudentsWithCourses = baos.toString();

        assertEquals(expectedDisplayedStudentsWithCourses, actualDisplayedStudentsWithCourses);
    }

    @Test
    void displayStudentsWithTheirCourses_shouldDisplayedCorrectTableWithStudentsAndTheirCourses_whenNamesOfSomeCoursesAreEmpty() {
        StudentDto firstStudent = new StudentDto("FirstName_1", "LastName_1");
        StudentDto secondStudent = new StudentDto("FirstName_2", "LastName_2");
        StudentDto thirdStudent = new StudentDto("FirstName_3", "LastName_3");
        CourseDto firstCourse = new CourseDto("", "Description_1");
        CourseDto secondCourse = new CourseDto("", "Description_2");
        CourseDto thirdCourse = new CourseDto("CourseName_3", "Description_3");
        CourseDto fourthCourse = new CourseDto("CourseName_4", "Description_4");
        Set<CourseDto> coursesForFirstStudent = new HashSet<>();
        Set<CourseDto> coursesForSecondStudent = new HashSet<>();
        Set<CourseDto> coursesForThirdStudent = new HashSet<>();
        coursesForFirstStudent.add(firstCourse);
        coursesForFirstStudent.add(secondCourse);
        coursesForSecondStudent.add(firstCourse);
        coursesForSecondStudent.add(thirdCourse);
        coursesForSecondStudent.add(fourthCourse);
        coursesForThirdStudent.add(firstCourse);
        coursesForThirdStudent.add(secondCourse);
        coursesForThirdStudent.add(thirdCourse);
        coursesForThirdStudent.add(fourthCourse);
        Map<StudentDto, Set<CourseDto>> studentsWithTheirCourses = new HashMap<>();
        studentsWithTheirCourses.put(firstStudent, coursesForFirstStudent);
        studentsWithTheirCourses.put(secondStudent, coursesForSecondStudent);
        studentsWithTheirCourses.put(thirdStudent, coursesForThirdStudent);
        String expectedDisplayedStudentsWithCourses = """
                Students with their courses:
                +------------------------+--------------------------------+
                | FirstName_2 LastName_2 | , CourseName_3, CourseName_4   |
                +------------------------+--------------------------------+
                | FirstName_3 LastName_3 | , , CourseName_3, CourseName_4 |
                +------------------------+--------------------------------+
                | FirstName_1 LastName_1 | ,                              |
                +------------------------+--------------------------------+
                """;

        view.displayStudentsWithTheirCourses(studentsWithTheirCourses);
        String actualDisplayedStudentsWithCourses = baos.toString();

        assertEquals(expectedDisplayedStudentsWithCourses, actualDisplayedStudentsWithCourses);
    }

    @Test
    void displayStudentsWithTheirCourses_shouldDisplayedCorrectTableWithStudentsAndTheirCourses_whenMapContainStudentsWithOtherStudentsAndOtherCourses() {
        StudentDto firstStudent = new StudentDto("FirstName__1", "LastName___1");
        StudentDto secondStudent = new StudentDto("First_2", "LastName_2");
        StudentDto thirdStudent = new StudentDto("F", "LastName____3");
        CourseDto firstCourse = new CourseDto("CourseName__1", "Description_1");
        CourseDto secondCourse = new CourseDto("CourseName____2", "Description_2");
        CourseDto thirdCourse = new CourseDto("CourseName___3", "Description_3");
        CourseDto fourthCourse = new CourseDto("C", "Description_4");
        Set<CourseDto> coursesForFirstStudent = new HashSet<>();
        Set<CourseDto> coursesForSecondStudent = new HashSet<>();
        Set<CourseDto> coursesForThirdStudent = new HashSet<>();
        coursesForFirstStudent.add(firstCourse);
        coursesForFirstStudent.add(secondCourse);
        coursesForSecondStudent.add(firstCourse);
        coursesForSecondStudent.add(thirdCourse);
        coursesForSecondStudent.add(fourthCourse);
        coursesForThirdStudent.add(firstCourse);
        coursesForThirdStudent.add(secondCourse);
        coursesForThirdStudent.add(thirdCourse);
        coursesForThirdStudent.add(fourthCourse);
        Map<StudentDto, Set<CourseDto>> studentsWithTheirCourses = new HashMap<>();
        studentsWithTheirCourses.put(firstStudent, coursesForFirstStudent);
        studentsWithTheirCourses.put(secondStudent, coursesForSecondStudent);
        studentsWithTheirCourses.put(thirdStudent, coursesForThirdStudent);
        String expectedDisplayedStudentsWithCourses = """
                Students with their courses:
                +---------------------------+---------------------------------------------------+
                | FirstName__1 LastName___1 | CourseName__1, CourseName____2                    |
                +---------------------------+---------------------------------------------------+
                | First_2 LastName_2        | C, CourseName__1, CourseName___3                  |
                +---------------------------+---------------------------------------------------+
                | F LastName____3           | C, CourseName__1, CourseName___3, CourseName____2 |
                +---------------------------+---------------------------------------------------+
                """;

        view.displayStudentsWithTheirCourses(studentsWithTheirCourses);
        String actualDisplayedStudentsWithCourses = baos.toString();

        assertEquals(expectedDisplayedStudentsWithCourses, actualDisplayedStudentsWithCourses);
    }

    @Test
    void displayCourses_shouldNullPointerException_whenCoursesListIsNull() {
        assertThrows(NullPointerException.class, () -> view.displayCourses(null));
    }

    @Test
    void displayCourses_shouldNullPointerException_whenCourseInCoursesListIsNull() {
        List<CourseDto> coursesList = new ArrayList<>();
        coursesList.add(null);

        assertThrows(NullPointerException.class, () -> view.displayCourses(coursesList));
    }

    @Test
    void displayCourses_shouldNullPointerException_whenCourseNameInCoursesListIsNull() {
        CourseDto course = new CourseDto(null, "Description");
        List<CourseDto> coursesList = new ArrayList<>();
        coursesList.add(course);

        assertThrows(NullPointerException.class, () -> view.displayCourses(coursesList));
    }

    @Test
    void displayCourses_shouldNullPointerException_whenCourseDescriptionInCoursesListIsNull() {
        CourseDto course = new CourseDto("CourseName", null);
        List<CourseDto> coursesList = new ArrayList<>();
        coursesList.add(course);

        assertThrows(NullPointerException.class, () -> view.displayCourses(coursesList));
    }

    @Test
    void displayCourses_shouldDisplayedOnlySentences_whenCoursesListEmpty() {
        List<CourseDto> coursesList = new ArrayList<>();
        String expectedDisplayedCourses = "\n";

        view.displayCourses(coursesList);
        String actualDisplayedCourses = baos.toString();

        assertEquals(expectedDisplayedCourses, actualDisplayedCourses);
    }

    @Test
    void displayCourses_shouldDisplayedTableWithCoursesNamesAndDescriptions_whenSomeCoursesNamesAndDescriptionsEmpty() {
        CourseDto firstCourse = new CourseDto("", "Description_1");
        CourseDto secondCourse = new CourseDto("CourseName_2", "");
        CourseDto thirdCourse = new CourseDto("", "");
        List<CourseDto> coursesList = new ArrayList<>();
        coursesList.add(firstCourse);
        coursesList.add(secondCourse);
        coursesList.add(thirdCourse);
        String expectedDisplayedCourses = """

                +--------------+---------------+
                |              | Description_1 |
                +--------------+---------------+
                | CourseName_2 |               |
                +--------------+---------------+
                |              |               |
                +--------------+---------------+\n""";

        view.displayCourses(coursesList);
        String actualDisplayedCourses = baos.toString();

        assertEquals(expectedDisplayedCourses, actualDisplayedCourses);
    }

    @Test
    void displayCourses_shouldDisplayedTableWithCoursesNamesAndDescriptions_whenСoursesWithDifferentNamesAndDescriptionsPresentInCourseList() {
        CourseDto firstCourse = new CourseDto("CourseName______1", "Description_____1");
        CourseDto secondCourse = new CourseDto("CourseName_2", "Description__2");
        CourseDto thirdCourse = new CourseDto("Co", "De");
        List<CourseDto> coursesList = new ArrayList<>();
        coursesList.add(firstCourse);
        coursesList.add(secondCourse);
        coursesList.add(thirdCourse);
        String expectedDisplayedCourses = """

                +-------------------+-------------------+
                | CourseName______1 | Description_____1 |
                +-------------------+-------------------+
                | CourseName_2      | Description__2    |
                +-------------------+-------------------+
                | Co                | De                |
                +-------------------+-------------------+\n""";

        view.displayCourses(coursesList);
        String actualDisplayedCourses = baos.toString();

        assertEquals(expectedDisplayedCourses, actualDisplayedCourses);
    }

    @Test
    void makeCharacterSequence_shouldInvocationTargetException_whenCharactersCountIsNull() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("makeCharacterSequence", Integer.class, Character.class);
        method.setAccessible(true);

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, null, '-'));
    }

    @Test
    void makeCharacterSequence_shouldStringThatContainsTenNullWords_whenCharacterIsNull() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("makeCharacterSequence", Integer.class, Character.class);
        method.setAccessible(true);
        String expectedCharacterSequence = "nullnullnullnullnullnullnullnullnullnull";

        String actualCharacterSequence = method.invoke(view, 10, null).toString();

        assertEquals(expectedCharacterSequence, actualCharacterSequence);
    }

    @Test
    void makeCharacterSequence_shouldEmptyString_whenCharactersCountLessThanZero() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("makeCharacterSequence", Integer.class, Character.class);
        method.setAccessible(true);
        String expectedCharacterSequence = "";

        String actualCharacterSequence = method.invoke(view, -2, '-').toString();

        assertEquals(expectedCharacterSequence, actualCharacterSequence);
    }

    @Test
    void makeCharacterSequence_shouldEmptyString_whenCharactersCountIsZero() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("makeCharacterSequence", Integer.class, Character.class);
        method.setAccessible(true);
        String expectedCharacterSequence = "";

        String actualCharacterSequence = method.invoke(view, 0, '-').toString();

        assertEquals(expectedCharacterSequence, actualCharacterSequence);
    }

    @Test
    void makeCharacterSequence_shouldStringThatContainsFiveSpaces_whenCharacterIsSpace() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("makeCharacterSequence", Integer.class, Character.class);
        method.setAccessible(true);
        String expectedCharacterSequence = "     ";

        String actualCharacterSequence = method.invoke(view, 5, ' ').toString();

        assertEquals(expectedCharacterSequence, actualCharacterSequence);
    }

    @Test
    void makeCharacterSequence_shouldStringThatContainsEightyCharacters_whenLargeCharactersCount() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("makeCharacterSequence", Integer.class, Character.class);
        method.setAccessible(true);
        String expectedCharacterSequence = "********************************************************************************";

        String actualCharacterSequence = method.invoke(view, 80, '*').toString();

        assertEquals(expectedCharacterSequence, actualCharacterSequence);
    }

    @Test
    void getStudentFullName_shouldInvocationTargetException_whenStudentIsNull() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getStudentFullName", StudentDto.class);
        method.setAccessible(true);
        Student student = null;

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, student));
    }

    @Test
    void getStudentFullName_shouldStudentFullNameWhereFirstNameIsNull_whenStudentFirstNameIsNull() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getStudentFullName", StudentDto.class);
        method.setAccessible(true);
        StudentDto student = new StudentDto(null, "LastName");
        String expectedStudentFullName = "null LastName";

        String actualStudentFullName = method.invoke(view, student).toString();

        assertEquals(expectedStudentFullName, actualStudentFullName);
    }

    @Test
    void getStudentFullName_shouldOneSpace_whenStudentFirstNameAndLastNameAreEmpty() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getStudentFullName", StudentDto.class);
        method.setAccessible(true);
        StudentDto student = new StudentDto("", "");
        String expectedStudentFullName = " ";

        String actualStudentFullName = method.invoke(view, student).toString();

        assertEquals(expectedStudentFullName, actualStudentFullName);
    }

    @Test
    void getStudentFullName_shouldThreeSpaces_whenStudentFirstNameAndLastNameAreOneSpace() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getStudentFullName", StudentDto.class);
        method.setAccessible(true);
        StudentDto student = new StudentDto(" ", " ");
        String expectedStudentFullName = "   ";

        String actualStudentFullName = method.invoke(view, student).toString();

        assertEquals(expectedStudentFullName, actualStudentFullName);
    }

    @Test
    void getStudentFullName_shouldCorrectFullName_whenStudentFirstNameAndLastNameAreCorrect() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getStudentFullName", StudentDto.class);
        method.setAccessible(true);
        StudentDto student = new StudentDto("Firstname", "LastName");
        String expectedStudentFullName = "Firstname LastName";

        String actualStudentFullName = method.invoke(view, student).toString();

        assertEquals(expectedStudentFullName, actualStudentFullName);
    }

    @Test
    void getCoursesEnumeration_shouldInvocationTargetException_whenCoursesSetIsNull() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getCoursesEnumeration", Set.class);
        method.setAccessible(true);
        Set<Course> coursesList = null;

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, coursesList));
    }

    @Test
    void getCoursesEnumeration_shouldCoursesEnumerationWhereSecondCourseNameIsNull_whenSecondCourseNmaeIsNull()
            throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getCoursesEnumeration", Set.class);
        method.setAccessible(true);
        CourseDto firstCourse = new CourseDto("CourseName_1", "Description_1");
        CourseDto secondCourse = new CourseDto(null, "Description_2");
        Set<CourseDto> courses = new HashSet<CourseDto>();
        courses.add(firstCourse);
        courses.add(secondCourse);
        String expectedCoursesEnumeration = "null, CourseName_1";

        String actualCoursesEnumeration = method.invoke(view, courses).toString();

        assertEquals(expectedCoursesEnumeration, actualCoursesEnumeration);
    }

    @Test
    void getCoursesEnumeration_shouldEmptyString_whenCourseSetEmpty() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getCoursesEnumeration", Set.class);
        method.setAccessible(true);
        Set<Course> courses = new HashSet<Course>();
        String expectedCoursesEnumeration = "";

        String actualCoursesEnumeration = method.invoke(view, courses).toString();

        assertEquals(expectedCoursesEnumeration, actualCoursesEnumeration);
    }

    @Test
    void getCoursesEnumeration_shouldCommaWithSpace_whenCoursesNamesAreEmpty() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getCoursesEnumeration", Set.class);
        method.setAccessible(true);
        CourseDto firstCourse = new CourseDto("", "Description_1");
        CourseDto secondCourse = new CourseDto("", "Description_2");
        Set<CourseDto> courses = new HashSet<>();
        courses.add(firstCourse);
        courses.add(secondCourse);
        String expectedCoursesEnumeration = ", ";

        String actualCoursesEnumeration = method.invoke(view, courses).toString();

        assertEquals(expectedCoursesEnumeration, actualCoursesEnumeration);
    }

    @Test
    void getCoursesEnumeration_shouldCommaWithSpaces_whenCoursesNamesAreSpaces() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getCoursesEnumeration", Set.class);
        method.setAccessible(true);
        CourseDto firstCourse = new CourseDto("  ", "Description_1");
        CourseDto secondCourse = new CourseDto("  ", "Description_2");
        Set<CourseDto> courses = new HashSet<>();
        courses.add(firstCourse);
        courses.add(secondCourse);
        String expectedCoursesEnumeration = "  ,   ";

        String actualCoursesEnumeration = method.invoke(view, courses).toString();

        assertEquals(expectedCoursesEnumeration, actualCoursesEnumeration);
    }

    @Test
    void getCoursesEnumeration_shouldCorrectCoursesEnumeration_whenCoursesNamesAreCorrect() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getCoursesEnumeration", Set.class);
        method.setAccessible(true);
        CourseDto firstCourse = new CourseDto("CourseName_1", "Description_1");
        CourseDto secondCourse = new CourseDto("CourseName_2", "Description_2");
        Set<CourseDto> courses = new HashSet<>();
        courses.add(firstCourse);
        courses.add(secondCourse);
        String expectedCoursesEnumeration = "CourseName_1, CourseName_2";

        String actualCoursesEnumeration = method.invoke(view, courses).toString();

        assertEquals(expectedCoursesEnumeration, actualCoursesEnumeration);
    }

    @Test
    void getStringWithMaxLength_shouldInvocationTargetException_whenStringsListEmpty() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getStringWithMaxLength", List.class);
        method.setAccessible(true);
        List<String> stringsList = null;

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, stringsList));
    }

    @Test
    void getStringWithMaxLength_shouldInvocationTargetException_whenStringsListContainNull() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getStringWithMaxLength", List.class);
        method.setAccessible(true);
        List<String> strings = new ArrayList<>();
        strings.add("  fff");
        strings.add(null);

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, strings));
    }

    @Test
    void getStringWithMaxLength_shouldEmptyString_whenStringsListEmpty() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getStringWithMaxLength", List.class);
        method.setAccessible(true);
        List<String> strings = new ArrayList<>();
        String expectedMaxString = "";

        String actualMaxString = method.invoke(view, strings).toString();

        assertEquals(expectedMaxString, actualMaxString);
    }

    @Test
    void getStringWithMaxLength_shouldEmptyString_whenStringsListContainOnlyEmptyString() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getStringWithMaxLength", List.class);
        method.setAccessible(true);
        List<String> strings = new ArrayList<>();
        strings.add("");
        String expectedMaxString = "";

        String actualMaxString = method.invoke(view, strings).toString();

        assertEquals(expectedMaxString, actualMaxString);
    }

    @Test
    void getStringWithMaxLength_shouldStringWithMaxLength_whenStringsListContainOnlyEmptyString() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getStringWithMaxLength", List.class);
        method.setAccessible(true);
        List<String> strings = new ArrayList<>();
        strings.add("");
        strings.add("          ");
        strings.add("687678hngdtf    ^%^#$@#");
        String expectedMaxString = "687678hngdtf    ^%^#$@#";

        String actualMaxString = method.invoke(view, strings).toString();

        assertEquals(expectedMaxString, actualMaxString);
    }

    @Test
    void getMaxCousrse_shouldInvocationTargetException_whenIdentifierIsNull() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getMaxCousrse", String.class, List.class);
        method.setAccessible(true);
        Course firstCourse = new Course("CourseName_1", "Description_1");
        Course secondCourse = new Course("CourseName_2", "Description_2");
        List<Course> courses = new ArrayList<>();
        courses.add(firstCourse);
        courses.add(secondCourse);

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, null, courses));
    }

    @Test
    void getMaxCousrse_shouldInvocationTargetException_whenIdentifierSomeWord() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getMaxCousrse", String.class, List.class);
        method.setAccessible(true);
        String identifier = "SomeWord";
        Course firstCourse = new Course("CourseName_1", "Description_1");
        Course secondCourse = new Course("CourseName_2", "Description_2");
        List<Course> courses = new ArrayList<>();
        courses.add(firstCourse);
        courses.add(secondCourse);

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, identifier, courses));
    }

    @Test
    void getMaxCousrse_shouldInvocationTargetException_whenIdentifierIsNameAndCoursesListIsNull() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getMaxCousrse", String.class, List.class);
        method.setAccessible(true);
        String identifier = "Name";

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, identifier, null));
    }

    @Test
    void getMaxCousrse_shouldInvocationTargetException_whenIdentifierIsDescriptionAndCoursesListIsNull()
            throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getMaxCousrse", String.class, List.class);
        method.setAccessible(true);
        String identifier = "Description";

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, identifier, null));
    }

    @Test
    void getMaxCousrse_shouldInvocationTargetException_whenIdentifierIsNameAndCourseNameIsNull() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getMaxCousrse", String.class, List.class);
        method.setAccessible(true);
        String identifier = "Name";
        Course firstCourse = new Course("CourseName_1", "Description_1");
        Course secondCourse = new Course(null, "Description_2");
        List<Course> courses = new ArrayList<>();
        courses.add(firstCourse);
        courses.add(secondCourse);

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, identifier, courses));
    }

    @Test
    void getMaxCousrse_shouldInvocationTargetException_whenIdentifierIsDescriptionAndCourseDescriptionIsNull()
            throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("getMaxCousrse", String.class, List.class);
        method.setAccessible(true);
        String identifier = "Description";
        Course firstCourse = new Course("CourseName_1", "Description_1");
        Course secondCourse = new Course("CourseName_2", null);
        List<Course> courses = new ArrayList<>();
        courses.add(firstCourse);
        courses.add(secondCourse);

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, identifier, courses));
    }

    @Test
    void formatLineWithPluses_shouldInvocationTargetException_whenLineIsNull() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("formatLineWithPluses", String.class, Integer.class);
        method.setAccessible(true);
        String line = null;
        Integer plusIndexInsideLine = 2;

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, line, plusIndexInsideLine));
    }

    @Test
    void formatLineWithPluses_shouldInvocationTargetException_whenPlusIndexInsideLineIsNull() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("formatLineWithPluses", String.class, Integer.class);
        method.setAccessible(true);
        String line = "-------------";
        Integer plusIndexInsideLine = null;

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, line, plusIndexInsideLine));
    }

    @Test
    void formatLineWithPluses_shouldInvocationTargetException_whenPlusIndexInsideLineMoreThanLineLength()
            throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("formatLineWithPluses", String.class, Integer.class);
        method.setAccessible(true);
        String line = "---";
        Integer plusIndexInsideLine = 3;

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, line, plusIndexInsideLine));
    }

    @Test
    void formatLineWithPluses_shouldIneWithPluses_whenLineAndPlusIndexInsideLineAreCorrect() throws Exception {
        Method method = SchoolViewImpl.class.getDeclaredMethod("formatLineWithPluses", String.class, Integer.class);
        method.setAccessible(true);
        String line = "-----------";
        Integer plusIndexInsideLine = 5;
        String expectedLineWithPluses = "+----+----+";

        String actualLineWithPluses = method.invoke(view, line, plusIndexInsideLine).toString();

        assertEquals(expectedLineWithPluses, actualLineWithPluses);
    }

    @AfterEach
    void tearDown() {
        System.setOut(System.out);
    }

}
