package ua.foxminded.schoolapp.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
    void save_shouldDaoException_whenGroupNameNotInitialized() {
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
    void save_shouldDaoException_whenGroupNameContainsMoreThanFiveCharacters() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        int actualRecordsNumber = groupDao.save(group);

        assertEquals(expectedRecordsNumber, actualRecordsNumber);
    }

    @Test
    void findAll_shouldDaoException_whenConnectorThrowSQLException() {
        groupDao = new GroupDaoImpl(connectorMock);

        try {
            when(connectorMock.getConnection()).thenThrow(SQLException.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> groupDao.findAll());
    }

    @Test
    void findAll_shouldEmptyGroupsList_whenGroupsTableEmpty() {
        groupDao = new GroupDaoImpl(connectorMock);
        List<Group> exceptAllAvailableGroups = new ArrayList<>();
        List<Group> actualAllAvailableGroups = null;

        try {
            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            actualAllAvailableGroups = groupDao.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(exceptAllAvailableGroups, actualAllAvailableGroups);
    }

    @Test
    void findAll_shouldAllAvailableGroups_whenGroupsTableContainGroups() {
        groupDao = new GroupDaoImpl(connectorMock);
        List<Group> exceptAllAvailableGroups = new ArrayList<>();
        List<Group> actualAllAvailableGroups = null;
        String sqlScript = """
                INSERT INTO groups (group_name)
                VALUES ('GH-10'),
                       ('GH-10'),
                       ('GH-10');
                """;

        for (int i = 1; i < 4; i++) {
            Group group = new Group("GH-10");
            group.setId(i);
            exceptAllAvailableGroups.add(group);
        }

        try (Connection connection = getTestConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);

            when(connectorMock.getConnection()).thenReturn(getTestConnection());
            actualAllAvailableGroups = groupDao.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(exceptAllAvailableGroups, actualAllAvailableGroups);
    }

    @Test
    void findGroupsWithGivenNumberStudents_shouldDaoException_whenConnectorThrowSQLException() {
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
        Map<Group, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
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
            groupsWithTheirNumberOfStudents = groupDao.findGroupsWithGivenNumberStudents(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(groupsWithTheirNumberOfStudents.isEmpty());
    }

    @Test
    void findGroupsWithGivenNumberStudents_shouldEmptyGroupsList_whenNumberOfStudentsIsZero() {
        Map<Group, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
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
            groupsWithTheirNumberOfStudents = groupDao.findGroupsWithGivenNumberStudents(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(groupsWithTheirNumberOfStudents.isEmpty());
    }

    @Test
    void findGroupsWithGivenNumberStudents_shouldEmptyGroupsList_whenThereNoGroupWithGivenNumberOfStudents() {
        Map<Group, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
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
            groupsWithTheirNumberOfStudents = groupDao.findGroupsWithGivenNumberStudents(2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(groupsWithTheirNumberOfStudents.isEmpty());
    }

    @Test
    void findGroupsWithGivenNumberStudents_shouldOneGroup_whenOneGroupWithGivenAndSmallerNumberOfStudents() {
        Map<Group, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
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
            groupsWithTheirNumberOfStudents = groupDao.findGroupsWithGivenNumberStudents(3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Entry<Group, Integer> groupWithStudentsNumber = groupsWithTheirNumberOfStudents.entrySet().iterator().next();
        assertEquals("DF-86", groupWithStudentsNumber.getKey().getGroupName());
        assertEquals(3, groupWithStudentsNumber.getValue());
    }

    @Test
    void findGroupsWithGivenNumberOfStudents_shouldTwoGroups_whenTwoGroupsWithGivenAndSmallerNumberOfStudents() {
        Map<Group, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
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
            groupsWithTheirNumberOfStudents = groupDao.findGroupsWithGivenNumberStudents(4);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Group firstGroup = new Group("XZ-21");
        Group secondGroup = new Group("DF-86");
        firstGroup.setId(1);
        secondGroup.setId(2);
        assertTrue(groupsWithTheirNumberOfStudents.containsKey(firstGroup));
        assertTrue(groupsWithTheirNumberOfStudents.containsKey(secondGroup));
        assertTrue(groupsWithTheirNumberOfStudents.containsValue(4));
        assertTrue(groupsWithTheirNumberOfStudents.containsValue(3));
    }

    @Test
    void findGroupsWithGivenNumberStudents_shouldAllExistingGroups_whenGivenNumberOfStudentsIsMuchLargerThanWhatInEachOfAvailableGroups() {
        Map<Group, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();
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
            groupsWithTheirNumberOfStudents = groupDao.findGroupsWithGivenNumberStudents(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Group firstGroup = new Group("XZ-21");
        Group secondGroup = new Group("DF-86");
        Group thirdGroup = new Group("PO-37");
        firstGroup.setId(1);
        secondGroup.setId(2);
        thirdGroup.setId(3);
        assertTrue(groupsWithTheirNumberOfStudents.containsKey(firstGroup));
        assertTrue(groupsWithTheirNumberOfStudents.containsKey(secondGroup));
        assertTrue(groupsWithTheirNumberOfStudents.containsKey(thirdGroup));
        assertTrue(groupsWithTheirNumberOfStudents.containsValue(4));
        assertTrue(groupsWithTheirNumberOfStudents.containsValue(3));
        assertTrue(groupsWithTheirNumberOfStudents.containsValue(5));
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
