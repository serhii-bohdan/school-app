package ua.foxminded.schoolapp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.GroupDAO;
import ua.foxminded.schoolapp.exception.DAOException;
import ua.foxminded.schoolapp.model.Group;

public class GroupDAOImpl implements GroupDAO {

    public int save(Group group) {
        Connectable connector = new Connector();
        int rowsInserted;

        try (Connection connection = connector.createConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO groups (group_name)\n"
                                                                    + "VALUES(?)");
            statement.setString(1, group.getGroupName());
            rowsInserted = statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Connection failure while saving group.");
        }
        return rowsInserted;
    }

    public List<Group> findGroupsWithGivenNumberStudents(int amountOfStudents) {
        Connectable connector = new Connector();
        List<Group> groups = new ArrayList<>();

        try (Connection connection = connector.createConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT groups.group_id, group_name, COUNT(student_id)
                    FROM students
                    LEFT JOIN groups USING(group_id)
                    GROUP BY groups.group_id
                    HAVING COUNT(student_id) <= ?
                    ORDER BY COUNT(student_id) DESC;""");
            statement.setInt(1, amountOfStudents);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Group group = new Group();
                group.setId(resultSet.getInt("group_id"));
                group.setGroupName(resultSet.getString("group_name"));
                groups.add(group);
            }

        } catch (SQLException e) {
            throw new DAOException("Connection failed while finding for groups with "
                    + "the specified number of students.");
        }
        return groups;
    }

}
