package ua.foxminded.schoolapp.cli;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolapp.model.Course;
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
                """;

        view.showMenu();
        String actualOutput = baos.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void printMessage_shouldPrintedNullWord_whenMessageIsNull() {
        view = new SchoolView(scannerMock);
        String expectedOutput = "null";

        view.printMessage(null);
        String actualOutput = baos.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void printMessage_shouldPrintedOnlyCRLF_whenMessageIsEmptyString() {
        view = new SchoolView(scannerMock);
        String expectedOutput = "";

        view.printMessage("");
        String actualOutput = baos.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void printMessage_shouldPrintedWord_whenMessageIsOnlyOneWord() {
        view = new SchoolView(scannerMock);
        String expectedOutput = "Word";

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
    void displayGroupsWithTheirNumberOfStudents_shouldNullPointerException_whenMapWithGroupsWithTheirNumberOfStudentsIsNull() {
        view = new SchoolView(scannerMock);

        assertThrows(NullPointerException.class, () -> view.displayGroupsWithTheirNumberOfStudents(null));
    }

    @Test
    void displayGroupsWithTheirNumberOfStudents_shouldNullPointerException_whenGroupInMapIsNull() {
        view = new SchoolView(scannerMock);
        Map<Group, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
        groupsWithTheirNumberOfStudents.put(null, 20);

        assertThrows(NullPointerException.class,
                () -> view.displayGroupsWithTheirNumberOfStudents(groupsWithTheirNumberOfStudents));
    }

    @Test
    void displayGroupsWithTheirNumberOfStudents_shouldNullPointerException_whenGroupNameInMapIsNull() {
        view = new SchoolView(scannerMock);
        Group group = new Group(null);
        Map<Group, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
        groupsWithTheirNumberOfStudents.put(group, 20);

        assertThrows(NullPointerException.class,
                () -> view.displayGroupsWithTheirNumberOfStudents(groupsWithTheirNumberOfStudents));
    }

    @Test
    void displayGroupsWithTheirNumberOfStudents_shouldNullPointerException_whenNumberOfStudentsIsNull() {
        view = new SchoolView(scannerMock);
        Group group = new Group("FS-25");
        Map<Group, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
        groupsWithTheirNumberOfStudents.put(group, null);
        String expectedDisplayedGroups = """
                Groups with their number of students:
                                       +-------+------+
                                       | FS-25 | null |
                                       +-------+------+
                """;

        view.displayGroupsWithTheirNumberOfStudents(groupsWithTheirNumberOfStudents);
        String actualDisplayedGroups = baos.toString();

        assertEquals(expectedDisplayedGroups, actualDisplayedGroups);
    }

    @Test
    void displayGroupsWithTheirNumberOfStudents_shouldDisplayedOnlySentence_whenGroupsWithTheirNumberOfStudentsMapEmpty() {
        view = new SchoolView(scannerMock);
        Map<Group, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
        String expectedDisplayedGroups = "Groups with their number of students:\n";

        view.displayGroupsWithTheirNumberOfStudents(groupsWithTheirNumberOfStudents);
        String actualDisplayedGroups = baos.toString();

        assertEquals(expectedDisplayedGroups, actualDisplayedGroups);
    }

    @Test
    void displayGroupsWithTheirNumberOfStudents_shouldDisplayedCrookedTableGroupsWithTheirNumberOfStudents_whenGroupsInMapHaveNamesWithDifferentLengths() {
        view = new SchoolView(scannerMock);
        Map<Group, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
        Group firstGroup = new Group("JRJF-84");
        Group secondGroup = new Group("QFL-03");
        Group thirdGroup = new Group("VA-72");
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
    void displayGroupsWithTheirNumberOfStudents_shouldDisplayedCrookedTableGroupsWithTheirNumberOfStudents_whenNumberOfStudentsInMapHaveDifferentLengths() {
        view = new SchoolView(scannerMock);
        Map<Group, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
        Group firstGroup = new Group("JR-84");
        Group secondGroup = new Group("QF-03");
        Group thirdGroup = new Group("VA-72");
        groupsWithTheirNumberOfStudents.put(firstGroup, 6343);
        groupsWithTheirNumberOfStudents.put(secondGroup, 23);
        groupsWithTheirNumberOfStudents.put(thirdGroup, 1);
        String expectedDisplayedGroups = """
                Groups with their number of students:
                                       +-------+------+
                                       | QF-03 | 23 |
                                       +-------+----+
                                       | VA-72 | 1 |
                                       +-------+---+
                                       | JR-84 | 6343 |
                                       +-------+------+
                """;

        view.displayGroupsWithTheirNumberOfStudents(groupsWithTheirNumberOfStudents);
        String actualDisplayedGroups = baos.toString();

        assertEquals(expectedDisplayedGroups, actualDisplayedGroups);
    }

    @Test
    void displayGroupsWithTheirNumberOfStudents_shouldDisplayedGroupsWithTheirNumberOfStudents_whenMapContainsThreeCorrectGroupsWithTheirNumberOfStudents() {
        view = new SchoolView(scannerMock);
        Map<Group, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
        Group firstGroup = new Group("JR-84");
        Group secondGroup = new Group("QL-03");
        Group thirdGroup = new Group("VA-72");
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
    void displayStudentsWithTheirCourses_shouldNullPointerException_whenMapWithStudentsAndTheirCoursesIsNull() {
        view = new SchoolView(scannerMock);

        assertThrows(NullPointerException.class, () -> view.displayStudentsWithTheirCourses(null));
    }

    @Test
    void displayStudentsWithTheirCourses_shouldNullPointerException_whenStudentInMapIsNull() {
        view = new SchoolView(scannerMock);
        Course firstCourse = new Course("CourseName_1", "Description_1");
        Course secondCourse = new Course("CourseName_2", "Description_2");
        List<Course> coursesForStudent = new ArrayList<>();
        coursesForStudent.add(firstCourse);
        coursesForStudent.add(secondCourse);
        Map<Student, List<Course>> studentsWithTheirCourses = new HashMap<>();
        studentsWithTheirCourses.put(null, coursesForStudent);

        assertThrows(NullPointerException.class, () -> view.displayStudentsWithTheirCourses(studentsWithTheirCourses));
    }

    @Test
    void displayStudentsWithTheirCourses_shouldDisplayedStudentWithNullFirstName_whenStudentFirstNameInMapIsNull() {
        view = new SchoolView(scannerMock);
        Student student = new Student(null, "LastName", 1);
        Course firstCourse = new Course("CourseName_1", "Description_1");
        Course secondCourse = new Course("CourseName_2", "Description_2");
        List<Course> coursesForStudent = new ArrayList<>();
        coursesForStudent.add(firstCourse);
        coursesForStudent.add(secondCourse);
        Map<Student, List<Course>> studentsWithTheirCourses = new HashMap<>();
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
        view = new SchoolView(scannerMock);
        Student student = new Student("FirstName", "LastName", 1);
        Map<Student, List<Course>> studentsWithTheirCourses = new HashMap<>();
        studentsWithTheirCourses.put(student, null);

        assertThrows(NullPointerException.class, () -> view.displayStudentsWithTheirCourses(studentsWithTheirCourses));
    }

    @Test
    void displayStudentsWithTheirCourses_shouldNullPointerException_whenCourseInCoursesListForStudentIsNull() {
        view = new SchoolView(scannerMock);
        Student student = new Student("FirstName", "LastName", 1);
        Course firstCourse = new Course("CourseName_1", "Description_1");
        Course secondCourse = new Course("CourseName_2", "Description_2");
        List<Course> coursesForStudent = new ArrayList<>();
        coursesForStudent.add(firstCourse);
        coursesForStudent.add(secondCourse);
        coursesForStudent.add(null);
        Map<Student, List<Course>> studentsWithTheirCourses = new HashMap<>();
        studentsWithTheirCourses.put(student, coursesForStudent);

        assertThrows(NullPointerException.class, () -> view.displayStudentsWithTheirCourses(studentsWithTheirCourses));
    }

    @Test
    void displayStudentsWithTheirCourses_shouldDisplayedNullCourseName_whenCourseNameInCoursesListForStudentIsNull() {
        view = new SchoolView(scannerMock);
        Student student = new Student("FirstName", "LastName", 1);
        Course firstCourse = new Course("CourseName_1", "Description_1");
        Course secondCourse = new Course(null, "Description_2");
        List<Course> coursesForStudent = new ArrayList<>();
        coursesForStudent.add(firstCourse);
        coursesForStudent.add(secondCourse);
        Map<Student, List<Course>> studentsWithTheirCourses = new HashMap<>();
        studentsWithTheirCourses.put(student, coursesForStudent);
        String expectedDisplayedStudentsWithCourses = """
                Students with their courses:
                +--------------------+--------------------+
                | FirstName LastName | CourseName_1, null |
                +--------------------+--------------------+
                """;

        view.displayStudentsWithTheirCourses(studentsWithTheirCourses);
        String actualDisplayedStudentsWithCourses = baos.toString();

        assertEquals(expectedDisplayedStudentsWithCourses, actualDisplayedStudentsWithCourses);
    }

    @Test
    void displayStudentsWithTheirCourses_shouldDisplayedOnlySentence_whenStudentsWithTheirCoursesMapEmpty() {
        view = new SchoolView(scannerMock);
        Map<Student, List<Course>> studentsWithTheirCourses = new HashMap<>();
        String expectedDisplayedStudentsWithCourses = """
                Students with their courses:
                """;

        view.displayStudentsWithTheirCourses(studentsWithTheirCourses);
        String actualDisplayedStudentsWithCourses = baos.toString();

        assertEquals(expectedDisplayedStudentsWithCourses, actualDisplayedStudentsWithCourses);
    }

    @Test
    void displayStudentsWithTheirCourses_shouldDisplayedCorrectTableWithStudentsAndTheirCourses_whenNamesOfSomeStudentsAreEmpty() {
        view = new SchoolView(scannerMock);
        Student firstStudent = new Student("", "", 1);
        Student secondStudent = new Student("", "LastName_2", 1);
        Student thirdStudent = new Student("FirstName_3", "", 1);
        Course firstCourse = new Course("CourseName_1", "Description_1");
        Course secondCourse = new Course("CourseName_2", "Description_2");
        Course thirdCourse = new Course("CourseName_3", "Description_3");
        Course fourthCourse = new Course("CourseName_4", "Description_4");
        List<Course> coursesForFirstStudent = new ArrayList<>();
        List<Course> coursesForSecondStudent = new ArrayList<>();
        List<Course> coursesForThirdStudent = new ArrayList<>();
        coursesForFirstStudent.add(firstCourse);
        coursesForFirstStudent.add(secondCourse);
        coursesForSecondStudent.add(firstCourse);
        coursesForSecondStudent.add(thirdCourse);
        coursesForSecondStudent.add(fourthCourse);
        coursesForThirdStudent.add(firstCourse);
        coursesForThirdStudent.add(secondCourse);
        coursesForThirdStudent.add(thirdCourse);
        coursesForThirdStudent.add(fourthCourse);
        Map<Student, List<Course>> studentsWithTheirCourses = new HashMap<>();
        studentsWithTheirCourses.put(firstStudent, coursesForFirstStudent);
        studentsWithTheirCourses.put(secondStudent, coursesForSecondStudent);
        studentsWithTheirCourses.put(thirdStudent, coursesForThirdStudent);
        String expectedDisplayedStudentsWithCourses = """
                Students with their courses:
                +--------------+--------------------------------------------------------+
                |  LastName_2  | CourseName_1, CourseName_3, CourseName_4               |
                +--------------+--------------------------------------------------------+
                |              | CourseName_1, CourseName_2                             |
                +--------------+--------------------------------------------------------+
                | FirstName_3  | CourseName_1, CourseName_2, CourseName_3, CourseName_4 |
                +--------------+--------------------------------------------------------+
                """;

        view.displayStudentsWithTheirCourses(studentsWithTheirCourses);
        String actualDisplayedStudentsWithCourses = baos.toString();

        assertEquals(expectedDisplayedStudentsWithCourses, actualDisplayedStudentsWithCourses);
    }

    @Test
    void displayStudentsWithTheirCourses_shouldDisplayedCorrectTableWithStudentsAndTheirCourses_whenNamesOfSomeCoursesAreEmpty() {
        view = new SchoolView(scannerMock);
        Student firstStudent = new Student("FirstName_1", "LastName_1", 1);
        Student secondStudent = new Student("FirstName_2", "LastName_2", 1);
        Student thirdStudent = new Student("FirstName_3", "LastName_3", 1);
        Course firstCourse = new Course("", "Description_1");
        Course secondCourse = new Course("", "Description_2");
        Course thirdCourse = new Course("CourseName_3", "Description_3");
        Course fourthCourse = new Course("CourseName_4", "Description_4");
        List<Course> coursesForFirstStudent = new ArrayList<>();
        List<Course> coursesForSecondStudent = new ArrayList<>();
        List<Course> coursesForThirdStudent = new ArrayList<>();
        coursesForFirstStudent.add(firstCourse);
        coursesForFirstStudent.add(secondCourse);
        coursesForSecondStudent.add(firstCourse);
        coursesForSecondStudent.add(thirdCourse);
        coursesForSecondStudent.add(fourthCourse);
        coursesForThirdStudent.add(firstCourse);
        coursesForThirdStudent.add(secondCourse);
        coursesForThirdStudent.add(thirdCourse);
        coursesForThirdStudent.add(fourthCourse);
        Map<Student, List<Course>> studentsWithTheirCourses = new HashMap<>();
        studentsWithTheirCourses.put(firstStudent, coursesForFirstStudent);
        studentsWithTheirCourses.put(secondStudent, coursesForSecondStudent);
        studentsWithTheirCourses.put(thirdStudent, coursesForThirdStudent);
        String expectedDisplayedStudentsWithCourses = """
                Students with their courses:
                +------------------------+--------------------------------+
                | FirstName_3 LastName_3 | , , CourseName_3, CourseName_4 |
                +------------------------+--------------------------------+
                | FirstName_1 LastName_1 | ,                              |
                +------------------------+--------------------------------+
                | FirstName_2 LastName_2 | , CourseName_3, CourseName_4   |
                +------------------------+--------------------------------+
                """;

        view.displayStudentsWithTheirCourses(studentsWithTheirCourses);
        String actualDisplayedStudentsWithCourses = baos.toString();

        assertEquals(expectedDisplayedStudentsWithCourses, actualDisplayedStudentsWithCourses);
    }

    @Test
    void displayStudentsWithTheirCourses_shouldDisplayedCorrectTableWithStudentsAndTheirCourses_whenMapContainStudentsWithOtherStudentsAndOtherCourses() {
        view = new SchoolView(scannerMock);
        Student firstStudent = new Student("FirstName__1", "LastName___1", 1);
        Student secondStudent = new Student("First_2", "LastName_2", 1);
        Student thirdStudent = new Student("F", "LastName____3", 1);
        Course firstCourse = new Course("CourseName__1", "Description_1");
        Course secondCourse = new Course("CourseName____2", "Description_2");
        Course thirdCourse = new Course("CourseName___3", "Description_3");
        Course fourthCourse = new Course("C", "Description_4");
        List<Course> coursesForFirstStudent = new ArrayList<>();
        List<Course> coursesForSecondStudent = new ArrayList<>();
        List<Course> coursesForThirdStudent = new ArrayList<>();
        coursesForFirstStudent.add(firstCourse);
        coursesForFirstStudent.add(secondCourse);
        coursesForSecondStudent.add(firstCourse);
        coursesForSecondStudent.add(thirdCourse);
        coursesForSecondStudent.add(fourthCourse);
        coursesForThirdStudent.add(firstCourse);
        coursesForThirdStudent.add(secondCourse);
        coursesForThirdStudent.add(thirdCourse);
        coursesForThirdStudent.add(fourthCourse);
        Map<Student, List<Course>> studentsWithTheirCourses = new HashMap<>();
        studentsWithTheirCourses.put(firstStudent, coursesForFirstStudent);
        studentsWithTheirCourses.put(secondStudent, coursesForSecondStudent);
        studentsWithTheirCourses.put(thirdStudent, coursesForThirdStudent);
        String expectedDisplayedStudentsWithCourses = """
                Students with their courses:
                +---------------------------+---------------------------------------------------+
                | F LastName____3           | CourseName__1, CourseName____2, CourseName___3, C |
                +---------------------------+---------------------------------------------------+
                | First_2 LastName_2        | CourseName__1, CourseName___3, C                  |
                +---------------------------+---------------------------------------------------+
                | FirstName__1 LastName___1 | CourseName__1, CourseName____2                    |
                +---------------------------+---------------------------------------------------+
                """;

        view.displayStudentsWithTheirCourses(studentsWithTheirCourses);
        String actualDisplayedStudentsWithCourses = baos.toString();

        assertEquals(expectedDisplayedStudentsWithCourses, actualDisplayedStudentsWithCourses);
    }

    @Test
    void displayCourses_shouldNullPointerException_whenCoursesListIsNull() {
        view = new SchoolView(scannerMock);

        assertThrows(NullPointerException.class, () -> view.displayCourses(null));
    }

    @Test
    void displayCourses_shouldNullPointerException_whenCourseInCoursesListIsNull() {
        view = new SchoolView(scannerMock);
        List<Course> coursesList = new ArrayList<>();
        coursesList.add(null);

        assertThrows(NullPointerException.class, () -> view.displayCourses(coursesList));
    }

    @Test
    void displayCourses_shouldNullPointerException_whenCourseNameInCoursesListIsNull() {
        view = new SchoolView(scannerMock);
        Course course = new Course(null, "Description");
        List<Course> coursesList = new ArrayList<>();
        coursesList.add(course);

        assertThrows(NullPointerException.class, () -> view.displayCourses(coursesList));
    }

    @Test
    void displayCourses_shouldNullPointerException_whenCourseDescriptionInCoursesListIsNull() {
        view = new SchoolView(scannerMock);
        Course course = new Course("CourseName", null);
        List<Course> coursesList = new ArrayList<>();
        coursesList.add(course);

        assertThrows(NullPointerException.class, () -> view.displayCourses(coursesList));
    }

    @Test
    void displayCourses_shouldDisplayedOnlySentences_whenCoursesListEmpty() {
        view = new SchoolView(scannerMock);
        List<Course> coursesList = new ArrayList<>();
        String expectedDisplayedCourses = """
                Courses with descriptions:""";

        view.displayCourses(coursesList);
        String actualDisplayedCourses = baos.toString();

        assertEquals(expectedDisplayedCourses, actualDisplayedCourses);
    }

    @Test
    void displayCourses_shouldDisplayedTableWithCoursesNamesAndDescriptions_whenSomeCoursesNamesAndDescriptionsEmpty() {
        view = new SchoolView(scannerMock);
        Course firstCourse = new Course("", "Description_1");
        Course secondCourse = new Course("CourseName_2", "");
        Course thirdCourse = new Course("", "");
        List<Course> coursesList = new ArrayList<>();
        coursesList.add(firstCourse);
        coursesList.add(secondCourse);
        coursesList.add(thirdCourse);
        String expectedDisplayedCourses = """
                Courses with descriptions:
                +--------------+---------------+
                |              | Description_1 |
                +--------------+---------------+
                | CourseName_2 |               |
                +--------------+---------------+
                |              |               |
                +--------------+---------------+""";

        view.displayCourses(coursesList);
        String actualDisplayedCourses = baos.toString();

        assertEquals(expectedDisplayedCourses, actualDisplayedCourses);
    }

    @Test
    void displayCourses_shouldDisplayedTableWithCoursesNamesAndDescriptions_whenСoursesWithDifferentNamesAndDescriptionsPresentInCourseList() {
        view = new SchoolView(scannerMock);
        Course firstCourse = new Course("CourseName______1", "Description_____1");
        Course secondCourse = new Course("CourseName_2", "Description__2");
        Course thirdCourse = new Course("Co", "De");
        List<Course> coursesList = new ArrayList<>();
        coursesList.add(firstCourse);
        coursesList.add(secondCourse);
        coursesList.add(thirdCourse);
        String expectedDisplayedCourses = """
                Courses with descriptions:
                +-------------------+-------------------+
                | CourseName______1 | Description_____1 |
                +-------------------+-------------------+
                | CourseName_2      | Description__2    |
                +-------------------+-------------------+
                | Co                | De                |
                +-------------------+-------------------+""";

        view.displayCourses(coursesList);
        String actualDisplayedCourses = baos.toString();

        assertEquals(expectedDisplayedCourses, actualDisplayedCourses);
    }

    @Test
    void makeCharacterSequence_shouldInvocationTargetException_whenCharactersCountIsNull() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("makeCharacterSequence", Integer.class, Character.class);
        method.setAccessible(true);

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, null, '-'));
    }

    @Test
    void makeCharacterSequence_shouldStringThatContainsTenNullWords_whenCharacterIsNull() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("makeCharacterSequence", Integer.class, Character.class);
        method.setAccessible(true);
        String expectedCharacterSequence = "nullnullnullnullnullnullnullnullnullnull";

        String actualCharacterSequence = method.invoke(view, 10, null).toString();

        assertEquals(expectedCharacterSequence, actualCharacterSequence);
    }

    @Test
    void makeCharacterSequence_shouldEmptyString_whenCharactersCountLessThanZero() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("makeCharacterSequence", Integer.class, Character.class);
        method.setAccessible(true);
        String expectedCharacterSequence = "";

        String actualCharacterSequence = method.invoke(view, -2, '-').toString();

        assertEquals(expectedCharacterSequence, actualCharacterSequence);
    }

    @Test
    void makeCharacterSequence_shouldEmptyString_whenCharactersCountIsZero() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("makeCharacterSequence", Integer.class, Character.class);
        method.setAccessible(true);
        String expectedCharacterSequence = "";

        String actualCharacterSequence = method.invoke(view, 0, '-').toString();

        assertEquals(expectedCharacterSequence, actualCharacterSequence);
    }

    @Test
    void makeCharacterSequence_shouldStringThatContainsFiveSpaces_whenCharacterIsSpace() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("makeCharacterSequence", Integer.class, Character.class);
        method.setAccessible(true);
        String expectedCharacterSequence = "     ";

        String actualCharacterSequence = method.invoke(view, 5, ' ').toString();

        assertEquals(expectedCharacterSequence, actualCharacterSequence);
    }

    @Test
    void makeCharacterSequence_shouldStringThatContainsEightyCharacters_whenLargeCharactersCount() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("makeCharacterSequence", Integer.class, Character.class);
        method.setAccessible(true);
        String expectedCharacterSequence = "********************************************************************************";

        String actualCharacterSequence = method.invoke(view, 80, '*').toString();

        assertEquals(expectedCharacterSequence, actualCharacterSequence);
    }

    @Test
    void getStudentFullName_shouldInvocationTargetException_whenStudentIsNull() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getStudentFullName", Student.class);
        method.setAccessible(true);
        Student student = null;

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, student));
    }

    @Test
    void getStudentFullName_shouldStudentFullNameWhereFirstNameIsNull_whenStudentFirstNameIsNull() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getStudentFullName", Student.class);
        method.setAccessible(true);
        Student student = new Student(null, "LastName", 1);
        String expectedStudentFullName = "null LastName";

        String actualStudentFullName = method.invoke(view, student).toString();

        assertEquals(expectedStudentFullName, actualStudentFullName);
    }

    @Test
    void getStudentFullName_shouldOneSpace_whenStudentFirstNameAndLastNameAreEmpty() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getStudentFullName", Student.class);
        method.setAccessible(true);
        Student student = new Student("", "", 1);
        String expectedStudentFullName = " ";

        String actualStudentFullName = method.invoke(view, student).toString();

        assertEquals(expectedStudentFullName, actualStudentFullName);
    }

    @Test
    void getStudentFullName_shouldThreeSpaces_whenStudentFirstNameAndLastNameAreOneSpace() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getStudentFullName", Student.class);
        method.setAccessible(true);
        Student student = new Student(" ", " ", 1);
        String expectedStudentFullName = "   ";

        String actualStudentFullName = method.invoke(view, student).toString();

        assertEquals(expectedStudentFullName, actualStudentFullName);
    }

    @Test
    void getStudentFullName_shouldCorrectFullName_whenStudentFirstNameAndLastNameAreCorrect() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getStudentFullName", Student.class);
        method.setAccessible(true);
        Student student = new Student("Firstname", "LastName", 1);
        String expectedStudentFullName = "Firstname LastName";

        String actualStudentFullName = method.invoke(view, student).toString();

        assertEquals(expectedStudentFullName, actualStudentFullName);
    }

    @Test
    void getCoursesEnumeration_shouldInvocationTargetException_whenCoursesListIsNull() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getCoursesEnumeration", List.class);
        method.setAccessible(true);
        List<Course> coursesList = null;

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, coursesList));
    }

    @Test
    void getCoursesEnumeration_shouldCoursesEnumerationWhereSecondCourseNameIsNull_whenSecondCourseNmaeIsNull()
            throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getCoursesEnumeration", List.class);
        method.setAccessible(true);
        Course firstCourse = new Course("CourseName_1", "Description_1");
        Course secondCourse = new Course(null, "Description_2");
        List<Course> courses = new ArrayList<>();
        courses.add(firstCourse);
        courses.add(secondCourse);
        String expectedCoursesEnumeration = "CourseName_1, null";

        String actualCoursesEnumeration = method.invoke(view, courses).toString();

        assertEquals(expectedCoursesEnumeration, actualCoursesEnumeration);
    }

    @Test
    void getCoursesEnumeration_shouldEmptyString_whenCourseListEmpty() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getCoursesEnumeration", List.class);
        method.setAccessible(true);
        List<Course> courses = new ArrayList<>();
        String expectedCoursesEnumeration = "";

        String actualCoursesEnumeration = method.invoke(view, courses).toString();

        assertEquals(expectedCoursesEnumeration, actualCoursesEnumeration);
    }

    @Test
    void getCoursesEnumeration_shouldCommaWithSpace_whenCoursesNamesAreEmpty() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getCoursesEnumeration", List.class);
        method.setAccessible(true);
        Course firstCourse = new Course("", "Description_1");
        Course secondCourse = new Course("", "Description_2");
        List<Course> courses = new ArrayList<>();
        courses.add(firstCourse);
        courses.add(secondCourse);
        String expectedCoursesEnumeration = ", ";

        String actualCoursesEnumeration = method.invoke(view, courses).toString();

        assertEquals(expectedCoursesEnumeration, actualCoursesEnumeration);
    }

    @Test
    void getCoursesEnumeration_shouldCommaWithSpaces_whenCoursesNamesAreSpaces() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getCoursesEnumeration", List.class);
        method.setAccessible(true);
        Course firstCourse = new Course("  ", "Description_1");
        Course secondCourse = new Course("  ", "Description_2");
        List<Course> courses = new ArrayList<>();
        courses.add(firstCourse);
        courses.add(secondCourse);
        String expectedCoursesEnumeration = "  ,   ";

        String actualCoursesEnumeration = method.invoke(view, courses).toString();

        assertEquals(expectedCoursesEnumeration, actualCoursesEnumeration);
    }

    @Test
    void getCoursesEnumeration_shouldCorrectCoursesEnumeration_whenCoursesNamesAreCorrect() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getCoursesEnumeration", List.class);
        method.setAccessible(true);
        Course firstCourse = new Course("CourseName_1", "Description_1");
        Course secondCourse = new Course("CourseName_2", "Description_2");
        List<Course> courses = new ArrayList<>();
        courses.add(firstCourse);
        courses.add(secondCourse);
        String expectedCoursesEnumeration = "CourseName_1, CourseName_2";

        String actualCoursesEnumeration = method.invoke(view, courses).toString();

        assertEquals(expectedCoursesEnumeration, actualCoursesEnumeration);
    }

    @Test
    void getStringWithMaxLength_shouldInvocationTargetException_whenStringsListEmpty() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getStringWithMaxLength", List.class);
        method.setAccessible(true);
        List<String> stringsList = null;

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, stringsList));
    }

    @Test
    void getStringWithMaxLength_shouldInvocationTargetException_whenStringsListContainNull() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getStringWithMaxLength", List.class);
        method.setAccessible(true);
        List<String> strings = new ArrayList<>();
        strings.add("  fff");
        strings.add(null);

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, strings));
    }

    @Test
    void getStringWithMaxLength_shouldEmptyString_whenStringsListEmpty() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getStringWithMaxLength", List.class);
        method.setAccessible(true);
        List<String> strings = new ArrayList<>();
        String expectedMaxString = "";

        String actualMaxString = method.invoke(view, strings).toString();

        assertEquals(expectedMaxString, actualMaxString);
    }

    @Test
    void getStringWithMaxLength_shouldEmptyString_whenStringsListContainOnlyEmptyString() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getStringWithMaxLength", List.class);
        method.setAccessible(true);
        List<String> strings = new ArrayList<>();
        strings.add("");
        String expectedMaxString = "";

        String actualMaxString = method.invoke(view, strings).toString();

        assertEquals(expectedMaxString, actualMaxString);
    }

    @Test
    void getStringWithMaxLength_shouldStringWithMaxLength_whenStringsListContainOnlyEmptyString() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getStringWithMaxLength", List.class);
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
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getMaxCousrse", String.class, List.class);
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
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getMaxCousrse", String.class, List.class);
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
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getMaxCousrse", String.class, List.class);
        method.setAccessible(true);
        String identifier = "Name";

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, identifier, null));
    }

    @Test
    void getMaxCousrse_shouldInvocationTargetException_whenIdentifierIsDescriptionAndCoursesListIsNull()
            throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getMaxCousrse", String.class, List.class);
        method.setAccessible(true);
        String identifier = "Description";

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, identifier, null));
    }

    @Test
    void getMaxCousrse_shouldInvocationTargetException_whenIdentifierIsNameAndCourseNameIsNull() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getMaxCousrse", String.class, List.class);
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
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("getMaxCousrse", String.class, List.class);
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
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("formatLineWithPluses", String.class, Integer.class);
        method.setAccessible(true);
        String line = null;
        Integer plusIndexInsideLine = 2;

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, line, plusIndexInsideLine));
    }

    @Test
    void formatLineWithPluses_shouldInvocationTargetException_whenPlusIndexInsideLineIsNull() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("formatLineWithPluses", String.class, Integer.class);
        method.setAccessible(true);
        String line = "-------------";
        Integer plusIndexInsideLine = null;

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, line, plusIndexInsideLine));
    }

    @Test
    void formatLineWithPluses_shouldInvocationTargetException_whenPlusIndexInsideLineMoreThanLineLength()
            throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("formatLineWithPluses", String.class, Integer.class);
        method.setAccessible(true);
        String line = "---";
        Integer plusIndexInsideLine = 3;

        assertThrows(InvocationTargetException.class, () -> method.invoke(view, line, plusIndexInsideLine));
    }

    @Test
    void formatLineWithPluses_shouldIneWithPluses_whenLineAndPlusIndexInsideLineAreCorrect() throws Exception {
        view = new SchoolView(scannerMock);
        Method method = SchoolView.class.getDeclaredMethod("formatLineWithPluses", String.class, Integer.class);
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
