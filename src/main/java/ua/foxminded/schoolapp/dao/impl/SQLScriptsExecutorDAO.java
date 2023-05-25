package ua.foxminded.schoolapp.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import ua.foxminded.schoolapp.exception.DAOException;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.ExecutorDAO;

public class SQLScriptsExecutorDAO implements ExecutorDAO {

    private Connectable connector;

    public SQLScriptsExecutorDAO(Connectable connector) {
        Objects.requireNonNull(connector);
        this.connector = connector;
    }

    public void executeSqlScript(String sqlScript) {
        try (Connection connection = connector.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);
        } catch (SQLException e) {
            throw new DAOException("Exception during SQL script execution.");
        }
    }

}
