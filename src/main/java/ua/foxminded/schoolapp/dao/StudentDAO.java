package ua.foxminded.schoolapp.dao;

import java.util.List;

import ua.foxminded.schoolapp.model.Student;

public interface StudentDAO extends DAO<Student> {

    List<Student> findAllStudents();

    List<Student> findStudentsRelatedToCourse(String courseName);

    int findStudentId(Student student);

    int deleteStudentById(int studentId);

    boolean isStudentOnCourse(String firstName, String lastName, String courseName);

    int addStudentToCourse(String firstName, String lastName, String courseName);

    int deleteStudentFromCourse(String firstName, String lastName, String courseName);

}
