package ua.foxminded.schoolapp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.CourseDAO;
import ua.foxminded.schoolapp.exception.DAOException;
import ua.foxminded.schoolapp.model.Course;

public class CourseDAOImpl implements CourseDAO {

    public int save(Course course) {
        Connectable connector = new Connector();
        int rowsInserted;

        try (Connection connection = connector.createConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO courses (course_name, course_description)\n"
                                                                    + "VALUES(?, ?)");
            statement.setString(1, course.getCourseName());
            statement.setString(2, course.getDescription());
            rowsInserted = statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Connection failure while saving course.");
        }
        return rowsInserted;
    }

    public List<Course> findAllCourses() {
        Connectable connector = new Connector();
        List<Course> courses = new ArrayList<>();

        try (Connection connection = connector.createConnection()) {
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
            throw new DAOException("Connection failed while finding all available courses.");
        }
        return courses;
    }

}
