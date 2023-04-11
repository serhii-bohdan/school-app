package ua.foxminded.schoolapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupDAO {

    private static final String URL = "jdbc:postgresql://localhost:5432/school";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    public void saveGroup(String groupName) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO groups (group_name)"
                                                                    + "VALUES(?)");
            statement.setString(1, groupName);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }

    public int findGroupIdByGroupName(String groupName) {
        int groupId = 0;    

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement("SELECT group_id FROM groups\n"
                                                                    + "WHERE group_name = ?");
            statement.setString(1, groupName);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                groupId = resultSet.getInt("group_id");
            }

        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
        return groupId;
    }

}
