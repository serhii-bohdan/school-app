package ua.foxminded.schoolapp.dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.StudentDAO;
import ua.foxminded.schoolapp.exception.DAOException;
import ua.foxminded.schoolapp.model.Student;

public class StudentDAOImpl implements StudentDAO {

    public int save(Student student) {
        Connectable connector = new Connector();
        int rowsInserted;

        try (Connection connection = connector.createConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO students (group_id, first_name, last_name)\n"
                                                                    + "VALUES(?, ?, ?);");
            statement.setInt(1, student.getGroupId());
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getLastName());
            rowsInserted = statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Connection failure while saving student.");
        }
        return rowsInserted;
    }

    public List<Student> findAllStudents() {
        Connectable connector = new Connector();
        List<Student> students = new ArrayList<>();

        try (Connection connection = connector.createConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT first_name, last_name, group_id\n"
                                    + "FROM students;");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                students.add(new Student(resultSet.getString("first_name"), resultSet.getString("last_name"),
                        resultSet.getInt("group_id")));
            }

        } catch (SQLException e) {
            throw new DAOException("Connection failed while finding for all students.");
        }
        return students;
    }

    public int findStudentId(Student student) {
        Connectable connector = new Connector();
        int studentId = 0;

        try (Connection connection = connector.createConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT student_id
                    FROM students
                    WHERE first_name = ? AND last_name = ? AND group_id = ?;""");
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setInt(3, student.getGroupId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                studentId = resultSet.getInt("student_id");
            }

        } catch (SQLException e) {
            throw new DAOException("Connection failed while finding for student ID.");
        }
        return studentId;
    }

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
                students.add(new Student(resultSet.getString("first_name"), resultSet.getString("last_name"),
                        resultSet.getInt("group_id")));
            }

        } catch (SQLException e) {
            throw new DAOException("Connection failure when finding for students related to a specific course.");
        }
        return students;
    }

    public int deleteStudentById(int studentId) {
        Connectable connector = new Connector();
        int rowsDeleted;

        try (Connection connection = connector.createConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM students\n" 
                                                                    + "WHERE student_id = ?;");
            statement.setInt(1, studentId);
            rowsDeleted = statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Connection failure when deleting a student by their ID.");
        }
        return rowsDeleted;
    }

    public boolean isStudentOnCourse(String firstName, String lastName, String courseName) {
        Connectable connector = new Connector();
        boolean exists = false;

        try (Connection connection = connector.createConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT 1 FROM students_courses
                    JOIN students ON students.student_id = students_courses.fk_student_id
                    JOIN courses ON courses.course_id = students_courses.fk_course_id
                    WHERE students.first_name = ?
                    AND students.last_name = ?
                    AND courses.course_name = ?""");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, courseName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                exists = true;
            }

        } catch (SQLException e) {
            throw new DAOException("Connection failure while checking for student availability.");
        }
        return exists;
    }

    public int addStudentToCourse(String firstName, String lastName, String courseName) {
        Connectable connector = new Connector();
        int rowsInserted;

        try (Connection connection = connector.createConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO students_courses (fk_student_id, fk_course_id)
                    VALUES ((SELECT student_id FROM students WHERE first_name = ? AND last_name = ?),
                    (SELECT course_id FROM courses WHERE course_name = ?))""");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, courseName);
            rowsInserted = statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Connection failed while adding a student to a course.");
        }
        return rowsInserted;
    }

    public int deleteStudentFromCourse(String firstName, String lastName, String courseName) {
        Connectable connector = new Connector();
        int rowsDeleted;

        try (Connection connection = connector.createConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    DELETE FROM students_courses
                    WHERE fk_student_id = (SELECT student_id FROM students WHERE first_name = ?
                    AND last_name = ?)
                    AND fk_course_id = (SELECT course_id FROM courses WHERE course_name = ?);""");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, courseName);
            rowsDeleted = statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Connection failure when removing a student from a course.");
        }
        return rowsDeleted;
    }
}
