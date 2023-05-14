package ua.foxminded.schoolapp.datasetup;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.exception.DataSetUpException;
import ua.foxminded.schoolapp.exception.FileReadingException;

class CoursesGeneratorTest {

    Reader readerMock;
    Generatable<Course> coursesGenerator;
    CoursesGeneratorTestHelper helper;

    @BeforeEach
    void setUp() {
        readerMock = mock(Reader.class);
        helper = new CoursesGeneratorTestHelper();
    }

    @Test
    void toGenerate_shouldNullPointerException_whenReaderIsNull() {

        assertThrows(NullPointerException.class, () -> {
            coursesGenerator = new CoursesGenerator(null);
        });
    }

    @Test
    void toGenerate_shouldTestCursesList_whenReaderReturnTenCoursesNamesAndDescriptions() {
        List<String> coursesNames = helper.getTestListOf("courses_names", 10);
        List<String> coursesDescriptions = helper.getTestListOf("courses_descriptions", 10);
        coursesGenerator = new CoursesGenerator(readerMock);
        when(readerMock.readFileAndPopulateList("courses/courses.txt")).thenReturn(coursesNames);
        when(readerMock.readFileAndPopulateList("courses/descriptions.txt")).thenReturn(coursesDescriptions);

        List<Course> courses = coursesGenerator.toGenerate();

        for (Course course : courses) {
            assertTrue(coursesNames.contains(course.getCourseName()));
            assertTrue(coursesDescriptions.contains(course.getDescription()));
        }
    }

    @Test
    void toGenerate_shouldTestCursesList_whenReaderReturnMoreThanTenCoursesNamesAndDescriptions() {
        List<String> coursesNames = helper.getTestListOf("courses_names", 40);
        List<String> coursesDescriptions = helper.getTestListOf("courses_descriptions", 40);
        coursesGenerator = new CoursesGenerator(readerMock);
        when(readerMock.readFileAndPopulateList("courses/courses.txt")).thenReturn(coursesNames);
        when(readerMock.readFileAndPopulateList("courses/descriptions.txt")).thenReturn(coursesDescriptions);

        List<Course> courses = coursesGenerator.toGenerate();

        for (Course course : courses) {
            assertTrue(coursesNames.contains(course.getCourseName()));
            assertTrue(coursesDescriptions.contains(course.getDescription()));
        }
    }

    @Test
    void toGenerate_shouldTestCursesListWithSizeTen_whenReaderReturnTenCoursesNamesAndDescriptions() {
        int expectedCoursesListSize = 10;
        List<String> coursesNames = helper.getTestListOf("courses_names", 40);
        List<String> coursesDescriptions = helper.getTestListOf("courses_descriptions", 40);
        coursesGenerator = new CoursesGenerator(readerMock);
        when(readerMock.readFileAndPopulateList("courses/courses.txt")).thenReturn(coursesNames);
        when(readerMock.readFileAndPopulateList("courses/descriptions.txt")).thenReturn(coursesDescriptions);

        int actualCoursesListSize = coursesGenerator.toGenerate().size();

        assertEquals(expectedCoursesListSize, actualCoursesListSize);
    }

    @Test
    void toGenerate_shouldDataSetUpException_whenReaderReturnLessThanTenCoursesNamesAndDescriptions() {
        List<String> coursesNames = helper.getTestListOf("courses_names", 6);
        List<String> coursesDescriptions = helper.getTestListOf("courses_descriptions", 6);
        coursesGenerator = new CoursesGenerator(readerMock);
        when(readerMock.readFileAndPopulateList("courses/courses.txt")).thenReturn(coursesNames);
        when(readerMock.readFileAndPopulateList("courses/descriptions.txt")).thenReturn(coursesDescriptions);

        assertThrows(DataSetUpException.class, () -> {
            coursesGenerator.toGenerate();
        });
    }

    @Test
    void toGenerate_shouldDataSetUpException_whenReaderReturnEmptyDescriptionsList() {
        List<String> coursesNames = helper.getTestListOf("courses_names", 10);
        List<String> coursesDescriptions = new ArrayList<>();
        coursesGenerator = new CoursesGenerator(readerMock);
        when(readerMock.readFileAndPopulateList("courses/courses.txt")).thenReturn(coursesNames);
        when(readerMock.readFileAndPopulateList("courses/descriptions.txt")).thenReturn(coursesDescriptions);

        assertThrows(DataSetUpException.class, () -> {
            coursesGenerator.toGenerate();
        });
    }

    @Test
    void toGenerate_shouldFileReadingException_whenReaderThrowFileReadingException() {
        List<String> coursesDescriptions = helper.getTestListOf("courses_descriptions", 10);
        coursesGenerator = new CoursesGenerator(readerMock);
        when(readerMock.readFileAndPopulateList("courses/courses.txt")).thenThrow(FileReadingException.class);
        when(readerMock.readFileAndPopulateList("courses/descriptions.txt")).thenReturn(coursesDescriptions);

        assertThrows(FileReadingException.class, () -> {
            coursesGenerator.toGenerate();
        });
    }

}
