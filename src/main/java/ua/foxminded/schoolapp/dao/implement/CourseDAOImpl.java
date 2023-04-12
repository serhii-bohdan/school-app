package ua.foxminded.schoolapp.dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.CourseDAO;
import ua.foxminded.schoolapp.entity.Course;
import ua.foxminded.schoolapp.entity.Student;

public class CourseDAOImpl implements CourseDAO {

    @Override
    public void save(Course course) {
        Connectable connector = new Connector();

        try (Connection connection = connector.createConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO courses (course_name, course_description)"
                                                                    + "VALUES(?, ?)");
            statement.setString(1, course.getName());
            statement.setString(2, course.getDescription());
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }

    @Override
    public List<Student> findStudentsRelatedToCourse(String courseName) {
        Connectable connector = new Connector();
        List<Student> students = new ArrayList<>();

        try (Connection connection = connector.createConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT group_id, first_name, last_name
                    FROM students
                    JOIN students_courses ON students.student_id = students_courses.fk_student_id
                    JOIN courses ON courses.course_id = students_courses.fk_course_id
                    WHERE course_name = ?;""");
            statement.setString(1, courseName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                students.add(new Student(resultSet.getInt("group_id"), resultSet.getString("first_name"),
                        resultSet.getString("last_name")));
            }

        } catch (SQLException e) {
            System.err.println("Connection failure.");
            e.printStackTrace();
        }
        return students;
    }

}
