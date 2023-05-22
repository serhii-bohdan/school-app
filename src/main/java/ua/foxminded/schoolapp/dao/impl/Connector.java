package ua.foxminded.schoolapp.dao.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import ua.foxminded.schoolapp.dao.Connectable;

public class Connector implements Connectable {

    private final Properties properties;

    public Connector(String configFilePath) {
        properties = new Properties();

        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(configFilePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }

    public Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }

}
