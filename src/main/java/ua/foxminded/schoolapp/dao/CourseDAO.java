package ua.foxminded.schoolapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CourseDAO {

    private static final String URL = "jdbc:postgresql://localhost:5432/school";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";
    private final String sqlQuery = "INSERT INTO courses (course_id, course_name, course_description) VALUES(?, ?, ?)";

    public void saveCourse(int courseId, String courseName, String courseDescription) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, courseId);
            statement.setString(2, courseName);
            statement.setString(3, courseDescription);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }
}
