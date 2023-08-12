package ua.foxminded.schoolapp.dao.impl;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.foxminded.schoolapp.dao.CourseDao;
import ua.foxminded.schoolapp.dao.mapper.CourseRowMapper;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Student;

/**
 * The CourseDaoImpl class is an implementation of the {@link CourseDao}
 * interface. It provides methods for accessing and manipulating Course entities
 * in the database.
 * <p>
 * This class is annotated with {@link Repository}, marking it as a Spring
 * repository component, which enables automatic dependency injection and
 * exception translation for database access.
 * <p>
 * The CourseDaoImpl uses the {@link JdbcTemplate} to interact with the
 * database. It also uses a {@link CourseRowMapper} to map rows of the result
 * set to Course objects.
 * <p>
 * Note: This class should be used in conjunction with the Spring context to
 * configure the application and enable database access for Course entities.
 *
 * @author Serhii Bohdan
 */
@Repository
public class CourseDaoImpl implements CourseDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Course> courseRowMapper = new CourseRowMapper();

    /**
     * Constructs a new CourseDaoImpl with the specified JdbcTemplate.
     *
     * @param jdbcTemplate the JdbcTemplate used to interact with the database
     */
    public CourseDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int save(Course course) {
        String sql = """
                INSERT INTO courses (course_name, course_description)
                VALUES(?, ?);
                """;

        return jdbcTemplate.update(sql, course.getCourseName(), course.getDescription());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course find(int courseId) {
        String sql = """
                SELECT * FROM courses
                WHERE course_id = ?;
                """;

        try {
            return jdbcTemplate.queryForObject(sql, courseRowMapper, courseId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Course> findAll() {
        String sql = "SELECT * FROM courses;";

        return jdbcTemplate.query(sql, courseRowMapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(Course course) {
        String sql = """
                UPDATE courses
                SET course_name = ?, course_description = ?
                WHERE course_id = ?;
                """;

        return jdbcTemplate.update(sql, course.getCourseName(), course.getDescription(), course.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int delete(int courseId) {
        String sql = """
                DELETE FROM students_courses
                WHERE course_id = ?;

                DELETE FROM courses
                WHERE course_id = ?;
                """;

        return jdbcTemplate.update(sql, courseId, courseId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Course> findCoursesForStudent(Student student) {
        String sql = """
                SELECT courses.course_id, course_name, course_description
                FROM students JOIN students_courses ON students.student_id = students_courses.student_id
                JOIN courses ON courses.course_id = students_courses.course_id
                WHERE students.student_id = ? AND students.first_name = ? AND students.last_name = ?;
                """;

        return jdbcTemplate.query(sql, courseRowMapper, student.getId(), student.getFirstName(), student.getLastName());
    }

}
