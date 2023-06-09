package ua.foxminded.schoolapp.service;

import java.util.List;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

public interface Service {

    List<Group> getGroupsWithGivenNumberStudents(int amountOfStudents);

    List<Student> getStudentsRelatedToCourse(String courseName);

    void addNewStudent(String firstName, String lastName, int groupId);

    void deleteStudentById(int studentId);

    void addStudentToCourse(String firstName, String lastName, String courseName);

    void deleteStudentFromCourse(String firstName, String lastName, String courseName);

    Student findStudentById(int studentId);

    List<Student> getAllStudents();

}
