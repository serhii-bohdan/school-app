package ua.foxminded.schoolapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GroupDAO {

    private static final String URL = "jdbc:postgresql://localhost:5432/school";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";
    private final String sqlQuery = "insert into groups (group_id, group_name) values(?, ?)";

    public void saveGroup(int groupId, String groupName) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, groupId);
            statement.setString(2, groupName);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }

}
