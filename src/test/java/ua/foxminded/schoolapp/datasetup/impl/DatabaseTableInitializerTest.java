package ua.foxminded.schoolapp.datasetup.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.lang.reflect.Method;
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
import ua.foxminded.schoolapp.dao.impl.Connector;
import ua.foxminded.schoolapp.datasetup.Generatable;
import ua.foxminded.schoolapp.datasetup.GroupsGeneratorTestHelper;
import ua.foxminded.schoolapp.datasetup.Initializable;
import ua.foxminded.schoolapp.datasetup.StudentGeneratorTestHelper;
import ua.foxminded.schoolapp.exception.DaoException;
import ua.foxminded.schoolapp.datasetup.CoursesGeneratorTestHelper;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

class DatabaseTableInitializerTest {

    final static String URL = "jdbc:h2:~/test;MODE=PostgreSQL";
    final static String USER = "sa";
    final static String PASSWORD = "1234";

    Initializable initializer;
    Connectable connectorMock;
    Generatable<Group> groupsGeneratorMock;
    Generatable<Student> studentsGeneratorMock;
    Generatable<Course> coursesGeneratorMock;
    GroupsGeneratorTestHelper groupsGeneratorHelper;
    StudentGeneratorTestHelper studentsGeneratorHelper;
    CoursesGeneratorTestHelper coursesGeneratorHelper;

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

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        connectorMock = new Connector("dbconnectiontest/correct.properties");
        groupsGeneratorMock = mock(Generatable.class);
        studentsGeneratorMock = mock(Generatable.class);
        coursesGeneratorMock = mock(Generatable.class);
        groupsGeneratorHelper = new GroupsGeneratorTestHelper();
        studentsGeneratorHelper = new StudentGeneratorTestHelper();
        coursesGeneratorHelper = new CoursesGeneratorTestHelper();
    }

    @Test
    void databaseTableInitializer_shouldNullPointerException_whenConnectorIsNull() {
        assertThrows(NullPointerException.class, () -> new DatabaseTableInitializer(null, groupsGeneratorMock,
                studentsGeneratorMock, coursesGeneratorMock));
    }

    @Test
    void databaseTableInitializer_shouldNullPointerException_whenGroupsGeneratorIsNull() {
        assertThrows(NullPointerException.class,
                () -> new DatabaseTableInitializer(connectorMock, null, studentsGeneratorMock, coursesGeneratorMock));
    }

    @Test
    void databaseTableInitializer_shouldNullPointerException_whenStudentsGeneratorIsNull() {
        assertThrows(NullPointerException.class,
                () -> new DatabaseTableInitializer(connectorMock, groupsGeneratorMock, null, coursesGeneratorMock));
    }

    @Test
    void databaseTableInitializer_shouldNullPointerException_whenCoursesGeneratorIsNull() {
        assertThrows(NullPointerException.class,
                () -> new DatabaseTableInitializer(connectorMock, groupsGeneratorMock, studentsGeneratorMock, null));
    }

    @Test
    void initialize_shouldDaoException_whenGroupsGeneratorReturnEmptyGroupsList() {
        initializer = new DatabaseTableInitializer(connectorMock, groupsGeneratorMock, studentsGeneratorMock,
                coursesGeneratorMock);
        when(groupsGeneratorMock.toGenerate()).thenReturn(new ArrayList<>());
        when(studentsGeneratorMock.toGenerate()).thenReturn(studentsGeneratorHelper.getTestListOfStudents(10));
        when(coursesGeneratorMock.toGenerate()).thenReturn(coursesGeneratorHelper.getTestListOfCourses(10));

        assertThrows(DaoException.class, () -> initializer.initialize());
    }

    @Test
    void initialize_shouldEmptyStudentsTestTable_whenStudentsGeneratorReturnEmptyStudentsList() {
        initializer = new DatabaseTableInitializer(connectorMock, groupsGeneratorMock, studentsGeneratorMock,
                coursesGeneratorMock);
        int actualStudentsNumberInTestTable = 0;
        when(groupsGeneratorMock.toGenerate()).thenReturn(groupsGeneratorHelper.getTestListOfGroups(10));
        when(studentsGeneratorMock.toGenerate()).thenReturn(new ArrayList<>());
        when(coursesGeneratorMock.toGenerate()).thenReturn(coursesGeneratorHelper.getTestListOfCourses(10));
        String sqlScript = """
                SELECT count(student_id) AS students_number
                FROM students;
                """;

        initializer.initialize();

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlScript);

            while (resultSet.next()) {
                actualStudentsNumberInTestTable = resultSet.getInt("students_number");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, actualStudentsNumberInTestTable);
    }

    @Test
    void initialize_should_whenCoursesGeneratorReturnEmptyCoursesList() {
        initializer = new DatabaseTableInitializer(connectorMock, groupsGeneratorMock, studentsGeneratorMock,
                coursesGeneratorMock);
        when(groupsGeneratorMock.toGenerate()).thenReturn(groupsGeneratorHelper.getTestListOfGroups(10));
        when(studentsGeneratorMock.toGenerate()).thenReturn(studentsGeneratorHelper.getTestListOfStudents(10));
        when(coursesGeneratorMock.toGenerate()).thenReturn(new ArrayList<>());

        assertThrows(DaoException.class, () -> initializer.initialize());
    }

    @Test
    void initialize_shouldSavedTenGroupsInTestTable_whenGroupsGeneratorReturnTenGroups() {
        initializer = new DatabaseTableInitializer(connectorMock, groupsGeneratorMock, studentsGeneratorMock,
                coursesGeneratorMock);
        int actualGroupsNumberInTestTable = 0;
        when(groupsGeneratorMock.toGenerate()).thenReturn(groupsGeneratorHelper.getTestListOfGroups(10));
        when(studentsGeneratorMock.toGenerate()).thenReturn(studentsGeneratorHelper.getTestListOfStudents(10));
        when(coursesGeneratorMock.toGenerate()).thenReturn(coursesGeneratorHelper.getTestListOfCourses(10));
        String sqlScript = """
                SELECT count(group_id) AS groups_number
                FROM groups;
                """;

        initializer.initialize();

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlScript);

            while (resultSet.next()) {
                actualGroupsNumberInTestTable = resultSet.getInt("groups_number");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(10, actualGroupsNumberInTestTable);
    }

    @Test
    void initialize_shouldSavedTenStudentsInTestTable_whenStudentsGeneratorReturnTenStudents() {
        initializer = new DatabaseTableInitializer(connectorMock, groupsGeneratorMock, studentsGeneratorMock,
                coursesGeneratorMock);
        int actualStudentsNumberInTestTable = 0;
        when(groupsGeneratorMock.toGenerate()).thenReturn(groupsGeneratorHelper.getTestListOfGroups(10));
        when(studentsGeneratorMock.toGenerate()).thenReturn(studentsGeneratorHelper.getTestListOfStudents(10));
        when(coursesGeneratorMock.toGenerate()).thenReturn(coursesGeneratorHelper.getTestListOfCourses(10));
        String sqlScript = """
                SELECT count(student_id) AS students_number
                FROM students;
                """;

        initializer.initialize();

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlScript);

            while (resultSet.next()) {
                actualStudentsNumberInTestTable = resultSet.getInt("students_number");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(10, actualStudentsNumberInTestTable);
    }

    @Test
    void initialize_shouldSavedTenCoursesInTestTable_whenCoursesGeneratorReturnTenCourses() {
        initializer = new DatabaseTableInitializer(connectorMock, groupsGeneratorMock, studentsGeneratorMock,
                coursesGeneratorMock);
        int actualCoursesNumberInTestTable = 0;
        when(groupsGeneratorMock.toGenerate()).thenReturn(groupsGeneratorHelper.getTestListOfGroups(10));
        when(studentsGeneratorMock.toGenerate()).thenReturn(studentsGeneratorHelper.getTestListOfStudents(10));
        when(coursesGeneratorMock.toGenerate()).thenReturn(coursesGeneratorHelper.getTestListOfCourses(10));
        String sqlScript = """
                SELECT count(course_id) AS courses_number
                FROM courses;
                """;

        initializer.initialize();

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlScript);

            while (resultSet.next()) {
                actualCoursesNumberInTestTable = resultSet.getInt("courses_number");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(10, actualCoursesNumberInTestTable);
    }

    @Test
    void fillGroupsTable_should_when() throws Exception {
        initializer = new DatabaseTableInitializer(connectorMock, groupsGeneratorMock, studentsGeneratorMock,
                coursesGeneratorMock);
        List<Group> genratedGroups = groupsGeneratorHelper.getTestListOfGroups(10);
        List<Group> actualSavedGroups = new ArrayList<>();
        Method method = DatabaseTableInitializer.class.getDeclaredMethod("fillGroupsTable");
        method.setAccessible(true);
        when(groupsGeneratorMock.toGenerate()).thenReturn(genratedGroups);
        String sqlScript = """
                SELECT group_name
                FROM groups;
                """;

        method.invoke(initializer);

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlScript);

            while (resultSet.next()) {
                actualSavedGroups.add(new Group(resultSet.getString("group_name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(genratedGroups, actualSavedGroups);
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
