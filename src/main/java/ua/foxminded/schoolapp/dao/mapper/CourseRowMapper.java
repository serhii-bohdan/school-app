package ua.foxminded.schoolapp.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import ua.foxminded.schoolapp.model.Course;

/**
 * The CourseRowMapper class is an implementation of the Spring
 * {@link RowMapper} interface for mapping rows of the result set to
 * {@link Course} objects.
 * <p>
 * This mapper is used by Spring's {@link JdbcTemplate} when querying the
 * database to convert the retrieved data from the result set into Course
 * objects.
 * <p>
 * Note: This mapper assumes that the columns "course_id", "course_name", and
 * "course_description" exist in the result set, and it maps them accordingly to
 * the corresponding fields of the Course object.
 * <p>
 *
 * @author Serhii Bohdan
 */
public class CourseRowMapper implements RowMapper<Course> {

    /**
     * Maps a row from the result set to a Course object.
     *
     * @param rs     the result set with the data to be mapped
     * @param rowNum the current row number
     * @return the Course object mapped from the row
     * @throws SQLException if an SQL exception occurs during mapping
     */
    @Override
    public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
        Course course = new Course();

        course.setId(rs.getInt("course_id"));
        course.setCourseName(rs.getString("course_name"));
        course.setDescription(rs.getString("course_description"));
        return course;
    }

}
