package ua.foxminded.schoolapp.service;

import java.util.List;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

public interface Service {

    List<Group> getGroupsWithGivenNumberStudents(int amountOfStudents);

    List<Student> getStudentsRelatedToCourse(String courseName);

    boolean addNewStudent(String firstName, String lastName, int groupId);

    boolean deleteStudentById(int studentId);

    boolean addStudentToCourse(String firstName, String lastName, String courseName);

    boolean deleteStudentFromCourse(String firstName, String lastName, String courseName);

    Student getStudentById(int studentId);

    List<Student> getAllStudents();

}
