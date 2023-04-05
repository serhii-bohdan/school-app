package ua.foxminded.schoolapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentDAO {

    private static final String URL = "jdbc:postgresql://localhost:5432/school";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";
    private final String sqlQuery = "insert into students (student_id, group_id, first_name, last_name) values(?, ?, ?, ?)";

    public void saveStudent(int studentId, int groupId, String firstName, String lastName) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, studentId);
            statement.setInt(2, groupId);
            statement.setString(3, firstName);
            statement.setString(4, lastName);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }

}
