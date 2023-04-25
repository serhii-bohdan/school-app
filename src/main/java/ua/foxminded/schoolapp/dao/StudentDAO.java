package ua.foxminded.schoolapp.dao;

import java.util.List;
import ua.foxminded.schoolapp.entity.Student;

public interface StudentDAO extends CRUD<Student> {

    List<Student> findAvailableStudents();

    List<Student> findStudentsRelatedToCourse(String courseName);

    int findStudentId(Student student);

    boolean isStudentOnCourse(String firstName, String lastName, String courseName);

    void deleteStudentById(int studentId);

    void addStudentToCourse(String firstName, String lastName, String courseName);

    void deleteStudentFromCourse(String firstName, String lastName, String courseName);

}
