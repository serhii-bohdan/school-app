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

class CourseDaoImplTest {

    final static String URL = "jdbc:h2:~/test;MODE=PostgreSQL";
    final static String USER = "sa";
    final static String PASSWORD = "1234";

    Connectable connectorMock;
    CourseDao courseDao;

    @BeforeAll
    static void setUpBeforeClass() {
        String sqlScript = """
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
    void save_shouldDaoException_whenCourseFieldsNotInitialized() {
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
    void save_shouldDaoException_whenCourseDescriptionNotInitialized() {
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
    void save_shouldDaoException_whenCourseNameContainsMoreThanTwentyFiveCharacters() {
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
    void findAll_shouldDaoException_whenThrownSQLException() {
        courseDao = new CourseDaoImpl(connectorMock);

        try {
            when(connectorMock.getConnection()).thenThrow(SQLException.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> courseDao.findAll());
    }

    @Test
    void findAll_shouldEmptyCoursesList_whenCoursesTableEmpty() {
        courseDao = new CourseDaoImpl(connectorMock);
        List<Course> exceptAllAvailableCourses = new ArrayList<>();
        List<Course> actualAllAvailableCourses = null;

        try {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            actualAllAvailableCourses = courseDao.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(exceptAllAvailableCourses, actualAllAvailableCourses);
    }

    @Test
    void findAll_shouldListAllAvailableCoursesInTable_whenCoursesTableContainsCourses() {
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
            actualAllAvailableCourses = courseDao.findAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(exceptAllAvailableCourses, actualAllAvailableCourses);
    }

    @Test
    void findCoursesForStudent_shouldNullPointerException_whenStudentIsNull() {
        courseDao = new CourseDaoImpl(connectorMock);

        try {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertThrows(NullPointerException.class, () -> courseDao.findCoursesForStudent(null));
    }

    @Test
    void findCoursesForStudent_shouldDaoException_whenConnectorThrowSQLException() {
        courseDao = new CourseDaoImpl(connectorMock);
        Student student = new Student("FirstName", "LastName", 1);
        student.setId(1);

        try {
            when(connectorMock.getConnection()).thenThrow(SQLException.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> courseDao.findCoursesForStudent(student));
    }

    @Test
    void findCoursesForStudent_shouldEmptyCoursesListForStudent_whenStudentIsNotRegisteredOnCourses() {
        courseDao = new CourseDaoImpl(connectorMock);
        List<Course> exceptStudentCourses = new ArrayList<>();
        List<Course> actualStudentCourses = null;
        Student student = new Student("FirstName", "LastName", 1);
        student.setId(1);
        String sqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName_1', 'Description_1'),
                       ('CourseName_2', 'Description_2'),
                       ('CourseName_3', 'Description_3');
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            actualStudentCourses = courseDao.findCoursesForStudent(student);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(exceptStudentCourses, actualStudentCourses);
    }

    @Test
    void findCoursesForStudent_shouldEmptyCoursesListForStudent_whenThereIsNoSuchStudentInStudentsTable() {
        courseDao = new CourseDaoImpl(connectorMock);
        List<Course> exceptStudentCourses = new ArrayList<>();
        List<Course> actualStudentCourses = null;
        Student otherStudent = new Student("OtherFirstName", "OtherLastName", 1);
        otherStudent.setId(2);
        String sqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName_1', 'Description_1'),
                       ('CourseName_2', 'Description_2'),
                       ('CourseName_3', 'Description_3');

                INSERT INTO students_courses (student_id, course_id)
                VALUES (1, 1),
                       (1, 2),
                       (1, 3);
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            actualStudentCourses = courseDao.findCoursesForStudent(otherStudent);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(exceptStudentCourses, actualStudentCourses);
    }

    @Test
    void findCoursesForStudent_shouldOneCourseInCoursesList_whenStudentIsRegisteredOnlyOnOneCourse() {
        courseDao = new CourseDaoImpl(connectorMock);
        List<Course> exceptStudentCourses = new ArrayList<>();
        List<Course> actualStudentCourses = null;
        Student student = new Student("FirstName", "LastName", 1);
        student.setId(1);
        Course course = new Course("CourseName_1", "Description_1");
        course.setId(1);
        exceptStudentCourses.add(course);
        String sqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName_1', 'Description_1'),
                       ('CourseName_2', 'Description_2'),
                       ('CourseName_3', 'Description_3');

                INSERT INTO students_courses (student_id, course_id)
                VALUES (1, 1);
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            actualStudentCourses = courseDao.findCoursesForStudent(student);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(exceptStudentCourses, actualStudentCourses);
    }

    @Test
    void findCoursesForStudent_shouldCoursesListForStudent_whenStudentIsRegisteredForTheseCourses() {
        courseDao = new CourseDaoImpl(connectorMock);
        List<Course> exceptStudentCourses = new ArrayList<>();
        List<Course> actualStudentCourses = null;
        Student student = new Student("FirstName", "LastName", 1);
        student.setId(1);
        String sqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName_1', 'Description_1'),
                       ('CourseName_2', 'Description_2'),
                       ('CourseName_3', 'Description_3');

                INSERT INTO students_courses (student_id, course_id)
                VALUES (1, 1),
                       (1, 2),
                       (1, 3);
                """;

        for (int i = 1; i < 4; i++) {
            Course course = new Course();
            course.setId(i);
            course.setCourseName("CourseName_" + i);
            course.setDescription("Description_" + i);
            exceptStudentCourses.add(course);
        }

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            actualStudentCourses = courseDao.findCoursesForStudent(student);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(exceptStudentCourses, actualStudentCourses);
    }

    @AfterEach
    void tearDown() {
        String sqlScript = """
                ALTER TABLE students ALTER COLUMN student_id RESTART WITH 1;
                DELETE FROM students;
                ALTER TABLE groups ALTER COLUMN group_id RESTART WITH 1;
                DELETE FROM groups;
                ALTER TABLE courses ALTER COLUMN course_id RESTART WITH 1;
                DELETE FROM courses;
                ALTER TABLE students_courses ALTER COLUMN student_courses_id RESTART WITH 1;
                DELETE FROM students_courses;
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
                DROP TABLE IF EXISTS groups CASCADE;
                DROP TABLE IF EXISTS students CASCADE;
                DROP TABLE IF EXISTS courses CASCADE;
                DROP TABLE IF EXISTS students_courses CASCADE;
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
