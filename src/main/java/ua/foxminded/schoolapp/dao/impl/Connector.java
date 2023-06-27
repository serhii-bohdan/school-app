package ua.foxminded.schoolapp.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import ua.foxminded.schoolapp.dao.Connectable;

/**
 * The Connector class is responsible for establishing a database connection
 * based on the provided configuration file. It implements the
 * {@link Connectable} interface.
 *
 * @author Serhii Bohdan
 */
public class Connector implements Connectable {

    private final Properties properties;

    /**
     * Constructs a Connector object with the specified configuration file path.
     *
     * @param configFilePath the path to the configuration file
     */
    public Connector(String configFilePath) {
        properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFilePath)) {
            properties.load(input);
        } catch (IOException e) {
            throw new NullPointerException(
                    "An error occurred while searching for the file at the specified path: " + configFilePath);
        }
    }

    /**
     * Retrieves a database connection based on the configuration properties.
     *
     * @return the Connection object representing the database connection
     * @throws SQLException if a database access error occurs
     */
    @Override
    public Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }

}
