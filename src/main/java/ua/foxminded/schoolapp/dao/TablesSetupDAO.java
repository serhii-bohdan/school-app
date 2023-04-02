package ua.foxminded.schoolapp.dao;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.io.FileUtils;

public class TablesSetupDAO {

    private static final String URL = "jdbc:postgresql://localhost:5432/school";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    public void createTables() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            executeScript(statement, "groups_table_creation.sql");
            executeScript(statement, "students_table_creation.sql");
            executeScript(statement, "courses_table_creation.sql");
        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }

    private void executeScript(Statement statement, String scriptFileName) throws SQLException {
        File scriptFile = new File("src/main/resources/sql/" + scriptFileName);
        String script = null;

        try {
            script = FileUtils.readFileToString(scriptFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
        statement.execute(script);
    }
}
