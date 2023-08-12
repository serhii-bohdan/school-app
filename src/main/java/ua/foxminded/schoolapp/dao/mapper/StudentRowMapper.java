package ua.foxminded.schoolapp.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import ua.foxminded.schoolapp.model.Student;

/**
 * The StudentRowMapper class is an implementation of the Spring
 * {@link RowMapper} interface for mapping rows of the result set to
 * {@link Student} objects.
 * <p>
 * This mapper is used by Spring's {@link JdbcTemplate} when querying the
 * database to convert the retrieved data from the result set into Student
 * objects.
 * <p>
 * Note: This mapper assumes that the columns "student_id", "first_name",
 * "last_name", and "group_id" exist in the result set, and it maps them
 * accordingly to the corresponding fields of the Student object.
 *
 * @author Serhii Bohdan
 */
public class StudentRowMapper implements RowMapper<Student> {

    /**
     * Maps a row from the result set to a Student object.
     *
     * @param rs     the result set with the data to be mapped
     * @param rowNum the current row number
     * @return the Student object mapped from the row
     * @throws SQLException if an SQL exception occurs during mapping
     */
    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student();

        student.setId(rs.getInt("student_id"));
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setGroupId(rs.getInt("group_id"));
        return student;
    }

}
