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

/**
 * The CourseDaoImpl class is an implementation of the {@link CourseDao} interface. It
 * provides methods for accessing and manipulating Course entities in the
 * database.
 *
 * @author Serhii Bohdan
 */
public class CourseDaoImpl implements CourseDao {

    private Connectable connector;

    /**
     * Constructs a CourseDaoImpl object with the specified Connectable connector.
     *
     * @param connector the Connectable object used for obtaining a database
     *                  connection
     */
    public CourseDaoImpl(Connectable connector) {
        Objects.requireNonNull(connector);
        this.connector = connector;
    }

    /**
     * Saves the Course entity to the database and returns the number of affected
     * rows.
     *
     * @param course the Course entity to save
     * @return the number of affected rows
     */
    @Override
    public int save(Course course) {
        int rowsInserted;

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO courses (course_name, course_description)\n"
                                                                    + "VALUES(?, ?)");
            statement.setString(1, course.getCourseName());
            statement.setString(2, course.getDescription());
            rowsInserted = statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("An error occurred when saving the course data to the database.\n" + e.getMessage());
        }
        return rowsInserted;
    }

    /**
     * Retrieves all courses from the database.
     *
     * @return a list of all Course objects
     */
    @Override
    public List<Course> findAllCourses() {
        List<Course> courses = new ArrayList<>();

        try (Connection connection = connector.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT course_id, course_name, course_description\n"
                                                       + "FROM courses");
            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt("course_id"));
                course.setCourseName(resultSet.getString("course_name"));
                course.setDescription(resultSet.getString("course_description"));
                courses.add(course);
            }

        } catch (SQLException e) {
            throw new DaoException("An error occurred when searching for all course data." + e.getMessage());
        }
        return courses;
    }

}
