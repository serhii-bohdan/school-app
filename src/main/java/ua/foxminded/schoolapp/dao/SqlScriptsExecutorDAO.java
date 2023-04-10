package ua.foxminded.schoolapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import ua.foxminded.schoolapp.datageneration.Reader;

public class SqlScriptsExecutorDAO {

    private static final String URL = "jdbc:postgresql://localhost:5432/school";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    Reader reader = new Reader();

    public void executeSqlScriptFrom(String filePath) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            String sqlScript = reader.readSqlScriptFrom(filePath);
            statement.execute(sqlScript);
        } catch (SQLException e) {
            System.err.println("Connection failure.");
            e.printStackTrace();
        }
    }

}
