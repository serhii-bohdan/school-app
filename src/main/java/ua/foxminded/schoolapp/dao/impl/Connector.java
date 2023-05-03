package ua.foxminded.schoolapp.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import ua.foxminded.schoolapp.dao.Connectable;

public class Connector implements Connectable {

    public static final String URL = "jdbc:postgresql://localhost:5432/school";
    public static final String USER = "postgres";
    public static final String PASSWORD = "1234";

    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
