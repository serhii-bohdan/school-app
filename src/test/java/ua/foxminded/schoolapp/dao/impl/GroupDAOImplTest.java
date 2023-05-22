package ua.foxminded.schoolapp.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.sql.Statement;
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
import ua.foxminded.schoolapp.dao.GroupDAO;
import ua.foxminded.schoolapp.exception.DAOException;
import ua.foxminded.schoolapp.model.*;

class GroupDAOImplTest {

    final static String URL = "jdbc:h2:~/test;MODE=PostgreSQL";
    final static String USER = "sa";
    final static String PASSWORD = "1234";

    Connectable connectorMock;
    GroupDAO groupDao;

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
                );""";

        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);
        } catch (Exception e) {
            throw new DAOException("An error occurred while obtaining a connection to the test database.");
        }
    }

    @BeforeEach
    void setUp() {
        connectorMock = mock(Connectable.class);
    }

    @Test
    void save_shouldNullPointerException_whenConnectorIsNull() {
        assertThrows(NullPointerException.class, () -> {
            groupDao = new GroupDAOImpl(null);
        });
    }

    @Test
    void save_shouldNullPointerException_whenGroupIsNull() {
        groupDao = new GroupDAOImpl(connectorMock);

        try {
            when(connectorMock.getConnection()).thenReturn(getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(NullPointerException.class, () -> {
            groupDao.save(null);
        });
    }

    @Test
    void save_shouldDAOException_whenGroupNameNotInitialized() {
        groupDao = new GroupDAOImpl(connectorMock);
        Group group = new Group();

        try {
            when(connectorMock.getConnection()).thenReturn(getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(DAOException.class, () -> {
            groupDao.save(group);
        });
    }

    @Test
    void save_shouldDAOException_whenGroupNameContainsMoreThanFiveCharacters() {
        String expectedGroupName = "LKJ-576";
        Group group = new Group(expectedGroupName);
        groupDao = new GroupDAOImpl(connectorMock);

        try {
            when(connectorMock.getConnection()).thenReturn(getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(DAOException.class, () -> {
            groupDao.save(group);
        });
    }

    @Test
    void save_shouldSavedGroupInTestTable_whenGroupNameContainsLessThanFiveCharacters() {
        int expectedGroupId = 1;
        String expectedGroupName = "AQ-";
        int actualGroupId = 0;
        String actualGroupName = "";
        Group group = new Group(expectedGroupName);
        groupDao = new GroupDAOImpl(connectorMock);

        try (Connection connection = getConnection()) {
            when(connectorMock.getConnection()).thenReturn(getConnection());
            groupDao.save(group);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("""
                    SELECT group_id, group_name
                    FROM groups
                    WHERE group_name = 'AQ-';""");

            while (resultSet.next()) {
                actualGroupId = resultSet.getInt("group_id");
                actualGroupName = resultSet.getString("group_name");
            }
        } catch (Exception e) {
            throw new DAOException("An error occurred while obtaining a connection to the test database.");
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
        groupDao = new GroupDAOImpl(connectorMock);

        try (Connection connection = getConnection()) {
            when(connectorMock.getConnection()).thenReturn(getConnection());
            groupDao.save(group);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("""
                    SELECT group_id, group_name
                    FROM groups
                    WHERE group_name = '';""");

            while (resultSet.next()) {
                actualGroupId = resultSet.getInt("group_id");
                actualGroupName = resultSet.getString("group_name");
            }
        } catch (Exception e) {
            throw new DAOException("An error occurred while obtaining a connection to the test database.");
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
        groupDao = new GroupDAOImpl(connectorMock);

        try (Connection connection = getConnection()) {
            when(connectorMock.getConnection()).thenReturn(getConnection());
            groupDao.save(group);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("""
                    SELECT group_id, group_name
                    FROM groups
                    WHERE group_name = 'GC-34';""");

            while (resultSet.next()) {
                actualGroupId = resultSet.getInt("group_id");
                actualGroupName = resultSet.getString("group_name");
            }
        } catch (Exception e) {
            throw new DAOException("An error occurred while obtaining a connection to the test database.");
        }

        assertEquals(expectedGroupName, actualGroupName);
        assertEquals(expectedGroupId, actualGroupId);
    }

    @Test
    void save_shouldOneRecordInGroupsTestTable_whenGroupWithNameThatContainsFiveCharacters() {
        int expectedRecordsNumber = 1;
        String expectedGroupName = "GC-34";
        Group group = new Group(expectedGroupName);
        groupDao = new GroupDAOImpl(connectorMock);

        try {
            when(connectorMock.getConnection()).thenReturn(getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int actualRecordsNumber = groupDao.save(group);

        assertEquals(expectedRecordsNumber, actualRecordsNumber);
    }

    @AfterEach
    void tearDown() {
        String sqlScript = """
                ALTER TABLE students ALTER COLUMN student_id RESTART WITH 1;
                DELETE FROM students;
                ALTER TABLE groups ALTER COLUMN group_id RESTART WITH 1;
                DELETE FROM groups;""";

        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);
        } catch (Exception e) {
            throw new DAOException("An error occurred while obtaining a connection to the test database.");
        }
    }

    @AfterAll
    static void tearDownAfterClass() {
        String sqlScript = """
                DROP TABLE IF EXISTS groups CASCADE;
                DROP TABLE IF EXISTS students CASCADE;""";
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);
        } catch (Exception e) {
            throw new DAOException("An error occurred while obtaining a connection to the test database.");
        }
    }

    private static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
