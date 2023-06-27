package ua.foxminded.schoolapp.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The Connectable interface provides a contract for classes that can establish
 * a database connection.
 *
 * @author Serhii Bohdan
 */
public interface Connectable {

    /**
     * Retrieves a database connection.
     *
     * @return the Connection object representing the database connection
     * @throws SQLException if a database access error occurs
     */
    Connection getConnection() throws SQLException;

}
