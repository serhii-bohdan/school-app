package ua.foxminded.schoolapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentDAO {

    private static final String URL = "jdbc:postgresql://localhost:5432/school";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    public void saveStudent(int groupId, String firstName, String lastName) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO students (group_id, first_name, last_name)"
                                                                    + "VALUES(?, ?, ?);");
            statement.setInt(1, groupId);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }

}
