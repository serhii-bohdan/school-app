package ua.foxminded.schoolapp.dao.impl;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.foxminded.schoolapp.dao.GroupDao;
import ua.foxminded.schoolapp.dao.mapper.GroupRowMapper;
import ua.foxminded.schoolapp.model.Group;

/**
 * The GroupDaoImpl class is an implementation of the {@link GroupDao}
 * interface. It provides methods for accessing and manipulating Group entities
 * in the database.
 * <p>
 * This class is annotated with {@link Repository}, marking it as a Spring
 * repository component, which enables automatic dependency injection and
 * exception translation for database access.
 * <p>
 * The GroupDaoImpl uses the {@link JdbcTemplate} to interact with the database.
 * It also uses a {@link GroupRowMapper} to map rows of the result set to Group
 * objects.
 * <p>
 * Note: This class should be used in conjunction with the Spring context to
 * configure the application and enable database access for Group entities.
 *
 * @author Serhii Bohdan
 */
@Repository
public class GroupDaoImpl implements GroupDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Group> groupRowMapper = new GroupRowMapper();

    /**
     * Constructs a new GroupDaoImpl with the given JdbcTemplate.
     *
     * @param jdbcTemplate the JdbcTemplate to be used for database interactions
     */
    public GroupDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int save(Group group) {
        String sql = """
                INSERT INTO groups (group_name)
                VALUES( ? );
                """;

        return jdbcTemplate.update(sql, group.getGroupName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Group find(int groupId) {
        String sql = """
                SELECT * FROM groups
                WHERE group_id = ?;
                """;

        try {
            return jdbcTemplate.queryForObject(sql, groupRowMapper, groupId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> findAll() {
        String sql = "SELECT * FROM groups;";

        return jdbcTemplate.query(sql, groupRowMapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(Group group) {
        String sql = """
                UPDATE groups SET group_name = ?
                WHERE group_id = ?;
                """;

        return jdbcTemplate.update(sql, group.getGroupName(), group.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int delete(int groupId) {
        String sql = """
                DELETE FROM students
                WHERE group_id = ?;

                DELETE FROM groups
                WHERE group_id = ?;
                """;

        return jdbcTemplate.update(sql, groupId, groupId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> findGroupsWithGivenNumberOfStudents(int amountOfStudents) {
        String sql = """
                SELECT groups.group_id, group_name
                FROM students
                LEFT JOIN groups USING(group_id)
                GROUP BY groups.group_id
                HAVING COUNT(student_id) <= ?
                ORDER BY COUNT(student_id) DESC;
                """;

        return jdbcTemplate.query(sql, groupRowMapper, amountOfStudents);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int findNumberOfStudentsForGroup(Group group) {
        String sql = """
                SELECT COUNT(student_id) AS students_count
                FROM students
                LEFT JOIN groups USING(group_id)
                GROUP BY groups.group_id
                HAVING groups.group_id = ?;
                """;
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, group.getId());
        } catch (EmptyResultDataAccessException e) {
            return -1;
        }
    }

}
