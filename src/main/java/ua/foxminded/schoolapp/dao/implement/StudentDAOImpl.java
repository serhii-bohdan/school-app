package ua.foxminded.schoolapp.dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.StudentDAO;
import ua.foxminded.schoolapp.entity.Student;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public void save(Student student) {
        Connectable connector = new Connector();

        try (Connection connection = connector.createConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO students (group_id, first_name, last_name)"
                                                                    + "VALUES(?, ?, ?);");
            statement.setInt(1, student.getGroupId());
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getLastName());
            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Connection failure.");
            e.printStackTrace();
        }
    }

    @Override
    public List<Student> findAvailableStudents() {
        Connectable connector = new Connector();
        List<Student> students = new ArrayList<>();

        try (Connection connection = connector.createConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT first_name, last_name, group_id\n"
                    + "FROM students;");
            ResultSet resultSet = statement.executeQuery();
            
            while(resultSet.next()) {
                students.add(new Student(resultSet.getInt("group_id"), resultSet.getString("first_name"),
                        resultSet.getString("last_name")));
            }

        } catch (SQLException e) {
            System.err.println("Connection failure.");
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public int findStudentIdByNameAndGroupId(String firstName, String lastName, int groupId) {
        Connectable connector = new Connector();
        int studentId = 0;

        try (Connection connection = connector.createConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT student_id
                    FROM students
                    WHERE first_name = ? AND last_name = ? AND group_id = ?;""");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, groupId);
            ResultSet resultSet = statement.executeQuery();
            
            while(resultSet.next()) {
                studentId = resultSet.getInt("student_id");
            }
            
        } catch (SQLException e) {
            System.err.println("Connection failure.");
            e.printStackTrace();
        }
        return studentId;
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

    public void deleteStudentById(int studentId) {
        Connectable connector = new Connector();

        try (Connection connection = connector.createConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM students\n"
                                                                    + "WHERE student_id = ?;");
            statement.setInt(1, studentId);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Connection failure.");
            e.printStackTrace();
        }
    }

}
