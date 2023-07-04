package ua.foxminded.schoolapp.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import ua.foxminded.schoolapp.exception.DaoException;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.ExecutorDao;

/**
 * The SqlScriptsExecutorDao class is responsible for executing SQL scripts. It
 * implements the {@link ExecutorDao} interface.
 *
 * @author Serhii Bohdan
 */
public class SqlScriptsExecutorDao implements ExecutorDao {

    private Connectable connector;

    /**
     * Constructs a SqlScriptsExecutorDao object with the specified Connectable
     * connector.
     *
     * @param connector the Connectable object used for obtaining a database
     *                  connection
     */
    public SqlScriptsExecutorDao(Connectable connector) {
        Objects.requireNonNull(connector);
        this.connector = connector;
    }

    /**
     * Executes the provided SQL script using a database connection obtained from
     * the connector.
     *
     * @param sqlScript the SQL script to execute
     */
    @Override
    public void executeSqlScript(String sqlScript) {
        try (Connection connection = connector.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sqlScript);
        } catch (SQLException e) {
            throw new DaoException(
                    "An error occurred during the execution of the transferred SQL script.\n" + e.getMessage());
        }
    }

}
