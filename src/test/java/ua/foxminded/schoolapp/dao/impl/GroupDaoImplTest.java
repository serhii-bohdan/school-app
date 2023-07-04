package ua.foxminded.schoolapp.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.GroupDao;
import ua.foxminded.schoolapp.exception.DaoException;
import ua.foxminded.schoolapp.model.*;

class GroupDaoImplTest {

    final static String URL = "jdbc:h2:~/test;MODE=PostgreSQL";
    final static String USER = "sa";
    final static String PASSWORD = "1234";

    Connectable connectorMock;
    GroupDao groupDao;

    @BeforeAll
    static void setUpBeforeClass() {
        String sqlScript = """
                CREATE TABLE IF NOT EXISTS groups (
                  group_id SERIAL PRIMARY KEY,
                  group_name VARCHAR(5) NOT NULL
                );

                CREATE TABLE IF NOT EXISTS students (
                  student_id SERIAL PRIMARY KEY,
                  first_name VARCHAR(25) NOT NULL,
                  last_name VARCHAR(25) NOT NULL,
                  group_id INTEGER REFERENCES groups(group_id)
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
    void groupdaoimpl_shouldNullPointerException_whenConnectorIsNull() {
        assertThrows(NullPointerException.class, () -> new GroupDaoImpl(null));
    }

    @Test
    void save_shouldNullPointerException_whenGroupIsNull() {
        groupDao = new GroupDaoImpl(connectorMock);

        try {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(NullPointerException.class, () -> groupDao.save(null));
    }

    @Test
    void save_shouldDAOException_whenGroupNameNotInitialized() {
        groupDao = new GroupDaoImpl(connectorMock);
        Group group = new Group();

        try {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> groupDao.save(group));
    }

    @Test
    void save_shouldDAOException_whenGroupNameContainsMoreThanFiveCharacters() {
        String expectedGroupName = "LKJ-576";
        Group group = new Group(expectedGroupName);
        groupDao = new GroupDaoImpl(connectorMock);

        try {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> groupDao.save(group));
    }

    @Test
    void save_shouldSavedGroupInTestTable_whenGroupNameContainsLessThanFiveCharacters() {
        int expectedGroupId = 1;
        String expectedGroupName = "AQ-";
        int actualGroupId = 0;
        String actualGroupName = "";
        Group group = new Group(expectedGroupName);
        groupDao = new GroupDaoImpl(connectorMock);

        try (Connection connection = getTestConnection()) {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            groupDao.save(group);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("""
                    SELECT group_id, group_name
                    FROM groups
                    WHERE group_name = 'AQ-';
                    """);

            while (resultSet.next()) {
                actualGroupId = resultSet.getInt("group_id");
                actualGroupName = resultSet.getString("group_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedGroupName, actualGroupName);
        assertEquals(expectedGroupId, actualGroupId);
    }

    @Test
    void save_shouldSavedGroupInTestTable_whenGroupNameEmpty() {
        int expectedGroupId = 1;
        String expectedGroupName = "";
        int actualGroupId = 0;
        String actualGroupName = "";
        Group group = new Group(expectedGroupName);
        groupDao = new GroupDaoImpl(connectorMock);

        try (Connection connection = getTestConnection()) {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            groupDao.save(group);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("""
                    SELECT group_id, group_name
                    FROM groups
                    WHERE group_name = '';
                    """);

            while (resultSet.next()) {
                actualGroupId = resultSet.getInt("group_id");
                actualGroupName = resultSet.getString("group_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedGroupName, actualGroupName);
        assertEquals(expectedGroupId, actualGroupId);
    }

    @Test
    void save_shouldSavedGroupInTestTable_whenGroupWithNameThatContainsFiveCharacters() {
        int expectedGroupId = 1;
        String expectedGroupName = "GC-34";
        int actualGroupId = 0;
        String actualGroupName = "";
        Group group = new Group(expectedGroupName);
        groupDao = new GroupDaoImpl(connectorMock);

        try (Connection connection = getTestConnection()) {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            groupDao.save(group);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("""
                    SELECT group_id, group_name
                    FROM groups
                    WHERE group_name = 'GC-34';
                    """);

            while (resultSet.next()) {
                actualGroupId = resultSet.getInt("group_id");
                actualGroupName = resultSet.getString("group_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedGroupName, actualGroupName);
        assertEquals(expectedGroupId, actualGroupId);
    }

    @Test
    void save_shouldOneRecordInGroupsTestTable_whenGroupWithNameThatContainsFiveCharacters() {
        int expectedRecordsNumber = 1;
        String expectedGroupName = "GC-34";
        Group group = new Group(expectedGroupName);
        groupDao = new GroupDaoImpl(connectorMock);

        try {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int actualRecordsNumber = groupDao.save(group);

        assertEquals(expectedRecordsNumber, actualRecordsNumber);
    }

    @Test
    void findGroupsWithGivenNumberStudents_shouldDAOException_whenConnectorThrowSQLException() {
        groupDao = new GroupDaoImpl(connectorMock);

        try {
            when(connectorMock.getConnection()).thenThrow(SQLException.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> groupDao.findGroupsWithGivenNumberStudents(4));

    }

    @Test
    void findGroupsWithGivenNumberStudents_shouldEmptyGroupsList_whenNegativeNumberOfStudents() {
        List<Group> groups = new ArrayList<>();
        String sqlScript = """
                INSERT INTO groups (group_name)
                VALUES('XZ-21'),
                      ('DF-86'),
                      ('PO-37');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 2),
                       ('FirstName', 'LastName', 2),
                       ('FirstName', 'LastName', 2),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3);
                """;
        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            groupDao = new GroupDaoImpl(connectorMock);
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            groups = groupDao.findGroupsWithGivenNumberStudents(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(groups.isEmpty());
    }

    @Test
    void findGroupsWithGivenNumberStudents_shouldEmptyGroupsList_whenNumberOfStudentsIsZero() {
        List<Group> groups = new ArrayList<>();
        String sqlScript = """
                INSERT INTO groups (group_name)
                VALUES('XZ-21'),
                      ('DF-86'),
                      ('PO-37');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 2),
                       ('FirstName', 'LastName', 2),
                       ('FirstName', 'LastName', 2),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3);
                """;
        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            groupDao = new GroupDaoImpl(connectorMock);
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            groups = groupDao.findGroupsWithGivenNumberStudents(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(groups.isEmpty());
    }

    @Test
    void findGroupsWithGivenNumberStudents_shouldEmptyGroupsList_whenThereNoGroupWithGivenNumberOfStudents() {
        List<Group> groups = new ArrayList<>();
        String sqlScript = """
                INSERT INTO groups (group_name)
                VALUES('XZ-21'),
                      ('DF-86'),
                      ('PO-37');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 2),
                       ('FirstName', 'LastName', 2),
                       ('FirstName', 'LastName', 2),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3);
                """;
        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            groupDao = new GroupDaoImpl(connectorMock);
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            groups = groupDao.findGroupsWithGivenNumberStudents(2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(groups.isEmpty());
    }

    @Test
    void findGroupsWithGivenNumberStudents_shouldOneGroup_whenOneGroupWithGivenAndSmallerNumberOfStudents() {
        List<Group> groups = new ArrayList<>();
        String sqlScript = """
                INSERT INTO groups (group_name)
                VALUES('XZ-21'),
                      ('DF-86'),
                      ('PO-37');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 2),
                       ('FirstName', 'LastName', 2),
                       ('FirstName', 'LastName', 2),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3);
                """;
        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            groupDao = new GroupDaoImpl(connectorMock);
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            groups = groupDao.findGroupsWithGivenNumberStudents(3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("DF-86", groups.get(0).getGroupName());
    }

    @Test
    void findGroupsWithGivenNumberOfStudents_shouldTwoGroups_whenTwoGroupsWithGivenAndSmallerNumberOfStudents() {
        List<Group> groups = new ArrayList<>();
        String sqlScript = """
                INSERT INTO groups (group_name)
                VALUES('XZ-21'),
                      ('DF-86'),
                      ('PO-37');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 2),
                       ('FirstName', 'LastName', 2),
                       ('FirstName', 'LastName', 2),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3);
                """;
        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            groupDao = new GroupDaoImpl(connectorMock);
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            groups = groupDao.findGroupsWithGivenNumberStudents(4);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("XZ-21", groups.get(0).getGroupName());
        assertEquals("DF-86", groups.get(1).getGroupName());
    }

    @Test
    void findGroupsWithGivenNumberStudents_shouldAllExistingGroups_whenGivenNumberOfStudentsIsMuchLargerThanWhatInEachOfAvailableGroups() {
        List<Group> groups = new ArrayList<>();
        String sqlScript = """
                INSERT INTO groups (group_name)
                VALUES('XZ-21'),
                      ('DF-86'),
                      ('PO-37');

                INSERT INTO students (first_name, last_name, group_id)
                VALUES ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 1),
                       ('FirstName', 'LastName', 2),
                       ('FirstName', 'LastName', 2),
                       ('FirstName', 'LastName', 2),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3),
                       ('FirstName', 'LastName', 3);
                """;
        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            groupDao = new GroupDaoImpl(connectorMock);
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            groups = groupDao.findGroupsWithGivenNumberStudents(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("PO-37", groups.get(0).getGroupName());
        assertEquals("XZ-21", groups.get(1).getGroupName());
        assertEquals("DF-86", groups.get(2).getGroupName());
    }

    @AfterEach
    void tearDown() {
        String sqlScript = """
                ALTER TABLE students ALTER COLUMN student_id RESTART WITH 1;
                DELETE FROM students;
                ALTER TABLE groups ALTER COLUMN group_id RESTART WITH 1;
                DELETE FROM groups;
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
