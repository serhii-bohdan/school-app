package ua.foxminded.schoolapp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolapp.cli.Controller;
import ua.foxminded.schoolapp.dao.ExecutorDao;
import ua.foxminded.schoolapp.datasetup.Reader;
import ua.foxminded.schoolapp.exception.DaoException;
import ua.foxminded.schoolapp.exception.FileReadingException;
import ua.foxminded.schoolapp.service.CourseService;
import ua.foxminded.schoolapp.service.GroupService;
import ua.foxminded.schoolapp.service.StudentService;

class MainTest {

    Scanner scannerMock;
    Reader readerMock;
    ExecutorDao executorDaoMock;
    GroupService groupServiceMock;
    StudentService studentServiceMock;
    CourseService courseServiceMock;
    Controller controllerMock;
    String tablesCreationSqlScript = """
            DROP TABLE IF EXISTS groups CASCADE;
            DROP TABLE IF EXISTS students CASCADE;
            DROP TABLE IF EXISTS courses CASCADE;
            DROP TABLE IF EXISTS students_courses CASCADE;

            CREATE TABLE groups (
              group_id SERIAL PRIMARY KEY,
              group_name VARCHAR(5) NOT NULL
            );

            CREATE TABLE students (
              student_id SERIAL PRIMARY KEY,
              first_name VARCHAR(25) NOT NULL,
              last_name VARCHAR(25) NOT NULL,
              group_id INTEGER REFERENCES groups(group_id)
            );

            CREATE TABLE courses (
              course_id SERIAL PRIMARY KEY,
              course_name VARCHAR(25) NOT NULL,
              course_description TEXT NOT NULL
            );

            CREATE TABLE students_courses (
              student_courses_id SERIAL PRIMARY KEY,
              student_id INTEGER REFERENCES students(student_id) ON DELETE CASCADE NOT NULL,
              course_id INTEGER REFERENCES courses(course_id) ON DELETE CASCADE NOT NULL
            );""";

    @BeforeEach
    void setUp() {
        scannerMock = mock(Scanner.class);
        readerMock = mock(Reader.class);
        executorDaoMock = mock(ExecutorDao.class);
        groupServiceMock = mock(GroupService.class);
        studentServiceMock = mock(StudentService.class);
        courseServiceMock = mock(CourseService.class);
        controllerMock = mock(Controller.class);
    }

    @Test
    void main_shouldInvokedAllMethodsInMainMethod_whenTheseMethodsWorkCorrectly() {
        Main.setScanner(scannerMock);
        Main.setReader(readerMock);
        Main.setExecutorDao(executorDaoMock);
        Main.setGroupService(groupServiceMock);
        Main.setStudentService(studentServiceMock);
        Main.setCourseService(courseServiceMock);
        Main.setController(controllerMock);
        when(readerMock.readAllFileToString("sql/tables_creation.sql")).thenReturn(tablesCreationSqlScript);

        Main.main(new String[] {});

        verify(readerMock, times(1)).readAllFileToString("sql/tables_creation.sql");
        verify(executorDaoMock).executeSqlScript(tablesCreationSqlScript);
        verify(groupServiceMock, times(1)).initGroups();
        verify(studentServiceMock, times(1)).initStudents();
        verify(courseServiceMock, times(1)).initCourses();
        verify(studentServiceMock, times(1)).initStudentsCoursesTable();
        verify(controllerMock).runSchoolApp();
        verify(scannerMock).close();
    }

    @Test
    void main_shouldFileReadingException_whenReaderThrowFileReadingException() {
        Main.setReader(readerMock);
        when(readerMock.readAllFileToString("sql/tables_creation.sql"))
                .thenThrow(new FileReadingException("Failed to read file."));

        assertThrows(FileReadingException.class, () -> Main.main(new String[] {}));
    }

    @Test
    void main_shouldDaoException_whenExecutorDaoThrowDaoException() {
        Main.setReader(readerMock);
        Main.setExecutorDao(executorDaoMock);
        String incorrectSqlScript = "Incorrect sql script";
        when(readerMock.readAllFileToString("sql/tables_creation.sql")).thenReturn(incorrectSqlScript);
        doThrow(new DaoException("An error occurred when executing the SQL script.")).when(executorDaoMock)
                .executeSqlScript(incorrectSqlScript);

        assertThrows(DaoException.class, () -> Main.main(new String[] {}));
    }

}
