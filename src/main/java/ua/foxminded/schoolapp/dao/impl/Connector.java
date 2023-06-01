package ua.foxminded.schoolapp.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import ua.foxminded.schoolapp.dao.Connectable;

public class Connector implements Connectable {

    private final Properties properties;

    public Connector(String configFilePath) {
        properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFilePath)) {
            properties.load(input);
        } catch (IOException e) {
            throw new NullPointerException("An error occurred while searching for "
                    + "the file at the specified path: " + configFilePath);
        }
    }

    public Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }

}
