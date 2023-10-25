package ua.foxminded.schoolapp.service.generate.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.schoolapp.service.generate.CoursesGeneratorTestHelper;
import ua.foxminded.schoolapp.service.generate.Generatable;
import ua.foxminded.schoolapp.service.generate.Reader;
import ua.foxminded.schoolapp.dto.CourseDto;
import ua.foxminded.schoolapp.exception.DataGenerationException;
import ua.foxminded.schoolapp.exception.FileReadingException;

@SpringBootTest(classes = { CoursesGenerator.class })
class CoursesGeneratorTest {

    private final CoursesGeneratorTestHelper helper = new CoursesGeneratorTestHelper();;

    @MockBean
    private Reader readerMock;

    @Autowired
    private Generatable<CourseDto> coursesGenerator;

    @Test
    void toGenerate_shouldTestCursesList_whenReaderReturnTenCoursesNamesAndDescriptions() {
        List<String> coursesNames = helper.getTestListOf("courses_names", 10);
        List<String> coursesDescriptions = helper.getTestListOf("courses_descriptions", 10);
        when(readerMock.readFileAndPopulateListWithLines("courses/courses.txt")).thenReturn(coursesNames);
        when(readerMock.readFileAndPopulateListWithLines("courses/descriptions.txt")).thenReturn(coursesDescriptions);

        List<CourseDto> courses = coursesGenerator.toGenerate();

        for (CourseDto course : courses) {
            assertTrue(coursesNames.contains(course.getCourseName()));
            assertTrue(coursesDescriptions.contains(course.getDescription()));
        }
    }

    @Test
    void toGenerate_shouldTestCursesList_whenReaderReturnMoreThanTenCoursesNamesAndDescriptions() {
        List<String> coursesNames = helper.getTestListOf("courses_names", 40);
        List<String> coursesDescriptions = helper.getTestListOf("courses_descriptions", 40);
        when(readerMock.readFileAndPopulateListWithLines("courses/courses.txt")).thenReturn(coursesNames);
        when(readerMock.readFileAndPopulateListWithLines("courses/descriptions.txt")).thenReturn(coursesDescriptions);

        List<CourseDto> courses = coursesGenerator.toGenerate();

        for (CourseDto course : courses) {
            assertTrue(coursesNames.contains(course.getCourseName()));
            assertTrue(coursesDescriptions.contains(course.getDescription()));
        }
    }

    @Test
    void toGenerate_shouldTestCursesListWithSizeTen_whenReaderReturnTenCoursesNamesAndDescriptions() {
        int expectedCoursesListSize = 10;
        List<String> coursesNames = helper.getTestListOf("courses_names", 40);
        List<String> coursesDescriptions = helper.getTestListOf("courses_descriptions", 40);
        when(readerMock.readFileAndPopulateListWithLines("courses/courses.txt")).thenReturn(coursesNames);
        when(readerMock.readFileAndPopulateListWithLines("courses/descriptions.txt")).thenReturn(coursesDescriptions);

        int actualCoursesListSize = coursesGenerator.toGenerate().size();

        assertEquals(expectedCoursesListSize, actualCoursesListSize);
    }

    @Test
    void toGenerate_shouldDataSetUpException_whenReaderReturnLessThanTenCoursesNamesAndDescriptions() {
        List<String> coursesNames = helper.getTestListOf("courses_names", 6);
        List<String> coursesDescriptions = helper.getTestListOf("courses_descriptions", 6);
        when(readerMock.readFileAndPopulateListWithLines("courses/courses.txt")).thenReturn(coursesNames);
        when(readerMock.readFileAndPopulateListWithLines("courses/descriptions.txt")).thenReturn(coursesDescriptions);

        assertThrows(DataGenerationException.class, () -> coursesGenerator.toGenerate());
    }

    @Test
    void toGenerate_shouldDataSetUpException_whenReaderReturnEmptyDescriptionsList() {
        List<String> coursesNames = helper.getTestListOf("courses_names", 10);
        List<String> coursesDescriptions = new ArrayList<>();
        when(readerMock.readFileAndPopulateListWithLines("courses/courses.txt")).thenReturn(coursesNames);
        when(readerMock.readFileAndPopulateListWithLines("courses/descriptions.txt")).thenReturn(coursesDescriptions);

        assertThrows(DataGenerationException.class, () -> coursesGenerator.toGenerate());
    }

    @Test
    void toGenerate_shouldFileReadingException_whenReaderThrowFileReadingException() {
        List<String> coursesDescriptions = helper.getTestListOf("courses_descriptions", 10);
        when(readerMock.readFileAndPopulateListWithLines("courses/courses.txt")).thenThrow(FileReadingException.class);
        when(readerMock.readFileAndPopulateListWithLines("courses/descriptions.txt")).thenReturn(coursesDescriptions);

        assertThrows(FileReadingException.class, () -> coursesGenerator.toGenerate());
    }

}
