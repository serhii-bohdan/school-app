package ua.foxminded.schoolapp.datasetup.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolapp.exception.FileReadingException;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.datasetup.Generatable;
import ua.foxminded.schoolapp.datasetup.Reader;
import ua.foxminded.schoolapp.datasetup.StudentGeneratorTestHelper;
import ua.foxminded.schoolapp.exception.DataSetUpException;

class StudentsGeneratorTest {

    Reader readerMock;
    Generatable<Student> studentsGenerator;
    StudentGeneratorTestHelper helper;

    @BeforeEach
    void setUp() {
        readerMock = mock(Reader.class);
        helper = new StudentGeneratorTestHelper();
    }

    @Test
    void toGenerate_shouldNullPointerException_whenReaderIsNull() {

        assertThrows(NullPointerException.class, () -> {
            studentsGenerator = new StudentsGenerator(null);
        });
    }

    @Test
    void toGenerate_shouldListOfStudentsWithTestNamesAndRandomGroupIds_whenReaderReturnTwentyStudentsNames() {
        List<String> testFirstNames = helper.getTestListOf("first_names", 20);
        List<String> testLastNames = helper.getTestListOf("last_names", 20);
        studentsGenerator = new StudentsGenerator(readerMock);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenReturn(testLastNames);

        List<Student> actualStudents = studentsGenerator.toGenerate();

        for (Student student : actualStudents) {
            assertTrue(testFirstNames.contains(student.getFirstName()));
            assertTrue(testLastNames.contains(student.getLastName()));
            assertTrue(student.getGroupId() >= 1 && student.getGroupId() <= 10);
        }
    }

