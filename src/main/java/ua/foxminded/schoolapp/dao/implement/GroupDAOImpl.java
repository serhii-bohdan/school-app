package ua.foxminded.schoolapp.dao.implement;

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

    public int findGroupId(Group group) {
        Connectable connector = new Connector();
        int groupId = 0;

        try (Connection connection = connector.createConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT group_id FROM groups\n"
                                                                    + "WHERE group_name = ?");
            statement.setString(1, group.getGroupName());
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                groupId = resultSet.getInt("group_id");
            }

        } catch (SQLException e) {
            throw new DAOException("Connection failed while finding group ID.");
        }
        return groupId;
    }

    public List<Group> findGroupsWithGivenNumberStudents(int amountOfStudents) {
        Connectable connector = new Connector();
        List<Group> groups = new ArrayList<>();

        try (Connection connection = connector.createConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT group_name, COUNT(student_id)
                    FROM students
                    LEFT JOIN groups USING(group_id)
                    GROUP BY groups.group_id
                    HAVING COUNT(student_id) <= ?
                    ORDER BY COUNT(student_id) DESC;""");
            statement.setInt(1, amountOfStudents);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                groups.add(new Group(resultSet.getString("group_name")));
            }

        } catch (SQLException e) {
            throw new DAOException("Connection failed while finding for groups with "
                    + "the specified number of students.");
        }
        return groups;
    }

}
