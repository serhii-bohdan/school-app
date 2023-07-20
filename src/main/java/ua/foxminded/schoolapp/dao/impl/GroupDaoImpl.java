package ua.foxminded.schoolapp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.GroupDao;
import ua.foxminded.schoolapp.exception.DaoException;
import ua.foxminded.schoolapp.model.Group;

/**
 * The GroupDaoImpl class is an implementation of the {@link GroupDao}
 * interface. It provides methods for accessing and manipulating Group entities
 * in the database.
 *
 * @author Serhii Bohdan
 */
public class GroupDaoImpl implements GroupDao {

    /**
     * A constant representing a new line character.
     */
    public static final String NEW_LINE = "\n";

    private Connectable connector;

    /**
     * Constructs a GroupDaoImpl object with the specified Connectable connector.
     *
     * @param connector the Connectable object used for obtaining a database
     *                  connection
     */
    public GroupDaoImpl(Connectable connector) {
        Objects.requireNonNull(connector, "connector must not be null");
        this.connector = connector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int save(Group group) {
        int rowsInserted;

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO groups (group_name)
                    VALUES(?);
                    """);
            statement.setString(1, group.getGroupName());
            rowsInserted = statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Error saving group data to database." + NEW_LINE + e.getMessage());
        }
        return rowsInserted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> findAll() {
        List<Group> groups = new ArrayList<>();

        try (Connection connection = connector.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("""
                    SELECT group_id, group_name
                    FROM groups;
                    """);

            while (resultSet.next()) {
                Group group = new Group();
                group.setId(resultSet.getInt("group_id"));
                group.setGroupName(resultSet.getString("group_name"));
                groups.add(group);
            }

        } catch (SQLException e) {
            throw new DaoException(
                    "An error occurred while searching for data about all groups." + NEW_LINE + e.getMessage());
        }
        return groups;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Group, Integer> findGroupsWithGivenNumberStudents(int amountOfStudents) {
        Map<Group, Integer> groupsWithTheirNumberOfStudents = new HashMap<>();

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT groups.group_id, group_name, COUNT(student_id) AS students_count
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
                groupsWithTheirNumberOfStudents.put(group, resultSet.getInt("students_count"));
            }

        } catch (SQLException e) {
            throw new DaoException("An error occurred when searching for groups "
                    + "with the specified number of students." + NEW_LINE + e.getMessage());
        }
        return groupsWithTheirNumberOfStudents;
    }

}
