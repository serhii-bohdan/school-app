package ua.foxminded.schoolapp.dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.CourseDAO;
import ua.foxminded.schoolapp.entity.Course;

public class CourseDAOImpl implements CourseDAO {

    @Override
    public void save(Course course) {
        Connectable connector = new Connector();

        try (Connection connection = connector.createConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO courses (course_name, course_description)"
                                                                    + "VALUES(?, ?)");
            statement.setString(1, course.getCourseName());
            statement.setString(2, course.getDescription());
            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Connection failure.");
            e.printStackTrace();
        }
    }

    public List<Course> findAvailableCourses() {
        Connectable connector = new Connector();
        List<Course> courses = new ArrayList<>();

        try (Connection connection = connector.createConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT course_name, course_description "
                                                       + "FROM courses");
            while (resultSet.next()) {
                courses.add(new Course(resultSet.getString("course_name"), (resultSet.getString("course_description"))));
            }

        } catch (SQLException e) {
            System.err.println("Connection failure.");
            e.printStackTrace();
        }
        return courses;
    }

}
