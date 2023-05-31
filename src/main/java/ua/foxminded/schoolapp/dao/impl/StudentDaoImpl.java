package ua.foxminded.schoolapp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.StudentDAO;
import ua.foxminded.schoolapp.exception.DAOException;
import ua.foxminded.schoolapp.model.Student;

public class StudentDAOImpl implements StudentDAO {

    private Connectable connector;

    public StudentDAOImpl(Connectable connector) {
        Objects.requireNonNull(connector);
        this.connector = connector;
    }

    public int save(Student student) {
        int rowsInserted;

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO students (first_name, last_name, group_id)\n"
                                                                    + "VALUES(?, ?, ?);");
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setInt(3, student.getGroupId());
            rowsInserted = statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Connection failure while saving student.");
        }
        return rowsInserted;
    }

    public List<Student> findAllStudents() {
        List<Student> students = new ArrayList<>();

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT student_id, first_name, last_name, group_id\n"
                                    + "FROM students;");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt("student_id"));
                student.setFirstName(resultSet.getString("first_name"));
                student.setLastName(resultSet.getString("last_name"));
                student.setGroupId(resultSet.getInt("group_id"));
                students.add(student);
            }

        } catch (SQLException e) {
            throw new DAOException("Connection failed while finding for all students.");
        }
        return students;
    }

    public Student findStudentById(int studentId) {
        Student student = new Student();

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT student_id, first_name, last_name, group_id
                    FROM students
                    WHERE student_id = ?;""");
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                student.setId(resultSet.getInt("student_id"));
                student.setFirstName(resultSet.getString("first_name"));
                student.setLastName(resultSet.getString("last_name"));
                student.setGroupId(resultSet.getInt("group_id"));
            }

        } catch (SQLException e) {
            throw new DAOException("Connection failed while finding for student ID.");
        }
        return student;
    }

    public List<Student> findStudentsRelatedToCourse(String courseName) {
        List<Student> students = new ArrayList<>();

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT student_id, first_name, last_name, group_id
                    FROM students
                    JOIN students_courses ON students.student_id = students_courses.fk_student_id
                    JOIN courses ON courses.course_id = students_courses.fk_course_id
                    WHERE course_name = ?;""");
            statement.setString(1, courseName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt("student_id"));
                student.setFirstName(resultSet.getString("first_name"));
                student.setLastName(resultSet.getString("last_name"));
                student.setGroupId(resultSet.getInt("group_id"));
                students.add(student);
            }

        } catch (SQLException e) {
            throw new DAOException("Connection failure when finding for students related to a specific course.");
        }
        return students;
    }

    public int deleteStudentById(int studentId) {
        int rowsDeleted;

        try (Connection connection = connector.getConnection()) {
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
        boolean exists = false;

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT 1 FROM students
                    JOIN students_courses ON students.student_id = students_courses.fk_student_id
                    JOIN courses ON courses.course_id = students_courses.fk_course_id
                    WHERE students.first_name = ?
                    AND students.last_name = ?
                    AND courses.course_name = ?;""");
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
        int rowsInserted;

        try (Connection connection = connector.getConnection()) {
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
        int rowsDeleted;

        try (Connection connection = connector.getConnection()) {
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
