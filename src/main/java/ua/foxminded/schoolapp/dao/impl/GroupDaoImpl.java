package ua.foxminded.schoolapp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.GroupDao;
import ua.foxminded.schoolapp.exception.DaoException;
import ua.foxminded.schoolapp.model.Group;

/**
 * The GroupDaoImpl class is an implementation of the {@link GroupDao} interface. It
 * provides methods for accessing and manipulating Group entities in the
 * database.
 *
 * @author Serhii Bohdan
 */
public class GroupDaoImpl implements GroupDao {

    private Connectable connector;

    /**
     * Constructs a GroupDaoImpl object with the specified Connectable connector.
     *
     * @param connector the Connectable object used for obtaining a database
     *                  connection
     */
    public GroupDaoImpl(Connectable connector) {
        Objects.requireNonNull(connector);
        this.connector = connector;
    }

    /**
     * Saves the Group entity to the database and returns the number of affected
     * rows.
     *
     * @param group the Group entity to save
     * @return the number of affected rows
     */
    @Override
    public int save(Group group) {
        int rowsInserted;

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO groups (group_name)\n"
                                    + "VALUES(?)");
            statement.setString(1, group.getGroupName());
            rowsInserted = statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Error saving group data to database.\n" + e.getMessage());
        }
        return rowsInserted;
    }

    /**
     * Finds groups with the specified number of students.
     *
     * @param amountOfStudents the number of students
     * @return a list of Group objects that match the specified criteria
     */
    @Override
    public List<Group> findGroupsWithGivenNumberStudents(int amountOfStudents) {
        List<Group> groups = new ArrayList<>();

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT groups.group_id, group_name, COUNT(student_id)
                    FROM students
                    LEFT JOIN groups USING(group_id)
                    GROUP BY groups.group_id
                    HAVING COUNT(student_id) <= ?
                    ORDER BY COUNT(student_id) DESC;
                    """);
            statement.setInt(1, amountOfStudents);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Group group = new Group();
                group.setId(resultSet.getInt("group_id"));
                group.setGroupName(resultSet.getString("group_name"));
                groups.add(group);
            }

        } catch (SQLException e) {
            throw new DaoException("An error occurred when searching for groups "
                    + "with the specified number of students.\n" + e.getMessage());
        }
        return groups;
    }

}
