package ua.foxminded.schoolapp.service.generate.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.schoolapp.exception.FileReadingException;
import ua.foxminded.schoolapp.service.generate.Generatable;
import ua.foxminded.schoolapp.service.generate.Reader;
import ua.foxminded.schoolapp.service.generate.StudentGeneratorTestHelper;
import ua.foxminded.schoolapp.dto.StudentDto;
import ua.foxminded.schoolapp.exception.DataGenerationException;

@SpringBootTest(classes = { StudentsGenerator.class })
class StudentsGeneratorTest {

    private final StudentGeneratorTestHelper helper = new StudentGeneratorTestHelper();

    @MockBean
    private Reader readerMock;

    @Autowired
    private Generatable<StudentDto> studentsGenerator;

    @Test
    void toGenerate_shouldListOfStudentsWithTestNamesAndRandomGroupIds_whenReaderReturnTwentyStudentsNames() {
        List<String> testFirstNames = helper.getTestListOf("first_names", 20);
        List<String> testLastNames = helper.getTestListOf("last_names", 20);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenReturn(testLastNames);

        List<StudentDto> actualStudents = studentsGenerator.toGenerate();

        for (StudentDto student : actualStudents) {
            assertTrue(testFirstNames.contains(student.getFirstName()));
            assertTrue(testLastNames.contains(student.getLastName()));
        }
    }

    @Test
    void toGenerate_shouldListOfStudentsWithSizeTwoHundred_whenReaderReturnTwentyStudentsNames() {
        int expectedSize = 200;
        List<String> testFirstNames = helper.getTestListOf("first_names", 20);
        List<String> testLastNames = helper.getTestListOf("last_names", 20);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenReturn(testLastNames);

        int actualSize = studentsGenerator.toGenerate().size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    void toGenerate_shouldListOfStudentsWithTestNamesAndRandomGroupIds_whenReaderReturnMoreThanTwentyStudentsNames() {
        List<String> testFirstNames = helper.getTestListOf("first_names", 50);
        List<String> testLastNames = helper.getTestListOf("last_names", 50);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenReturn(testLastNames);

        List<StudentDto> actualStudents = studentsGenerator.toGenerate();

        for (StudentDto student : actualStudents) {
            assertTrue(testFirstNames.contains(student.getFirstName()));
            assertTrue(testLastNames.contains(student.getLastName()));
        }
    }

    @Test
    void toGenerate_shouldDataSetUpException_whenReaderReturnEmptyStudentsFirstNamesList() {
        List<String> testFirstNames = new ArrayList<>();
        List<String> testLastNames = helper.getTestListOf("last_names", 20);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenReturn(testLastNames);

        assertThrows(DataGenerationException.class, () -> studentsGenerator.toGenerate());
    }

    @Test
    void toGenerate_shouldDataSetUpException_whenReaderReturnLessThanTwentyStudentsNames() {
        List<String> testFirstNames = helper.getTestListOf("first_names", 10);
        List<String> testLastNames = helper.getTestListOf("last_names", 10);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenReturn(testLastNames);

        assertThrows(DataGenerationException.class, () -> studentsGenerator.toGenerate());
    }

    @Test
    void toGenerate_shouldFileReadingException_whenReaderThrowsFileReadingException() {
        List<String> testFirstNames = helper.getTestListOf("first_names", 10);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt"))
                .thenThrow(FileReadingException.class);

        assertThrows(FileReadingException.class, () -> studentsGenerator.toGenerate());
    }

    @Test
    void getStudentsFullName_shouldGeneratedStudentsFullName_whenReaderReturnTwentyStudentsNames() throws Exception {
        Method method = StudentsGenerator.class.getDeclaredMethod("getStudentsFullName");
        method.setAccessible(true);
        List<String> testFirstNames = helper.getTestListOf("first_names", 20);
        List<String> testLastNames = helper.getTestListOf("last_names", 20);
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
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenReturn(testLastNames);

        assertThrows(InvocationTargetException.class, () -> method.invoke(studentsGenerator));
    }

    @Test
    void getStudentsFullName_shouldInvocationTargetException_whenReaderReturnLessThanTwentyStudentsNames()
            throws Exception {
        Method method = StudentsGenerator.class.getDeclaredMethod("getStudentsFullName");
        method.setAccessible(true);
        List<String> testFirstNames = helper.getTestListOf("first_names", 5);
        List<String> testLastNames = helper.getTestListOf("last_names", 5);
        when(readerMock.readFileAndPopulateListWithLines("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateListWithLines("students/last_names.txt")).thenReturn(testLastNames);

        assertThrows(InvocationTargetException.class, () -> method.invoke(studentsGenerator));
    }

}
