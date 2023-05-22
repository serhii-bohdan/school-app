package ua.foxminded.schoolapp.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface Connectable {

    Connection getConnection() throws SQLException;

}
