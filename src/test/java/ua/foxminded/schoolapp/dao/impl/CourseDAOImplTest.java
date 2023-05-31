package ua.foxminded.schoolapp.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.CourseDao;
import ua.foxminded.schoolapp.exception.*;
import ua.foxminded.schoolapp.model.*;

class CourseDAOImplTest {

    final static String URL = "jdbc:h2:~/test;MODE=PostgreSQL";
    final static String USER = "sa";
    final static String PASSWORD = "1234";

    Connectable connectorMock;
    CourseDao courseDao;

    @BeforeAll
    static void setUpBeforeClass() {
        String sqlScript = """
                CREATE TABLE courses (
                  course_id SERIAL PRIMARY KEY,
                  course_name VARCHAR(25) NOT NULL,
                  course_description TEXT NOT NULL
                );
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setUp() {
        connectorMock = mock(Connectable.class);
    }

    @Test
    void coursedaoimpl_shouldNullPointerException_whenConnectorIsNull() {
        assertThrows(NullPointerException.class, () -> new CourseDaoImpl(null));
    }

    @Test
    void save_shouldNullPointerException_whenCourseIsNull() {
        courseDao = new CourseDaoImpl(connectorMock);

        try {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertThrows(NullPointerException.class, () -> courseDao.save(null));
    }

    @Test
    void save_shouldDAOException_whenCourseFieldsNotInitialized() {
        courseDao = new CourseDaoImpl(connectorMock);
        Course course = new Course();

        try {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> courseDao.save(course));
    }

    @Test
    void save_shouldDAOException_whenCourseDescriptionNotInitialized() {
        courseDao = new CourseDaoImpl(connectorMock);
        Course course = new Course();
        course.setCourseName("CourseName");

        try {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> courseDao.save(course));
    }

    @Test
    void save_shouldDAOException_whenCourseNameContainsMoreThanTwentyFiveCharacters() {
        courseDao = new CourseDaoImpl(connectorMock);
        Course course = new Course("CourseNameThatContainsMoreThanTwentyFiveCharacters", "Description");

        try {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> courseDao.save(course));
    }

    @Test
    void save_shouldOneRecordInTestDatabase_whenCourseNameAndDescriptionIsEmpty() {
        courseDao = new CourseDaoImpl(connectorMock);
        Course course = new Course("", "");
        String actualCourseName = null;
        String actualCourseDescription = null;
        int actualcourseId = 0;
        int recordNumber = 0;

        try (Connection connection = getTestConnection()) {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            recordNumber = courseDao.save(course);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("""
                    SELECT course_id, course_name, course_description
                    FROM courses
                    WHERE course_name = '' AND course_description = '';
                    """);

            while (resultSet.next()) {
                actualcourseId = resultSet.getInt("course_id");
                actualCourseName = resultSet.getString("course_name");
                actualCourseDescription = resultSet.getString("course_description");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(1, actualcourseId);
        assertEquals("", actualCourseName);
        assertEquals("", actualCourseDescription);
        assertEquals(1, recordNumber);
    }

    @Test
    void save_shouldOneRecordInTestDatabase_whenCourseNameAndDescriptionIsOnlySpaces() {
        courseDao = new CourseDaoImpl(connectorMock);
        Course course = new Course("    ", "      ");
        String actualCourseName = null;
        String actualCourseDescription = null;
        int actualcourseId = 0;
        int recordNumber = 0;

        try (Connection connection = getTestConnection()) {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            recordNumber = courseDao.save(course);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("""
                    SELECT course_id, course_name, course_description
                    FROM courses
                    WHERE course_name = '    ' AND course_description = '      ';
                    """);

            while (resultSet.next()) {
                actualcourseId = resultSet.getInt("course_id");
                actualCourseName = resultSet.getString("course_name");
                actualCourseDescription = resultSet.getString("course_description");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(1, actualcourseId);
        assertEquals("    ", actualCourseName);
        assertEquals("      ", actualCourseDescription);
        assertEquals(1, recordNumber);
    }

    @Test
    void save_shouldOneRecordInTestDatabase_whenCourseIsCorrectAndSavedSuccessfully() {
        courseDao = new CourseDaoImpl(connectorMock);
        Course course = new Course("CourseName", "Description");
        String actualCourseName = null;
        String actualCourseDescription = null;
        int actualcourseId = 0;
        int recordNumber = 0;

        try (Connection connection = getTestConnection()) {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            recordNumber = courseDao.save(course);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("""
                    SELECT course_id, course_name, course_description
                    FROM courses
                    WHERE course_name = 'CourseName' AND course_description = 'Description';
                    """);

            while (resultSet.next()) {
                actualcourseId = resultSet.getInt("course_id");
                actualCourseName = resultSet.getString("course_name");
                actualCourseDescription = resultSet.getString("course_description");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(1, actualcourseId);
        assertEquals("CourseName", actualCourseName);
        assertEquals("Description", actualCourseDescription);
        assertEquals(1, recordNumber);
    }

    @Test
    void findAllCourses_shouldDAOException_whenThrownSQLException() {
        courseDao = new CourseDaoImpl(connectorMock);

        try {
            when(connectorMock.getConnection()).thenThrow(SQLException.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> courseDao.findAllCourses());
    }

    @Test
    void findAllCourses_shouldEmptyList_whenDatabaseEmpty() {
        courseDao = new CourseDaoImpl(connectorMock);
        List<Course> exceptAllAvailableCourses = new ArrayList<>();
        List<Course> actualAllAvailableCourses = null;

        try {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            actualAllAvailableCourses = courseDao.findAllCourses();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(exceptAllAvailableCourses, actualAllAvailableCourses);
    }

    @Test
    void findAllCourses_shouldListAllAvailableCoursesInDatabase_whenDatabaseContainsCourses() {
        courseDao = new CourseDaoImpl(connectorMock);
        List<Course> exceptAllAvailableCourses = new ArrayList<>();
        List<Course> actualAllAvailableCourses = new ArrayList<>();
        String sqlScript = """
                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description'),
                       ('CourseName', 'Description'),
                       ('CourseName', 'Description');
                """;

        for (int i = 1; i <= 3; i++) {
            Course course = new Course();
            course.setId(i);
            course.setCourseName("CourseName");
            course.setDescription("Description");
            exceptAllAvailableCourses.add(course);
        }

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            actualAllAvailableCourses = courseDao.findAllCourses();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(exceptAllAvailableCourses, actualAllAvailableCourses);
    }

    @AfterEach
    void tearDown() {
        String sqlScript = """
                ALTER TABLE courses ALTER COLUMN course_id RESTART WITH 1;
                DELETE FROM courses;
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void tearDownAfterClass() {
        String sqlScript = """
                DROP TABLE IF EXISTS courses CASCADE;
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Connection getTestConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
