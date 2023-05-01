package ua.foxminded.schoolapp.cli.controller;

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
        List<Group> groups;

        if (amountOfStudents >= 10 && amountOfStudents <= 30) {
            groups = groupDao.findGroupsWithGivenNumberStudents(amountOfStudents);
        } else {
            throw new InputException("The entered number of students is not correct."
                    + "The number of students should be between 0 and 30 inclusive.");
        }
        return groups;
    }

    public List<Student> getStudentsRelatedToCourse(String courseName) {
        List<Student> students;
        boolean coursesExist = courseDao.findAllCourses().stream()
                                                         .map(Course::getCourseName)
                                                         .toList().contains(courseName);
        if (coursesExist) {
            students = studentDao.findStudentsRelatedToCourse(courseName);
        } else {
            throw new InputException("A course with that name does not exist.");
        }
        return students;
    }

    public void addNewStudent(String firstName, String lastName, int groupId) {
        Student student = new Student(firstName, lastName, groupId);
        List<Student> students = studentDao.findAllStudents();

        if(!students.contains(student) && (groupId >= 1 && groupId <= 10)) {
            studentDao.save(student);
        } else {
            throw new InputException("Error adding new student. Perhaps a student with such data is "
                    + "already registered. Also check that the group ID is correct.");
        }
    }

    public Student findStudentById(int studentId) {
        List<Integer> studentIds = studentDao.findAllStudents().stream()
                                                               .map(Student::getId)
                                                               .toList();
        if (studentIds.contains(studentId)) {
            return studentDao.findStudentById(studentId);
        } else {
            throw new InputException("There is no student with this ID.");
        }
    }

    public void deleteStudentById(int studentId) {
        List<Integer> studentIds = studentDao.findAllStudents().stream()
                                                               .map(Student::getId)
                                                               .toList();
        if (studentIds.contains(studentId)) {
            studentDao.deleteStudentById(studentId);
        } else {
            throw new InputException("There is no student with this ID.");
        }
    }
    
    public List<Student> getAllAvailableStudents() {
        return studentDao.findAllStudents();
    }

    public void addStudentToCourse(String firstName, String lastName, String courseName) {
        boolean studentExists = studentDao.findAllStudents().stream()
                .anyMatch(s -> firstName.equals(s.getFirstName()) && lastName.equals(s.getLastName()));
        boolean coursesExist = courseDao.findAllCourses().stream()
                                                         .map(Course::getCourseName)
                                                         .toList().contains(courseName);
        boolean  studentIsNotRegisteredForCourse = !studentDao.isStudentOnCourse(firstName, lastName, courseName);
        
        if (studentExists && coursesExist && studentIsNotRegisteredForCourse) {
            studentDao.addStudentToCourse(firstName, lastName, courseName);
        } else {
            throw new InputException("An error occurred when adding a student to the additional course. Perhaps "
                    + "this student does not exist, or he is already registered for this course. Also, check "
                    + "whether the name of the course is entered correctly.");
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
