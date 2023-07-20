package ua.foxminded.schoolapp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.CourseDao;
import ua.foxminded.schoolapp.exception.DaoException;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Student;

/**
 * The CourseDaoImpl class is an implementation of the {@link CourseDao}
 * interface. It provides methods for accessing and manipulating Course entities
 * in the database.
 *
 * @author Serhii Bohdan
 */
public class CourseDaoImpl implements CourseDao {

    /**
     * A constant representing a new line character.
     */
    public static final String NEW_LINE = "\n";

    private Connectable connector;

    /**
     * Constructs a CourseDaoImpl object with the specified Connectable connector.
     *
     * @param connector the Connectable object used for obtaining a database
     *                  connection
     */
    public CourseDaoImpl(Connectable connector) {
        Objects.requireNonNull(connector, "connector must not be null");
        this.connector = connector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int save(Course course) {
        int rowsInserted;

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO courses (course_name, course_description)
                    VALUES(?, ?);
                    """);
            statement.setString(1, course.getCourseName());
            statement.setString(2, course.getDescription());
            rowsInserted = statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(
                    "An error occurred when saving the course data to the database." + NEW_LINE + e.getMessage());
        }
        return rowsInserted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();

        try (Connection connection = connector.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("""
                    SELECT course_id, course_name, course_description
                    FROM courses;
                    """);

            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt("course_id"));
                course.setCourseName(resultSet.getString("course_name"));
                course.setDescription(resultSet.getString("course_description"));
                courses.add(course);
            }

        } catch (SQLException e) {
            throw new DaoException("An error occurred when searching for all course data." + NEW_LINE + e.getMessage());
        }
        return courses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Course> findCoursesForStudent(Student student) {
        List<Course> coursesForStudent = new ArrayList<>();

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT courses.course_id, course_name, course_description
                    FROM students JOIN students_courses ON students.student_id = students_courses.student_id
                    JOIN courses ON courses.course_id = students_courses.course_id
                    WHERE students.student_id = ? AND students.first_name = ? AND students.last_name = ?;
                    """);
            statement.setInt(1, student.getId());
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getLastName());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt("course_id"));
                course.setCourseName(resultSet.getString("course_name"));
                course.setDescription(resultSet.getString("course_description"));
                coursesForStudent.add(course);
            }

        } catch (SQLException e) {
            throw new DaoException("An error occurred when searching for courses in which the student is registered."
                    + NEW_LINE + e.getMessage());
        }
        return coursesForStudent;
    }

}
