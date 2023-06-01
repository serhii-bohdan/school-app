package ua.foxminded.schoolapp.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.ExecutorDao;
import ua.foxminded.schoolapp.exception.DaoException;

class SqlScriptsExecutorDaoTest {

    final static String URL = "jdbc:h2:~/test;MODE=PostgreSQL";
    final static String USER = "sa";
    final static String PASSWORD = "1234";

    ExecutorDao executor;
    Connectable connector;

    @BeforeEach
    void setUp() {
        connector = mock(Connectable.class);
    }

    @Test
    void sqlscriptsexecutordao_shouldNullPointerException_whenConnectorIsNull() {
        assertThrows(NullPointerException.class, () -> executor = new SqlScriptsExecutorDao(null));
    }

    @Test
    void sqlscriptsexecutordao_shouldNullPointerException_whenSQLScriptIsNull() {
        try {
            when(connector.getConnection()).thenReturn(getTestConnection());
            assertThrows(NullPointerException.class, () -> executor.executeSqlScript(null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void sqlscriptsexecutordao_shouldExecuteSQLScript_whenSQLScriptIsCorrect() {
        String actualGroupName = null;
        int actualGroupId = 0;
        String sqlScript = """
                CREATE TABLE IF NOT EXISTS groups (
                  group_id SERIAL PRIMARY KEY,
                  group_name VARCHAR(5) NOT NULL
                );

                INSERT INTO groups (group_name)
                VALUES ('LK-61');
                """;

        try (Connection connection = getTestConnection()) {
            executor = new SqlScriptsExecutorDao(connector);
            when(connector.getConnection()).thenReturn(getTestConnection());
            executor.executeSqlScript(sqlScript);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("""
                    SELECT group_id, group_name
                    FROM groups
                    WHERE group_name = 'LK-61';
                    """);
            while (resultSet.next()) {
                actualGroupName = resultSet.getString("group_name");
                actualGroupId = resultSet.getInt("group_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("LK-61", actualGroupName);
        assertEquals(1, actualGroupId);
    }

    @Test
    void sqlscriptsexecutordao_shouldDAOException_whenSQLScriptNotCorrect() {
        String sqlScript = """
                CRE TABLE IF NOT EXISTS groups (
                  group_id SERIAL PRIMARY KEY,
                  group_name VARCHAR(5) NOT NULL
                );
                """;

        try (Connection connection = getTestConnection()) {
            executor = new SqlScriptsExecutorDao(connector);
            when(connector.getConnection()).thenReturn(getTestConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(DaoException.class, () -> executor.executeSqlScript(sqlScript));
    }

    @AfterEach
    void tearDown() {
        String sqlScript = """
                DROP TABLE IF EXISTS groups CASCADE;
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
