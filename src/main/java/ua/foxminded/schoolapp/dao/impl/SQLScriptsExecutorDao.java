package ua.foxminded.schoolapp.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import ua.foxminded.schoolapp.exception.DaoException;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.ExecutorDao;

public class SQLScriptsExecutorDao implements ExecutorDao {

    private Connectable connector;

    public SQLScriptsExecutorDao(Connectable connector) {
        Objects.requireNonNull(connector);
        this.connector = connector;
    }

    public void executeSqlScript(String sqlScript) {
        try (Connection connection = connector.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);
        } catch (SQLException e) {
            throw new DaoException("Exception during SQL script execution.");
        }
    }

}
