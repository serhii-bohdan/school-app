package ua.foxminded.schoolapp.dao.implement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.ExecutorDAO;
import ua.foxminded.schoolapp.exception.DAOException;
import ua.foxminded.schoolapp.datasetup.Reader;

public class SQLScriptsExecutorDAO implements ExecutorDAO {

    private Reader reader;

    public SQLScriptsExecutorDAO(Reader reader) {
        this.reader = reader;
    }

    public void executeSqlScriptFrom(String filePath) {
        Connectable connector = new Connector();

        try (Connection connection = connector.createConnection()) {
            Statement statement = connection.createStatement();
            String sqlScript = reader.readSqlScriptFrom(filePath);
            statement.execute(sqlScript);
        } catch (SQLException e) {
            throw new DAOException("Connection failure while executing SQL script.");
        }
    }

}
