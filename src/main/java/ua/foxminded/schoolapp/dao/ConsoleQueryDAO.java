package ua.foxminded.schoolapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ua.foxminded.schoolapp.entity.Group;

public class ConsoleQueryDAO {

    private static final String URL = "jdbc:postgresql://localhost:5432/school";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    public List<Group> findGroupsWithGivenNumberStudents(int amountOfStudents) {
        List<Group> groups = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement("SELECT groups.group_id, group_name, COUNT(student_id)\n"
                                                                    + "FROM students\n"
                                                                    + "LEFT JOIN groups USING(group_id)\n"
                                                                    + "GROUP BY groups.group_id\n"
                                                                    + "HAVING COUNT(student_id) <= ?\n"
                                                                    + "ORDER BY COUNT(student_id) DESC");
            statement.setInt(1, amountOfStudents);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                groups.add(new Group(resultSet.getInt("group_id"), resultSet.getString("group_name")));
            }

        } catch (SQLException e) {
            System.err.println("Connection failure.");
            e.printStackTrace();
        }
        return groups;
    }

}
