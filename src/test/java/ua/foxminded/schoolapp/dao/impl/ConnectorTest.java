package ua.foxminded.schoolapp.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolapp.dao.Connectable;
import org.h2.jdbc.JdbcSQLInvalidAuthorizationSpecException;

class ConnectorTest {

    final static String URL = "jdbc:h2:~/test;MODE=PostgreSQL";
    final static String USER = "sa";
    final static String PASSWORD = "1234";

    Connectable connector;

    @Test
    void connector_shouldNullPointerException_whenProperitesFilePathIsNull() {
        assertThrows(NullPointerException.class, () -> new Connector(null));
    }

    @Test
    void connector_shouldNullPointerException_whenProperitesFileNonExistent() {
        assertThrows(NullPointerException.class, () -> new Connector("failed.properties"));
    }

    @Test
    void connector_shouldBeConnectionToTestDatabase_whenConnectorReadCorrectConnectionDataFromTestPropertiesFile()
            throws Exception {
        connector = new Connector("dbconnectiontest/correct.properties");
        String expectedConnection = null;
        String actualConnection = null;

        expectedConnection = DriverManager.getConnection(URL, USER, PASSWORD).toString().substring(7);
        actualConnection = connector.getConnection().toString().substring(7);

        assertEquals(expectedConnection, actualConnection);
    }

    @Test
    void connector_shouldSQLException_whenConnectorReadDataFromTestPropertiesFileWithFailedURL() throws Exception {
        connector = new Connector("dbconnectiontest/withfailedurl.properties");

        assertThrows(SQLException.class, () -> connector.getConnection());
    }

    @Test
    void connector_shouldJdbcSQLInvalidAuthorizationSpecException_whenConnectorReadDataFromTestPropertiesFileWithFailedUser()
            throws Exception {
        connector = new Connector("dbconnectiontest/withfaileduser.properties");

        assertThrows(JdbcSQLInvalidAuthorizationSpecException.class, () -> connector.getConnection());
    }

    @Test
    void connector_shouldJdbcSQLInvalidAuthorizationSpecException_whenConnectorReadDataFromTestPropertiesFileWithFailedPassword()
            throws Exception {
        connector = new Connector("dbconnectiontest/withfailedpassword.properties");

        assertThrows(JdbcSQLInvalidAuthorizationSpecException.class, () -> connector.getConnection());
    }

    @Test
    void connector_shouldSQLException_whenConnectorReadEmptyTestPropertiesFile() throws Exception {
        connector = new Connector("dbconnectiontest/empty.properties");

        assertThrows(java.sql.SQLException.class, () -> connector.getConnection());
    }
}
