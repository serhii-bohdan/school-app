package ua.foxminded.schoolapp.service;

import java.util.List;
import java.util.Objects;
import ua.foxminded.schoolapp.dao.CourseDao;
import ua.foxminded.schoolapp.dao.GroupDao;
import ua.foxminded.schoolapp.dao.StudentDao;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

public class SchoolService implements Service {

    private StudentDao studentDao;
    private GroupDao groupDao;
    private CourseDao courseDao;

    public SchoolService(StudentDao studentDao, GroupDao groupDao, CourseDao courseDao) {
        Objects.requireNonNull(studentDao);
        Objects.requireNonNull(groupDao);
        Objects.requireNonNull(courseDao);
        this.studentDao = studentDao;
        this.groupDao = groupDao;
        this.courseDao = courseDao;
    }

    public List<Group> getGroupsWithGivenNumberStudents(int amountOfStudents) {
        List<Group> groups = null;

        if (amountOfStudents >= 10 && amountOfStudents <= 30) {
            groups = groupDao.findGroupsWithGivenNumberStudents(amountOfStudents);
        } 

        return groups;
    }

    public List<Student> getStudentsRelatedToCourse(String courseName) {
        List<Student> students = null;
        boolean courseExists = courseDao.findAllCourses().stream()
                                                         .map(Course::getCourseName)
                                                         .toList().contains(courseName);
        if (courseExists) {
            students = studentDao.findStudentsRelatedToCourse(courseName);
        } 

        return students;
    }

    public boolean addNewStudent(String firstName, String lastName, int groupId) {
        boolean newStudentIsAdded = false;
        boolean studentIsNotExists = !studentDao.findAllStudents().stream()
                                                                  .anyMatch(s -> firstName.equals(s.getFirstName()) && 
                                                                          lastName.equals(s.getLastName()));

        if(studentIsNotExists && (groupId >= 1 && groupId <= 10)) {
            studentDao.save(new Student(firstName, lastName, groupId));
            newStudentIsAdded = true;
        } 

        return newStudentIsAdded;
    }

    public boolean deleteStudentById(int studentId) {
        boolean studentIsdDeleted = false;
        boolean studentIdExists = studentDao.findAllStudents().stream()
                                                              .map(Student::getId)
                                                              .toList().contains(studentId);
        if (studentIdExists) {
            studentDao.deleteStudentById(studentId);
            studentIsdDeleted = true;
        }

        return studentIsdDeleted;
    }

    public boolean addStudentToCourse(String firstName, String lastName, String courseName) {
        boolean studentIsAddedToCourse = false;
        boolean studentExists = studentDao.findAllStudents().stream()
                                                            .anyMatch(s -> firstName.equals(s.getFirstName()) && 
                                                                    lastName.equals(s.getLastName()));
        boolean coursesExist = courseDao.findAllCourses().stream()
                                                         .map(Course::getCourseName)
                                                         .toList().contains(courseName);
        boolean  studentIsNotOnCourse = !studentDao.isStudentOnCourse(firstName, lastName, courseName);

        if (studentExists && coursesExist && studentIsNotOnCourse) {
            studentDao.addStudentToCourse(firstName, lastName, courseName);
            studentIsAddedToCourse = true;
        } 

        return studentIsAddedToCourse;
    }

    public boolean deleteStudentFromCourse(String firstName, String lastName, String courseName) {
        boolean studentDeletedFromCourse = false;
        boolean studentExists = studentDao.findAllStudents().stream()
                                                            .anyMatch(s -> firstName.equals(s.getFirstName()) && 
                                                                    lastName.equals(s.getLastName()));
        boolean coursesExist = courseDao.findAllCourses().stream()
                                                         .map(Course::getCourseName)
                                                         .toList().contains(courseName);
        boolean  studentOnCourse = studentDao.isStudentOnCourse(firstName, lastName, courseName);

        if (studentExists && coursesExist && studentOnCourse) {
            studentDao.deleteStudentFromCourse(firstName, lastName, courseName);
            studentDeletedFromCourse = true;
        }

        return studentDeletedFromCourse;
    }

    public Student getStudentById(int studentId) {
        Student student = null;
        boolean studentIdExists = studentDao.findAllStudents().stream()
                                                              .map(Student::getId)
                                                              .toList().contains(studentId);
        if (studentIdExists) {
            student = studentDao.findStudentById(studentId);
        }

        return student;
    }

    public List<Student> getAllStudents() {
        return studentDao.findAllStudents();
    }

}
