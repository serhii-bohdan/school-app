package ua.foxminded.schoolapp.datasetup;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolapp.exception.FileReadingException;
import ua.foxminded.schoolapp.model.Student;

class StudentsGeneratorTest {

    Reader readerMock;
    Generatable<Student> studentsGenerator;

    @BeforeEach
    void setUp() {
        readerMock = mock(Reader.class);
    }

    @Test
    void toGenerate_shouldNullPointerException_whenReaderIsNull() {

        assertThrows(NullPointerException.class, () -> {
            studentsGenerator = new StudentsGenerator(null);
        });
    }

    @Test
    void toGenerate_shouldListOfStudentsWithTestNamesAndRandomGroupIds_whenReaderReturnTwentyStudentsNames() {
        List<String> testFirstNames = getTestListOf("first_names", 20);
        List<String> testLastNames = getTestListOf("last_names", 20);
        studentsGenerator = new StudentsGenerator(readerMock);
        when(readerMock.readFileAndPopulateList("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateList("students/last_names.txt")).thenReturn(testLastNames);

        List<Student> actualStudents = studentsGenerator.toGenerate();

        for (Student student : actualStudents) {
            assertTrue(testFirstNames.contains(student.getFirstName()));
            assertTrue(testLastNames.contains(student.getLastName()));
            assertTrue(student.getGroupId() >= 1 && student.getGroupId() <= 10);
        }
    }

    @Test
    void toGenerate_shouldListOfStudentsWithSizeTwoHundred_whenReaderReturnTwentyStudentsNames() {
        List<String> testFirstNames = getTestListOf("first_names", 20);
        List<String> testLastNames = getTestListOf("last_names", 20);
        studentsGenerator = new StudentsGenerator(readerMock);
        when(readerMock.readFileAndPopulateList("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateList("students/last_names.txt")).thenReturn(testLastNames);
        int expectedSize = 200;

        int actualSize = studentsGenerator.toGenerate().size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    void toGenerate_shouldListOfStudentsWithTestNamesAndRandomGroupIds_whenReaderReturnMoreThanTwentyStudentsNames() {
        List<String> testFirstNames = getTestListOf("first_names", 50);
        List<String> testLastNames = getTestListOf("last_names", 50);
        studentsGenerator = new StudentsGenerator(readerMock);
        when(readerMock.readFileAndPopulateList("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateList("students/last_names.txt")).thenReturn(testLastNames);

        List<Student> actualStudents = studentsGenerator.toGenerate();

        for (Student student : actualStudents) {
            assertTrue(testFirstNames.contains(student.getFirstName()));
            assertTrue(testLastNames.contains(student.getLastName()));
            assertTrue(student.getGroupId() >= 1 && student.getGroupId() <= 10);
        }
    }

    @Test
    void toGenerate_shouldIndexOutOfBoundsException_whenReaderReturnEmptyStudentsFirstNamesList() {
        List<String> testFirstNames = new ArrayList<>();
        List<String> testLastNames = getTestListOf("last_names", 20);
        studentsGenerator = new StudentsGenerator(readerMock);
        when(readerMock.readFileAndPopulateList("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateList("students/last_names.txt")).thenReturn(testLastNames);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            studentsGenerator.toGenerate();
        });
    }

    @Test
    void toGenerate_shouldIndexOutOfBoundsException_whenReaderReturnLessThanTwentyStudentsNames() {
        List<String> testFirstNames = getTestListOf("first_names", 10);
        List<String> testLastNames = getTestListOf("last_names", 10);
        studentsGenerator = new StudentsGenerator(readerMock);
        when(readerMock.readFileAndPopulateList("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateList("students/last_names.txt")).thenReturn(testLastNames);

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            studentsGenerator.toGenerate();
        });
    }

    @Test
    void toGenerate_shouldFileReadingException_whenReaderThrowsFileReadingException() {
        List<String> testFirstNames = getTestListOf("first_names", 10);
        studentsGenerator = new StudentsGenerator(readerMock);
        when(readerMock.readFileAndPopulateList("students/first_names.txt")).thenReturn(testFirstNames);
        when(readerMock.readFileAndPopulateList("students/last_names.txt")).thenThrow(FileReadingException.class);

        assertThrows(FileReadingException.class, () -> {
            studentsGenerator.toGenerate();
        });
    }

    private List<String> getTestListOf(String switcher, int numbersOfNameToGenerate) {
        String firstOrLastName;

        if ("first_names".equals(switcher) && numbersOfNameToGenerate > 0) {
            firstOrLastName = "First_Name_";
        } else if ("last_names".equals(switcher) && numbersOfNameToGenerate > 0) {
            firstOrLastName = "Last_Name_";
        } else {
            throw new IllegalArgumentException();
        }

        return IntStream.rangeClosed(1, numbersOfNameToGenerate)
                        .mapToObj(i -> firstOrLastName + i)
                        .toList();
    }

}
