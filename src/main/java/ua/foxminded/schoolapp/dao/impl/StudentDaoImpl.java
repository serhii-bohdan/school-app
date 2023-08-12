package ua.foxminded.schoolapp.dao.impl;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.foxminded.schoolapp.dao.StudentDao;
import ua.foxminded.schoolapp.dao.mapper.StudentRowMapper;
import ua.foxminded.schoolapp.model.Student;

/**
 * The StudentDaoImpl class is an implementation of the {@link StudentDao}
 * interface. It provides methods for accessing and manipulating Student
 * entities in the database.
 * <p>
 * This class uses {@link JdbcTemplate} to interact with the database and a
 * {@link StudentRowMapper} to map rows of the result set to Student objects. It
 * is annotated with {@link Repository}, marking it as a Spring repository
 * component, which enables automatic dependency injection and exception
 * translation for database access.
 * <p>
 * Note: This class should be used in conjunction with the Spring context to
 * configure the application and enable database access for Student entities.
 *
 * @author Serhii Bohdan
 */
@Repository
public class StudentDaoImpl implements StudentDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Student> studentRowMapper = new StudentRowMapper();

    /**
     * Constructs a new StudentDaoImpl with the given JdbcTemplate.
     *
     * @param jdbcTemplate the JdbcTemplate to be used for database interactions
     */
    public StudentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int save(Student student) {
        String sql = """
                INSERT INTO students (first_name, last_name, group_id)
                VALUES(?, ?, ?);
                """;

        return jdbcTemplate.update(sql, student.getFirstName(), student.getLastName(), student.getGroupId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Student find(int studentId) {
        String sql = """
                SELECT * FROM students
                WHERE student_id = ?;
                """;

        try {
            return jdbcTemplate.queryForObject(sql, studentRowMapper, studentId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Student> findAll() {
        String sql = "SELECT * FROM students;";

        return jdbcTemplate.query(sql, studentRowMapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(Student student) {
        String sql = """
                UPDATE students
                SET first_name = ?, last_name = ?, group_id = ?
                WHERE student_id = ?;
                """;

        return jdbcTemplate.update(sql, student.getFirstName(), student.getLastName(), student.getGroupId(),
                student.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int delete(int studentId) {
        String sql = """
                DELETE FROM students
                WHERE student_id = ?;
                """;

        return jdbcTemplate.update(sql, studentId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Student> findStudentsRelatedToCourse(String courseName) {
        String sql = """
                SELECT students.student_id, first_name, last_name, group_id
                FROM students
                JOIN students_courses ON students.student_id = students_courses.student_id
                JOIN courses ON courses.course_id = students_courses.course_id
                WHERE course_name = ?;
                """;

        return jdbcTemplate.query(sql, studentRowMapper, courseName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStudentOnCourse(String firstName, String lastName, String courseName) {
        String sql = """
                SELECT students.student_id, first_name, last_name, group_id FROM students
                JOIN students_courses ON students.student_id = students_courses.student_id
                JOIN courses ON courses.course_id = students_courses.course_id
                WHERE students.first_name = ? AND students.last_name = ?
                AND courses.course_name = ?;
                """;

        return !jdbcTemplate.query(sql, studentRowMapper, firstName, lastName, courseName).isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addStudentToCourse(String firstName, String lastName, String courseName) {
        String sql = """
                INSERT INTO students_courses (student_id, course_id)
                VALUES ((SELECT student_id FROM students WHERE first_name = ? AND last_name = ?),
                (SELECT course_id FROM courses WHERE course_name = ?));
                """;

        return jdbcTemplate.update(sql, firstName, lastName, courseName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addStudentToCourse(int studentId, int courseId) {
        String sql = """
                INSERT INTO students_courses (student_id, course_id)
                VALUES (?, ?);
                """;

        return jdbcTemplate.update(sql, studentId, courseId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteStudentFromCourse(String firstName, String lastName, String courseName) {
        String sql = """
                DELETE FROM students_courses
                WHERE student_id = (SELECT student_id FROM students WHERE first_name = ?
                AND last_name = ?)
                AND course_id = (SELECT course_id FROM courses WHERE course_name = ?);
                """;

        return jdbcTemplate.update(sql, firstName, lastName, courseName);
    }

}
