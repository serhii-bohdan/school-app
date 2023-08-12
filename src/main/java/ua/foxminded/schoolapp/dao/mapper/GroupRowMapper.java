package ua.foxminded.schoolapp.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import ua.foxminded.schoolapp.model.Group;

/**
 * The GroupRowMapper class is an implementation of the Spring {@link RowMapper}
 * interface for mapping rows of the result set to {@link Group} objects.
 * <p>
 * This mapper is used by Spring's {@link JdbcTemplate} when querying the
 * database to convert the retrieved data from the result set into Group
 * objects.
 * <p>
 * Note: This mapper assumes that the columns "group_id" and "group_name" exist
 * in the result set, and it maps them accordingly to the corresponding fields
 * of the Group object.
 *
 * @author Serhii Bohdan
 */
public class GroupRowMapper implements RowMapper<Group> {

    /**
     * Maps a row from the result set to a Group object.
     *
     * @param rs     the result set with the data to be mapped
     * @param rowNum the current row number
     * @return the Group object mapped from the row
     * @throws SQLException if an SQL exception occurs during mapping
     */
    @Override
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        Group group = new Group();

        group.setId(rs.getInt("group_id"));
        group.setGroupName(rs.getString("group_name"));
        return group;
    }

}
