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

public class CourseDaoImpl implements CourseDao {
    
    private Connectable connector;

    public CourseDaoImpl(Connectable connector) {
        Objects.requireNonNull(connector);
        this.connector = connector;
    }

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
