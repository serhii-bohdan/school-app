package ua.foxminded.schoolapp.service.generate.impl;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.schoolapp.exception.*;
import ua.foxminded.schoolapp.service.generate.Reader;

@SpringBootTest(classes = { ReaderImpl.class })
class ReaderImplTest {

    @Autowired
    Reader reader;

    @Test
    void readFileAndPopulateListWithLines_shouldFileReadingException_whenFileNotExist() {
        assertThrows(FileReadingException.class,
                () -> reader.readFileAndPopulateListWithLines("non-existent-file.txt"));
    }

    @Test
    void readFileAndPopulateListWithLines_shouldEmptyList_whenFileIsEmpty() {
        List<String> expectedFileLinesList = new ArrayList<>();

        List<String> actualFileLinesList = reader.readFileAndPopulateListWithLines("readingtest/empty_test.txt");

        assertEquals(expectedFileLinesList, actualFileLinesList);
    }

    @Test
    void readFileAndPopulateListWithLines_shouldListOfLines_whenFileContainsFirstNamesAndLinesWithOnlySpaces() {
        List<String> expectedFileLinesList = new ArrayList<>();
        expectedFileLinesList.add("First_Name_1");
        expectedFileLinesList.add("First_Name_2");
        expectedFileLinesList.add("First_Name_3");

        List<String> actualFileLinesList = reader.readFileAndPopulateListWithLines("readingtest/first_names_test.txt");

        assertEquals(expectedFileLinesList, actualFileLinesList);
    }

    @Test
    void readFileAndPopulateListWithLines_shouldListOfLines_whenFileContainsLastNamesAndEmptyLines() {
        List<String> expectedFileLinesList = new ArrayList<>();
        expectedFileLinesList.add("Last Name 1");
        expectedFileLinesList.add("Last Name 2");
        expectedFileLinesList.add("Last Name 3");

        List<String> actualFileLinesList = reader.readFileAndPopulateListWithLines("readingtest/last_names_test.txt");

        assertEquals(expectedFileLinesList, actualFileLinesList);
    }

    @Test
    void readFileAndPopulateListWithLines_shouldListOfLines_whenFileContainsCoursesNamesWithSpacesAtBeginningAndEnd() {
        List<String> expectedFileLinesList = new ArrayList<>();
        expectedFileLinesList.add("Course_1");
        expectedFileLinesList.add("Course_2");
        expectedFileLinesList.add("Course_3");

        List<String> actualFileLinesList = reader.readFileAndPopulateListWithLines("readingtest/courses_test.txt");

        assertEquals(expectedFileLinesList, actualFileLinesList);
    }

    @Test
    void readFileAndPopulateListWithLines_shouldListOfLines_whenFileContainsCoursesDscriptionsWithSpacesAndEmptyLines() {
        List<String> expectedFileLinesList = new ArrayList<>();
        expectedFileLinesList.add("Description One");
        expectedFileLinesList.add("Description Two");
        expectedFileLinesList.add("Description Three");

        List<String> actualFileLinesList = reader.readFileAndPopulateListWithLines("readingtest/descriptions_test.txt");

        assertEquals(expectedFileLinesList, actualFileLinesList);
    }

    @Test
    void readAllFileToString_shouldFileReadingException_whenwhenFileNotExist() {

        assertThrows(FileReadingException.class, () -> reader.readAllFileToString("non-existent-file.txt"));
    }

    @Test
    void readAllFileToString_shouldFileContentsAsString_whenFileIsSQLQuery() {
        String expectedFileContents = """
                SELECT students.first_name, courses.course_name
                FROM students
                INNER JOIN students_courses ON students.student_id = students_courses.fk_student_id
                INNER JOIN courses ON courses.course_id = students_courses.fk_course_id;""";

        String actualFileLinesList = reader.readAllFileToString("readingtest/query_test.sql");

        assertEquals(expectedFileContents, actualFileLinesList);
    }

    @Test
    void readAllFileToString_shouldEmptyString_whenFileIsEmpty() {
        String expectedFileContents = "";

        String actualFileLinesList = reader.readAllFileToString("readingtest/empty_test.txt");

        assertEquals(expectedFileContents, actualFileLinesList);
    }

}
