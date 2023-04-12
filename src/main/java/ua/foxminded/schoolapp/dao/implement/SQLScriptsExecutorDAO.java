package ua.foxminded.schoolapp.dao.implement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.datageneration.Reader;

public class SQLScriptsExecutorDAO {

    Reader reader = new Reader();

    public void executeSqlScriptFrom(String filePath) {
        Connectable connector = new Connector();

        try (Connection connection = connector.createConnection()) {
            Statement statement = connection.createStatement();
            String sqlScript = reader.readSqlScriptFrom(filePath);
            statement.execute(sqlScript);
        } catch (SQLException e) {
            System.err.println("Connection failure.");
            e.printStackTrace();
        }
    }

}
