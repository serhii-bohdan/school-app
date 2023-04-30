package ua.foxminded.schoolapp.cli.controller;

import java.util.ArrayList;
import java.util.List;
import ua.foxminded.schoolapp.dao.CourseDAO;
import ua.foxminded.schoolapp.dao.GroupDAO;
import ua.foxminded.schoolapp.dao.StudentDAO;
import ua.foxminded.schoolapp.exception.InputException;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

public class InputValidator implements Validator {

    private StudentDAO studentDao;
    private GroupDAO groupDao;
    private CourseDAO courseDao;
    
    public InputValidator(StudentDAO studentDao, GroupDAO groupDao, CourseDAO courseDao) {
        this.studentDao = studentDao;
        this.groupDao = groupDao;
        this.courseDao = courseDao;
    }
    
    public List<Group> getGroupsWithGivenNumberStudents(int amountOfStudents) {
        List<Group> groups = new ArrayList<>();

        if (amountOfStudents >= 10 && amountOfStudents <= 30) {
            groups = groupDao.findGroupsWithGivenNumberStudents(amountOfStudents);
        } else {
            System.out.println("The entered number of students is not correct."
                    + "The number of students should be between 0 and 30 inclusive.");
        }
        return groups;
    }

    public List<Student> getStudentsRelatedToCourse(String courseName) {
        List<Student> students = new ArrayList<>();
        List<String> coursesNamesThatExist = courseDao.findAllCourses().stream()
                                                                       .map(Course::getCourseName)
                                                                       .toList();
        if (coursesNamesThatExist.contains(courseName.trim())) {
            students = studentDao.findStudentsRelatedToCourse(courseName.trim());
        } else {
            System.out.println("A course with that name does not exist.");
        }
        return students;
    }

    public void addNewStudent(String firstName, String lastName, int groupId) {
        Student student = new Student(firstName, lastName, groupId);
        List<Student> students = studentDao.findAllStudents();

        if(!students.contains(student)) {
            studentDao.save(student);
        } else {
            throw new InputException("Error adding new student.");
        }
    }

    public void deleteStudentById(int studentId) {
        List<Integer> studentIds = studentDao.findAllStudents().stream()
                                                               .map(studentDao::findStudentId)
                                                               .toList();
        if (studentIds.contains(studentId)) {
            studentDao.deleteStudentById(studentId);
            System.out.println("The student was successfully deleted.");
        } else {
            System.out.println("There is no student with this ID.");
        }
    }

    public void addStudentToCourse(String firstName, String lastName, String courseName) {
        if (!studentDao.isStudentOnCourse(firstName, lastName, courseName)) {
            studentDao.addStudentToCourse(firstName, lastName, courseName);
            System.out.println("The student has been successfully added to the course.");
        } else {
            System.out.println("The student was already registered for the course.");
        }
    }

    public void deleteStudentFromCourse(String firstName, String lastName, String courseName) {
        if (studentDao.isStudentOnCourse(firstName, lastName, courseName)) {
            studentDao.deleteStudentFromCourse(firstName, lastName, courseName);
            System.out.println("The student has been successfully removed from the course.");
        } else {
            System.out.println("There is no student registered for this course.");
        }
    }
}
