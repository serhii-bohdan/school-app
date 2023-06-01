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
import ua.foxminded.schoolapp.dao.StudentDao;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.exception.DaoException;

class StudentDaoImplTest {

    final static String URL = "jdbc:h2:~/test;MODE=PostgreSQL";
    final static String USER = "sa";
    final static String PASSWORD = "1234";

    Connectable connectorMock;
    StudentDao studentDao;

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
    void studentDaoImpl_shouldNullPointerException_whenConnectorIsNull() {
        assertThrows(NullPointerException.class, () -> new StudentDaoImpl(null));
    }

    @Test
    void save_shouldNullPointerException_whenStudentIsNull() {
        studentDao = new StudentDaoImpl(connectorMock);

        try {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(NullPointerException.class, () -> studentDao.save(null));
    }

    @Test
    void save_shouldDAOException_whenStudentFieldsNotInitialized() {
        studentDao = new StudentDaoImpl(connectorMock);
        Student student = new Student();

        try {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> studentDao.save(student));
    }

    @Test
    void save_shouldDAOException_whenNoGroupWithSpecifiedGroupIdInGroupsTable() {
        studentDao = new StudentDaoImpl(connectorMock);
        Student student = new Student("FirstName", "LastName", 2);
        String fillingGroupsTableSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingGroupsTableSqlScript);
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> studentDao.save(student));
    }

    @Test
    void save_shouldNewRecordInStudentsTable_whenStudentFirstNameAndLastNameIsEmpty() {
        studentDao = new StudentDaoImpl(connectorMock);
        Student student = new Student("", "", 1);
        String fillingGroupsTableSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');
                """;
        String selectDataFromStudentsTableScript = """
                SELECT student_id, first_name, last_name, group_id
                FROM students
                WHERE student_id = 1 AND first_name = '' AND last_name = ''
                AND group_id = 1;
                """;
        int actualStudentId = 0;
        String actualFirstName = null;
        String actualLastName = null;
        int actualGroupId = 0;
        int recordNumber = 0;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingGroupsTableSqlScript);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection connection = getTestConnection()) {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            recordNumber = studentDao.save(student);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectDataFromStudentsTableScript);

            while (resultSet.next()) {
                actualStudentId = resultSet.getInt("student_id");
                actualFirstName = resultSet.getString("first_name");
                actualLastName = resultSet.getString("last_name");
                actualGroupId = resultSet.getInt("group_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(1, actualStudentId);
        assertEquals("", actualFirstName);
        assertEquals("", actualLastName);
        assertEquals(1, actualGroupId);
        assertEquals(1, recordNumber);
    }

    @Test
    void save_shouldNewRecordInStudentsTable_whenStudentFirstNameAndLastNameOnlySpaces() {
        studentDao = new StudentDaoImpl(connectorMock);
        Student student = new Student("    ", "    ", 1);
        String fillingGroupsTableSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');
                """;
        String selectDataFromStudentsTableScript = """
                SELECT student_id, first_name, last_name, group_id
                FROM students
                WHERE student_id = 1 AND first_name = '    ' AND last_name = '    '
                AND group_id = 1;
                """;
        int actualStudentId = 0;
        String actualFirstName = null;
        String actualLastName = null;
        int actualGroupId = 0;
        int recordNumber = 0;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingGroupsTableSqlScript);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection connection = getTestConnection()) {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            recordNumber = studentDao.save(student);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectDataFromStudentsTableScript);

            while (resultSet.next()) {
                actualStudentId = resultSet.getInt("student_id");
                actualFirstName = resultSet.getString("first_name");
                actualLastName = resultSet.getString("last_name");
                actualGroupId = resultSet.getInt("group_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(1, actualStudentId);
        assertEquals("    ", actualFirstName);
        assertEquals("    ", actualLastName);
        assertEquals(1, actualGroupId);
        assertEquals(1, recordNumber);
    }

    @Test
    void save_shouldNewRecordInStudentsTable_whenStudentFieldsIsCorrect() {
        studentDao = new StudentDaoImpl(connectorMock);
        Student student = new Student("FirstName", "LastName", 1);
        String fillingGroupsTableSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');
                """;
        String selectDataFromStudentsTableScript = """
                SELECT student_id, first_name, last_name, group_id
                FROM students
                WHERE student_id = 1 AND first_name = 'FirstName' AND last_name = 'LastName'
                AND group_id = 1;
                """;
        int actualStudentId = 0;
        String actualFirstName = null;
        String actualLastName = null;
        int actualGroupId = 0;
        int recordNumber = 0;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingGroupsTableSqlScript);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection connection = getTestConnection()) {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            recordNumber = studentDao.save(student);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectDataFromStudentsTableScript);

            while (resultSet.next()) {
                actualStudentId = resultSet.getInt("student_id");
                actualFirstName = resultSet.getString("first_name");
                actualLastName = resultSet.getString("last_name");
                actualGroupId = resultSet.getInt("group_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(1, actualStudentId);
        assertEquals("FirstName", actualFirstName);
        assertEquals("LastName", actualLastName);
        assertEquals(1, actualGroupId);
        assertEquals(1, recordNumber);
    }

    @Test
    void findAllStudents_shouldDAOException_whenThrownSQLException() {
        studentDao = new StudentDaoImpl(connectorMock);

        try {
            when(connectorMock.getConnection()).thenThrow(SQLException.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> studentDao.findAllStudents());
    }

    @Test
    void findAllStudents_shouldStudentsList_whenStudentsTableContainsStudnets() {
        studentDao = new StudentDaoImpl(connectorMock);
        List<Student> exceptAllAvailableStudnts = new ArrayList<>();
        List<Student> actualAllAvailableStudnts = new ArrayList<>();
        String fillingGroupsAndStudentsTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90'),
                       ('AL-31'),
                       ('NO-24');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 2),
                       ('FirstName', 'LastName', 3);
                """;

        for (int i = 1; i <= 3; i++) {
            Student student = new Student("FirstName", "LastName", i);
            student.setId(i);
            exceptAllAvailableStudnts.add(student);
        }

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingGroupsAndStudentsTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            actualAllAvailableStudnts = studentDao.findAllStudents();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(exceptAllAvailableStudnts, actualAllAvailableStudnts);
    }

    @Test
    void findAllStudents_shouldEmptyStudentsList_whenStudentsTableEmpty() {
        studentDao = new StudentDaoImpl(connectorMock);
        List<Student> exceptAllAvailableStudnts = new ArrayList<>();
        List<Student> actualAllAvailableStudnts = null;

        try {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            actualAllAvailableStudnts = studentDao.findAllStudents();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(exceptAllAvailableStudnts, actualAllAvailableStudnts);
    }

    @Test
    void findStudentById_shouldDAOException_whenThrownSQLException() {
        studentDao = new StudentDaoImpl(connectorMock);

        try (Connection connection = getTestConnection()) {
            when(connectorMock.getConnection()).thenThrow(SQLException.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> studentDao.findStudentById(1));
    }

    @Test
    void findStudentById_shouldReturnStudentWithNoInitializedFields_whenGivenStudentIdLessThenZero() {
        studentDao = new StudentDaoImpl(connectorMock);
        Student expectedStudent = new Student();
        Student actualStudent = null;

        try (Connection connection = getTestConnection()) {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            actualStudent = studentDao.findStudentById(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void findStudentById_shouldReturnStudentWithNoInitializedFields_whenGivenStudentIdIsZero() {
        studentDao = new StudentDaoImpl(connectorMock);
        Student expectedStudent = new Student();
        Student actualStudent = null;

        try (Connection connection = getTestConnection()) {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            actualStudent = studentDao.findStudentById(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void findStudentById_shouldReturnStudentWithNoInitializedFields_whenGivenStudentIdMoreThanZeroAndNoStudentWithThisId() {
        studentDao = new StudentDaoImpl(connectorMock);
        Student expectedStudent = new Student();
        Student actualStudent = null;

        String fillingGroupsAndStudentsTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingGroupsAndStudentsTablesSqlScript);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection connection = getTestConnection()) {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            actualStudent = studentDao.findStudentById(2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void findStudentById_shouldReturnedStudentWithInitializedFields_whenStudentExistsWithGivenId() {
        studentDao = new StudentDaoImpl(connectorMock);
        Student expectedStudent = new Student("FirstName", "LastName", 1);
        expectedStudent.setId(1);
        Student actualStudent = null;
        String fillingGroupsAndStudentsTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingGroupsAndStudentsTablesSqlScript);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection connection = getTestConnection()) {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            actualStudent = studentDao.findStudentById(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void findStudentsRelatedToCourse_shouldDAOException_whenThrownSQLException() {
        studentDao = new StudentDaoImpl(connectorMock);

        try (Connection connection = getTestConnection()) {
            when(connectorMock.getConnection()).thenThrow(SQLException.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> studentDao.findStudentsRelatedToCourse("CourseName"));
    }

    @Test
    void findStudentsRelatedToCourse_shouldEmptyStudentsList_whenGivenCourseNameIsNull() {
        studentDao = new StudentDaoImpl(connectorMock);
        List<Student> expectedStudentsOnCourse = new ArrayList<>();
        List<Student> actualStudentsOnCourse = null;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');

                INSERT INTO students_courses (student_id, course_id)
                VALUES (1, 1),
                       (2, 1),
                       (3, 1);
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            actualStudentsOnCourse = studentDao.findStudentsRelatedToCourse(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedStudentsOnCourse, actualStudentsOnCourse);
    }

    @Test
    void findStudentsRelatedToCourse_shouldEmptyStudentsList_whenNoCourseWithGivenName() {
        studentDao = new StudentDaoImpl(connectorMock);
        List<Student> expectedStudentsOnCourse = new ArrayList<>();
        List<Student> actualStudentsOnCourse = null;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');

                INSERT INTO students_courses (student_id, course_id)
                VALUES (1, 1),
                       (2, 1),
                       (3, 1);
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            actualStudentsOnCourse = studentDao.findStudentsRelatedToCourse("NonExistentCourse");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedStudentsOnCourse, actualStudentsOnCourse);
    }

    @Test
    void findStudentsRelatedToCourse_shouldEmptyStudentsList_whenNoStudentOnCourse() {
        studentDao = new StudentDaoImpl(connectorMock);
        List<Student> expectedStudentsOnCourse = new ArrayList<>();
        List<Student> actualStudentsOnCourse = null;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            actualStudentsOnCourse = studentDao.findStudentsRelatedToCourse("CourseName");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedStudentsOnCourse, actualStudentsOnCourse);
    }

    @Test
    void findStudentsRelatedToCourse_shouldListOfStudentsInCourseWithGivenName_whenStudentsRegisteredForCourse() {
        studentDao = new StudentDaoImpl(connectorMock);
        List<Student> expectedStudentsOnCourse = new ArrayList<>();
        List<Student> actualStudentsOnCourse = null;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');

                INSERT INTO students_courses (student_id, course_id)
                VALUES (1, 1),
                       (2, 1),
                       (3, 1);
                """;

        for (int i = 1; i <= 3; i++) {
            Student student = new Student("FirstName", "LastName", 1);
            student.setId(i);
            expectedStudentsOnCourse.add(student);
        }

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            actualStudentsOnCourse = studentDao.findStudentsRelatedToCourse("CourseName");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedStudentsOnCourse, actualStudentsOnCourse);
    }

    @Test
    void deleteStudentById_shouldDAOException_whenThrownSQLException() {
        studentDao = new StudentDaoImpl(connectorMock);

        try (Connection connection = getTestConnection()) {
            when(connectorMock.getConnection()).thenThrow(SQLException.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> studentDao.deleteStudentById(1));
    }

    @Test
    void deleteStudentById_shouldDeletedStudent_whenStudentWithGivenIdExists() {
        studentDao = new StudentDaoImpl(connectorMock);
        int studentId = 1;
        int rowsDeleted = 0;
        String actualFirstNameAfterDeleting = null;
        String actualLastNameAfterDeleting = null;
        int actualGroupIdAfterDeleting = 0;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);
                """;
        String selectingTestDataSqlScript = """
                SELECT student_id, first_name, last_name, group_id
                FROM students
                WHERE student_id = 1;
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            rowsDeleted = studentDao.deleteStudentById(studentId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectingTestDataSqlScript);

            while (resultSet.next()) {
                actualFirstNameAfterDeleting = resultSet.getString("first_name");
                actualLastNameAfterDeleting = resultSet.getString("last_name");
                actualGroupIdAfterDeleting = resultSet.getInt("group_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(1, rowsDeleted);
        assertEquals(null, actualFirstNameAfterDeleting);
        assertEquals(null, actualLastNameAfterDeleting);
        assertEquals(0, actualGroupIdAfterDeleting);
    }

    @Test
    void deleteStudentById_shouldNothingDeleted_whenStudentWithGivenIdNotExists() {
        studentDao = new StudentDaoImpl(connectorMock);
        int studentId = 2;
        int rowsDeleted = 0;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            rowsDeleted = studentDao.deleteStudentById(studentId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, rowsDeleted);
    }

    @Test
    void deleteStudentById_shouldNothingDeleted_whenStudentWithGivenIdNotExistsAndIdIsZero() {
        studentDao = new StudentDaoImpl(connectorMock);
        int studentId = 0;
        int rowsDeleted = 0;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            rowsDeleted = studentDao.deleteStudentById(studentId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, rowsDeleted);
    }

    @Test
    void deleteStudentById_shouldNothingDeleted_whenStudentWithGivenIdNotExistsAndIdLessThenZero() {
        studentDao = new StudentDaoImpl(connectorMock);
        int studentId = -1;
        int rowsDeleted = 0;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            rowsDeleted = studentDao.deleteStudentById(studentId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, rowsDeleted);
    }

    @Test
    void isStudentOnCourse_shouldDAOException_whenThrownSQLException() {
        studentDao = new StudentDaoImpl(connectorMock);

        try (Connection connection = getTestConnection()) {
            when(connectorMock.getConnection()).thenThrow(SQLException.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> studentDao.isStudentOnCourse("FirstName", "LsatName", "courseName"));
    }

    @Test
    void isStudentOnCourse_shouldTrue_whenStudentOnCourse() {
        studentDao = new StudentDaoImpl(connectorMock);
        boolean studentOnCourse = false;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');

                INSERT INTO students_courses (student_id, course_id)
                VALUES (1, 1);
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            studentOnCourse = studentDao.isStudentOnCourse("FirstName", "LastName", "CourseName");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(studentOnCourse);
    }

    @Test
    void isStudentOnCourse_shouldFalse_whenStudentNotOnCourse() {
        studentDao = new StudentDaoImpl(connectorMock);
        boolean studentOnCourse = true;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            studentOnCourse = studentDao.isStudentOnCourse("FirstName", "LastName", "CourseName");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertFalse(studentOnCourse);
    }

    @Test
    void isStudentOnCourse_shouldFalse_whenNoStudentWithGivenFirstNameAndLastName() {
        studentDao = new StudentDaoImpl(connectorMock);
        boolean studentOnCourse = true;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');

                INSERT INTO students_courses (student_id, course_id)
                VALUES (1, 1);
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            studentOnCourse = studentDao.isStudentOnCourse("NotExists", "NotExists", "CourseName");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertFalse(studentOnCourse);
    }

    @Test
    void isStudentOnCourse_shouldFalse_whenNoCourseWithGivenName() {
        studentDao = new StudentDaoImpl(connectorMock);
        boolean studentOnCourse = true;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');

                INSERT INTO students_courses (student_id, course_id)
                VALUES (1, 1);
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            studentOnCourse = studentDao.isStudentOnCourse("FirstName", "LastName", "NotExists");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertFalse(studentOnCourse);
    }

    @Test
    void isStudentOnCourse_shouldFalse_whenGivenStudentFirstNameIsNull() {
        studentDao = new StudentDaoImpl(connectorMock);
        boolean studentOnCourse = true;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');

                INSERT INTO students_courses (student_id, course_id)
                VALUES (1, 1);
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            studentOnCourse = studentDao.isStudentOnCourse(null, "LastName", "CourseName");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertFalse(studentOnCourse);
    }

    @Test
    void isStudentOnCourse_shouldFalse_whenGivenCourseNameIsNull() {
        studentDao = new StudentDaoImpl(connectorMock);
        boolean studentOnCourse = true;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');

                INSERT INTO students_courses (student_id, course_id)
                VALUES (1, 1);
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            studentOnCourse = studentDao.isStudentOnCourse("FirstName", "LastName", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertFalse(studentOnCourse);
    }

    @Test
    void addStudentToCourse_shouldDAOException_whenThrownSQLException() {
        studentDao = new StudentDaoImpl(connectorMock);

        try (Connection connection = getTestConnection()) {
            when(connectorMock.getConnection()).thenThrow(SQLException.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> studentDao.addStudentToCourse("FirstName", "LastName", "CourseName"));
    }

    @Test
    void addStudentToCourse_shouldAddedStudentToCourse_whenExistsStudentWithGivenFirstNameAndLastNameAndExistCourseWithGivenName() {
        studentDao = new StudentDaoImpl(connectorMock);
        String actudalStudentFirstNameAddedToCourse = null;
        String actudalStudentLastNameAddedToCourse = null;
        String actudalCourseNameToWhichStudentAdded = null;
        int studentsNumberAddedToCourse = 0;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');
                """;
        String selectingTestDataSqlScript = """
                SELECT first_name, last_name, course_name FROM students
                JOIN students_courses ON students.student_id = students_courses.student_id
                JOIN courses ON courses.course_id = students_courses.course_id
                WHERE students.first_name = 'FirstName'
                AND students.last_name = 'LastName'
                AND courses.course_name = 'CourseName';
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            studentsNumberAddedToCourse = studentDao.addStudentToCourse("FirstName", "LastName", "CourseName");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectingTestDataSqlScript);

            while (resultSet.next()) {
                actudalStudentFirstNameAddedToCourse = resultSet.getString("first_name");
                actudalStudentLastNameAddedToCourse = resultSet.getString("last_name");
                actudalCourseNameToWhichStudentAdded = resultSet.getString("course_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("FirstName", actudalStudentFirstNameAddedToCourse);
        assertEquals("LastName", actudalStudentLastNameAddedToCourse);
        assertEquals("CourseName", actudalCourseNameToWhichStudentAdded);
        assertEquals(1, studentsNumberAddedToCourse);
    }

    @Test
    void addStudentToCourse_shouldDAOException_whenNotExistsStudentWithGivenLastName() {
        studentDao = new StudentDaoImpl(connectorMock);
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> studentDao.addStudentToCourse("FirstName", "NotExists", "CourseName"));
    }

    @Test
    void addStudentToCourse_shouldDAOException_whenNotExistsCourseWithGivenName() {
        studentDao = new StudentDaoImpl(connectorMock);
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> studentDao.addStudentToCourse("FirstName", "LastName", "NotExists"));
    }

    @Test
    void addStudentToCourse_shouldDAOException_whenStudentLastNameIsIsNull() {
        studentDao = new StudentDaoImpl(connectorMock);
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> studentDao.addStudentToCourse("FirstName", null, "CourseName"));
    }

    @Test
    void addStudentToCourse_shouldDAOException_whenCourseNameIsNull() {
        studentDao = new StudentDaoImpl(connectorMock);
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> studentDao.addStudentToCourse("FirstName", "LastName", null));
    }

    @Test
    void deleteStudentFromCourse_shouldDAOException_whenThrownSQLException() {
        studentDao = new StudentDaoImpl(connectorMock);

        try (Connection connection = getTestConnection()) {
            when(connectorMock.getConnection()).thenThrow(SQLException.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class,
                () -> studentDao.deleteStudentFromCourse("FirstName", "LastName", "CourseName"));
    }

    @Test
    void deleteStudentFromCourse_shouldDeletedStudentFromCourse_whenStudentWasPreviouslyOnCourse() {
        studentDao = new StudentDaoImpl(connectorMock);
        String actudalStudentFirstNameAfterDeletingFromCourse = "";
        String actudalStudentLastNameAfterDeletingFromCourse = "";
        String actudalCourseNameFromWhichStudentDeleted = "";
        int studentsNumberDeletedFromCourse = 0;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');

                INSERT INTO students_courses (student_id, course_id)
                VALUES (1, 1);
                """;
        String selectingTestDataSqlScript = """
                SELECT first_name, last_name, course_name FROM students
                JOIN students_courses ON students.student_id = students_courses.student_id
                JOIN courses ON courses.course_id = students_courses.course_id
                WHERE students.first_name = 'FirstName'
                AND students.last_name = 'LastName'
                AND courses.course_name = 'CourseName';
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            studentsNumberDeletedFromCourse = studentDao.deleteStudentFromCourse("FirstName", "LastName", "CourseName");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectingTestDataSqlScript);

            while (resultSet.next()) {
                actudalStudentFirstNameAfterDeletingFromCourse = resultSet.getString("first_name");
                actudalStudentLastNameAfterDeletingFromCourse = resultSet.getString("last_name");
                actudalCourseNameFromWhichStudentDeleted = resultSet.getString("course_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("", actudalStudentFirstNameAfterDeletingFromCourse);
        assertEquals("", actudalStudentLastNameAfterDeletingFromCourse);
        assertEquals("", actudalCourseNameFromWhichStudentDeleted);
        assertEquals(1, studentsNumberDeletedFromCourse);
    }

    @Test
    void deleteStudentFromCourse_shouldNothingDeleted_whenNotExistsStudentWithGivenFirstNameOnCourse() {
        studentDao = new StudentDaoImpl(connectorMock);
        int studentsNumberDeletedFromCourse = 0;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            studentsNumberDeletedFromCourse = studentDao.deleteStudentFromCourse("FirstName", "LastName", "CourseName");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, studentsNumberDeletedFromCourse);
    }

    @Test
    void deleteStudentFromCourse_shouldNothingDeleted_whenNoStudentWithGivenFirstName() {
        studentDao = new StudentDaoImpl(connectorMock);
        int studentsNumberDeletedFromCourse = 0;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');

                INSERT INTO students_courses (student_id, course_id)
                VALUES (1, 1);
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            studentsNumberDeletedFromCourse = studentDao.deleteStudentFromCourse("NotExists", "LastName", "CourseName");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, studentsNumberDeletedFromCourse);
    }

    @Test
    void deleteStudentFromCourse_shouldNothingDeleted_whenNoCourseWithGivenName() {
        studentDao = new StudentDaoImpl(connectorMock);
        int studentsNumberDeletedFromCourse = 0;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');

                INSERT INTO students_courses (student_id, course_id)
                VALUES (1, 1);
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            studentsNumberDeletedFromCourse = studentDao.deleteStudentFromCourse("FirstName", "LastName", "NotExists");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, studentsNumberDeletedFromCourse);
    }

    @Test
    void deleteStudentFromCourse_shouldNothingDeleted_whenStudentFirstNameIsNull() {
        studentDao = new StudentDaoImpl(connectorMock);
        int studentsNumberDeletedFromCourse = 0;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');

                INSERT INTO students_courses (student_id, course_id)
                VALUES (1, 1);
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            studentsNumberDeletedFromCourse = studentDao.deleteStudentFromCourse(null, "LastName", "CourseName");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, studentsNumberDeletedFromCourse);
    }

    @Test
    void deleteStudentFromCourse_shouldNothingDeleted_whenCourseNameIsNull() {
        studentDao = new StudentDaoImpl(connectorMock);
        int studentsNumberDeletedFromCourse = 0;
        String fillingTablesSqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('MQ-90');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1);

                INSERT INTO courses (course_name, course_description)
                VALUES ('CourseName', 'Description');

                INSERT INTO students_courses (student_id, course_id)
                VALUES (1, 1);
                """;

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(fillingTablesSqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            studentsNumberDeletedFromCourse = studentDao.deleteStudentFromCourse("FirstName", "LastName", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, studentsNumberDeletedFromCourse);
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