    @Test
    void toGenerate_shouldListOfStudentsWithSizeTwoHundred_whenReaderReturnTwentyStudentsNames() {
        int expectedSize = 200;
        List<String> testFirstNames = helper.getTestListOf("first_names", 20);
        List<String> testLastNames = helper.getTestListOf("last_names", 20);
        studentsGenerator = new StudentsGenerator(readerMock);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenReturn(testLastNames);

        int actualSize = studentsGenerator.toGenerate().size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    void toGenerate_shouldListOfStudentsWithTestNamesAndRandomGroupIds_whenReaderReturnMoreThanTwentyStudentsNames() {
        List<String> testFirstNames = helper.getTestListOf("first_names", 50);
        List<String> testLastNames = helper.getTestListOf("last_names", 50);
        studentsGenerator = new StudentsGenerator(readerMock);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenReturn(testLastNames);

        List<Student> actualStudents = studentsGenerator.toGenerate();

        for (Student student : actualStudents) {
            assertTrue(testFirstNames.contains(student.getFirstName()));
            assertTrue(testLastNames.contains(student.getLastName()));
            assertTrue(student.getGroupId() >= 1 && student.getGroupId() <= 10);
        }
    }

    @Test
    void toGenerate_shouldDataSetUpException_whenReaderReturnEmptyStudentsFirstNamesList() {
        List<String> testFirstNames = new ArrayList<>();
        List<String> testLastNames = helper.getTestListOf("last_names", 20);
        studentsGenerator = new StudentsGenerator(readerMock);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenReturn(testLastNames);

        assertThrows(DataSetUpException.class, () -> {
            studentsGenerator.toGenerate();
        });
    }

    @Test
    void toGenerate_shouldDataSetUpException_whenReaderReturnLessThanTwentyStudentsNames() {
        List<String> testFirstNames = helper.getTestListOf("first_names", 10);
        List<String> testLastNames = helper.getTestListOf("last_names", 10);
        studentsGenerator = new StudentsGenerator(readerMock);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenReturn(testLastNames);

        assertThrows(DataSetUpException.class, () -> {
            studentsGenerator.toGenerate();
        });
    }

    @Test
    void toGenerate_shouldFileReadingException_whenReaderThrowsFileReadingException() {
        List<String> testFirstNames = helper.getTestListOf("first_names", 10);
        studentsGenerator = new StudentsGenerator(readerMock);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenThrow(FileReadingException.class);

        assertThrows(FileReadingException.class, () -> {
            studentsGenerator.toGenerate();
        });
    }

    @Test
    void getStudentsFullName_shouldGeneratedStudentsFullName_whenReaderReturnTwentyStudentsNames()
            throws Exception {
        Method method = StudentsGenerator.class.getDeclaredMethod("getStudentsFullName");
        method.setAccessible(true);
        List<String> testFirstNames = helper.getTestListOf("first_names", 20);
        List<String> testLastNames = helper.getTestListOf("last_names", 20);
        studentsGenerator = new StudentsGenerator(readerMock);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenReturn(testLastNames);

        @SuppressWarnings("unchecked")
        List<String[]> studentsFullName = (List<String[]>) method.invoke(studentsGenerator);

        for (String[] fullName : studentsFullName) {
            assertTrue(testFirstNames.contains(fullName[0]));
            assertTrue(testLastNames.contains(fullName[1]));
        }
    }

    @Test
    void getStudentsFullName_shouldGeneratedStudentsFullName_whenReaderReturnMoreThanTwentyStudentsNames()
            throws Exception {
        Method method = StudentsGenerator.class.getDeclaredMethod("getStudentsFullName");
        method.setAccessible(true);
        List<String> testFirstNames = helper.getTestListOf("first_names", 40);
        List<String> testLastNames = helper.getTestListOf("last_names", 40);
        studentsGenerator = new StudentsGenerator(readerMock);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenReturn(testLastNames);

        @SuppressWarnings("unchecked")
        List<String[]> studentsFullName = (List<String[]>) method.invoke(studentsGenerator);

        for (String[] fullName : studentsFullName) {
            assertTrue(testFirstNames.contains(fullName[0]));
            assertTrue(testLastNames.contains(fullName[1]));
        }
    }

    @Test
    void getStudentsFullName_shouldTwoHundredGeneratedStudentsFullName_whenwhenReaderReturnTwentyStudentsNames()
            throws Exception {
        int expectedFullNamesCount = 200;
        Method method = StudentsGenerator.class.getDeclaredMethod("getStudentsFullName");
        method.setAccessible(true);
        List<String> testFirstNames = helper.getTestListOf("first_names", 20);
        List<String> testLastNames = helper.getTestListOf("last_names", 20);
        studentsGenerator = new StudentsGenerator(readerMock);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenReturn(testLastNames);

        @SuppressWarnings("unchecked")
        List<String[]> studentsFullName = (List<String[]>) method.invoke(studentsGenerator);
        int actualFullNamesCount = studentsFullName.size();

        assertEquals(expectedFullNamesCount, actualFullNamesCount);
    }

    @Test
    void getStudentsFullName_shouldInvocationTargetException_whenReaderReturnsUnequalNumberFirstAndLastNamesStudents()
            throws Exception {
        Method method = StudentsGenerator.class.getDeclaredMethod("getStudentsFullName");
        method.setAccessible(true);
        List<String> testFirstNames = helper.getTestListOf("first_names", 25);
        List<String> testLastNames = helper.getTestListOf("last_names", 20);
        studentsGenerator = new StudentsGenerator(readerMock);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenReturn(testLastNames);

        assertThrows(InvocationTargetException.class, () -> {
            method.invoke(studentsGenerator);
        });
    }

    @Test
    void getStudentsFullName_shouldInvocationTargetException_whenReaderReturnLessThanTwentyStudentsNames()
            throws Exception {
        Method method = StudentsGenerator.class.getDeclaredMethod("getStudentsFullName");
        method.setAccessible(true);
        List<String> testFirstNames = helper.getTestListOf("first_names", 5);
        List<String> testLastNames = helper.getTestListOf("last_names", 5);
        studentsGenerator = new StudentsGenerator(readerMock);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenReturn(testLastNames);

        assertThrows(InvocationTargetException.class, () -> {
            method.invoke(studentsGenerator);
        });
    }

    @Test
    void getRandomGroupIds_shouldListOfRandomNumbersFromOneToTen_whenInvokeGenerateRandomGroupIds()
            throws Exception {
        Method method = StudentsGenerator.class.getDeclaredMethod("getRandomGroupIds");
        method.setAccessible(true);
        List<String> testFirstNames = helper.getTestListOf("first_names", 20);
        List<String> testLastNames = helper.getTestListOf("last_names", 20);
        studentsGenerator = new StudentsGenerator(readerMock);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenReturn(testLastNames);

        @SuppressWarnings("unchecked")
        List<Integer> randomGroupIds = (List<Integer>) method.invoke(studentsGenerator);

        for (Integer randomGroupId : randomGroupIds) {
            assertTrue(randomGroupId >= 1 && randomGroupId <= 10);
        }
    }

    @Test
    void getRandomGroupIds_shouldListOfRandomNumbersWhereEachNumberRepeatedAtLeastTenAndNotMoreThanThirtyTimes_whenInvokeGenerateRandomGroupIds()
            throws Exception {
        Method method = StudentsGenerator.class.getDeclaredMethod("getRandomGroupIds");
        method.setAccessible(true);
        List<String> testFirstNames = helper.getTestListOf("first_names", 20);
        List<String> testLastNames = helper.getTestListOf("last_names", 20);
        studentsGenerator = new StudentsGenerator(readerMock);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenReturn(testLastNames);

        @SuppressWarnings("unchecked")
        List<Integer> randomGroupIds = (List<Integer>) method.invoke(studentsGenerator);

        for (int i = 1; i <= 10; i++) {
            assertTrue(Collections.frequency(randomGroupIds, i) >= 10);
            assertTrue(Collections.frequency(randomGroupIds, i) <= 30);
        }
    }

    @Test
    void getRandomGroupIds_shouldTwoHundredRandomGroupIds_whenInvokeGenerateRandomGroupIds() throws Exception {
        int expectedRandomNumbersCount = 200;
        Method method = StudentsGenerator.class.getDeclaredMethod("getRandomGroupIds");
        method.setAccessible(true);
        List<String> testFirstNames = helper.getTestListOf("first_names", 20);
        List<String> testLastNames = helper.getTestListOf("last_names", 20);
        studentsGenerator = new StudentsGenerator(readerMock);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenReturn(testLastNames);

        @SuppressWarnings("unchecked")
        List<Integer> randomGroupIds = (List<Integer>) method.invoke(studentsGenerator);
        int actualRandomNumbersCount = randomGroupIds.size();

        assertEquals(expectedRandomNumbersCount, actualRandomNumbersCount);
    }

}
