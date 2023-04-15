package ua.foxminded.schoolapp.dao;

import java.util.List;
import ua.foxminded.schoolapp.entity.Student;

public interface StudentDAO extends CrudDAO<Student> {

    List<Student> findAvailableStudents();

    List<Student> findStudentsRelatedToCourse(String courseName);

    int findStudentIdByNameAndGroupId(String firstName, String lastName, int groupId);

    void deleteStudentById(int studentId);

    boolean isStudentOnCourse(String firstName, String lastName, String courseName);

    void addStudentToCourse(String firstName, String lastName, String courseName);

    void deleteStudentFromCourse(String firstName, String lastName, String courseName);

}
