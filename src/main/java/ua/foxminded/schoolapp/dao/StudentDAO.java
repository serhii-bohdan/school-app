package ua.foxminded.schoolapp.dao;

import java.util.List;
import ua.foxminded.schoolapp.entity.Student;

public interface StudentDAO extends CRUD<Student> {

    List<Student> findAllStudents();

    List<Student> findStudentsRelatedToCourse(String courseName);

    int findStudentId(Student student);

    int deleteStudentById(int studentId);

    boolean isStudentOnCourse(String firstName, String lastName, String courseName);

    int addStudentToCourse(String firstName, String lastName, String courseName);

    int deleteStudentFromCourse(String firstName, String lastName, String courseName);

}
