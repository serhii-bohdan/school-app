package ua.foxminded.schoolapp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.StudentDao;
import ua.foxminded.schoolapp.exception.DaoException;
import ua.foxminded.schoolapp.model.Student;

/**
 * The StudentDaoImpl class is an implementation of the {@link StudentDao} interface. It
 * provides methods for accessing and manipulating Student entities in the
 * database.
 *
 * @author Serhii Bohdan
 */
public class StudentDaoImpl implements StudentDao {

    private Connectable connector;

    /**
     * Constructs a StudentDaoImpl object with the specified Connectable connector.
     *
     * @param connector the Connectable object used for obtaining a database
     *                  connection
     */
    public StudentDaoImpl(Connectable connector) {
        Objects.requireNonNull(connector);
        this.connector = connector;
    }

    /**
     * Saves the Student entity to the database and returns the number of affected
     * rows.
     *
     * @param student the Student entity to save
     * @return the number of affected rows
     */
    @Override
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
            throw new DaoException("An error occurred when saving student data to the database.\n" + e.getMessage());
        }
        return rowsInserted;
    }

    /**
     * Retrieves all students from the database.
     *
     * @return a list of all Student objects
     */
    @Override
    public List<Student> findAllStudents() {
        List<Student> students = new ArrayList<>();

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT student_id, first_name, last_name, group_id\n"
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
            throw new DaoException("An error occurred when searching for all students' data.\n" + e.getMessage());
        }
        return students;
    }

    /**
     * Retrieves a student by their ID from the database.
     *
     * @param studentId the ID of the student
     * @return the Student object with the specified ID, or null if not found
     */
    @Override
    public Student findStudentById(int studentId) {
        Student student = new Student();

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT student_id, first_name, last_name, group_id
                    FROM students
                    WHERE student_id = ?;
                    """);
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                student.setId(resultSet.getInt("student_id"));
                student.setFirstName(resultSet.getString("first_name"));
                student.setLastName(resultSet.getString("last_name"));
                student.setGroupId(resultSet.getInt("group_id"));
            }

        } catch (SQLException e) {
            throw new DaoException("An error occurred when searching for student data by ID.\n" + e.getMessage());
        }
        return student;
    }

    /**
     * Retrieves all students related to a specific course from the database.
     *
     * @param courseName the name of the course
     * @return a list of Student objects related to the course
     */
    @Override
    public List<Student> findStudentsRelatedToCourse(String courseName) {
        List<Student> students = new ArrayList<>();

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT students.student_id, first_name, last_name, group_id
                    FROM students
                    JOIN students_courses ON students.student_id = students_courses.student_id
                    JOIN courses ON courses.course_id = students_courses.course_id
                    WHERE course_name = ?;
                    """);
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
            throw new DaoException("An error occurred when searching for the data of "
                    + "students attending the course.\n" + e.getMessage());
        }
        return students;
    }

    /**
     * Deletes a student by their ID from the database.
     *
     * @param studentId the ID of the student to delete
     * @return the number of rows deleted
     */
    @Override
    public int deleteStudentById(int studentId) {
        int rowsDeleted;

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("DELETE FROM students\n"
                                    + "WHERE student_id = ?;");
            statement.setInt(1, studentId);
            rowsDeleted = statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Error deleting student by ID.\n" + e.getMessage());
        }
        return rowsDeleted;
    }

    /**
     * Checks if a student is enrolled in a specific course.
     *
     * @param firstName  the first name of the student
     * @param lastName   the last name of the student
     * @param courseName the name of the course
     * @return true if the student is enrolled in the course, false otherwise
     */
    @Override
    public boolean isStudentOnCourse(String firstName, String lastName, String courseName) {
        boolean exists = false;

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT 1 FROM students
                    JOIN students_courses ON students.student_id = students_courses.student_id
                    JOIN courses ON courses.course_id = students_courses.course_id
                    WHERE students.first_name = ?
                    AND students.last_name = ?
                    AND courses.course_name = ?;
                    """);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, courseName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                exists = true;
            }

        } catch (SQLException e) {
            throw new DaoException(
                    "An error occurred when checking the student's presence on the course.\n" + e.getMessage());
        }
        return exists;
    }

    /**
     * Adds a student to a specific course by their first name, last name, and
     * course name.
     *
     * @param firstName  the first name of the student
     * @param lastName   the last name of the student
     * @param courseName the name of the course
     * @return the number of rows inserted
     */
    @Override
    public int addStudentToCourse(String firstName, String lastName, String courseName) {
        int rowsInserted;

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO students_courses (student_id, course_id)
                    VALUES ((SELECT student_id FROM students WHERE first_name = ? AND last_name = ?),
                    (SELECT course_id FROM courses WHERE course_name = ?));
                    """);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, courseName);
            rowsInserted = statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Error adding student to course.\n" + e.getMessage());
        }
        return rowsInserted;
    }

    /**
     * Adds a student to a specific course by their ID and course ID.
     *
     * @param studentId the ID of the student
     * @param courseId  the ID of the course
     * @return the number of rows inserted
     */
    @Override
    public int addStudentToCourse(int studentId, int courseId) {
        int rowsInserted;

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO students_courses (student_id, course_id)
                    VALUES (?, ?);
                    """);
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            rowsInserted = statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Error adding student to course.\n" + e.getMessage());
        }
        return rowsInserted;
    }

    /**
     * Deletes a student from a specific course by their first name, last name, and
     * course name.
     *
     * @param firstName  the first name of the student
     * @param lastName   the last name of the student
     * @param courseName the name of the course
     * @return the number of rows deleted
     */
    @Override
    public int deleteStudentFromCourse(String firstName, String lastName, String courseName) {
        int rowsDeleted;

        try (Connection connection = connector.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    DELETE FROM students_courses
                    WHERE student_id = (SELECT student_id FROM students WHERE first_name = ?
                    AND last_name = ?)
                    AND course_id = (SELECT course_id FROM courses WHERE course_name = ?);
                    """);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, courseName);
            rowsDeleted = statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("An error occurred when deleting a student from the course.\n" + e.getMessage());
        }
        return rowsDeleted;
    }

}
