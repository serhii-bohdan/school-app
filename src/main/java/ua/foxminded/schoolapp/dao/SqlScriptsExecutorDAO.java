package ua.foxminded.schoolapp.dao;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.io.FileUtils;

public class SqlScriptsExecutorDAO {

    private static final String URL = "jdbc:postgresql://localhost:5432/school";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    public void executeSqlScriptFrom(String scriptFileName) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            String sqlScript = convertFileToSqlScript(scriptFileName);
            statement.execute(sqlScript);
        } catch (SQLException e) {
            System.err.println("Connection failure.");
            e.printStackTrace();
        }
    }

    private String convertFileToSqlScript(String scriptFileName) {
        File scriptFile = new File("src/main/resources/sql/" + scriptFileName);
        String script = null;

        try {
            script = FileUtils.readFileToString(scriptFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("File not found.");
            e.printStackTrace();
        }
        return script;
    }
}
