package ua.foxminded.schoolapp.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import ua.foxminded.schoolapp.exception.DAOException;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.ExecutorDAO;
import ua.foxminded.schoolapp.datasetup.Reader;

public class SQLScriptsExecutorDAO implements ExecutorDAO {

    private Connectable connector;
    private Reader reader;

    public SQLScriptsExecutorDAO(Connectable connector, Reader reader) {
        this.connector = connector;
        this.reader = reader;
    }

    public void executeSqlScriptFrom(String filePath) {
        try (Connection connection = connector.getConnection()) {
            Statement statement = connection.createStatement();
            String sqlScript = reader.readAllFileToString(filePath);
            statement.execute(sqlScript);
        } catch (SQLException e) {
            throw new DAOException("Connection failure while executing SQL script.");
        }
    }

}
