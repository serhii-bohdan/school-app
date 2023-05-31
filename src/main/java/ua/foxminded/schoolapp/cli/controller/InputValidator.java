package ua.foxminded.schoolapp.cli.controller;

import java.util.List;
import ua.foxminded.schoolapp.dao.CourseDao;
import ua.foxminded.schoolapp.dao.GroupDao;
import ua.foxminded.schoolapp.dao.StudentDao;
import ua.foxminded.schoolapp.exception.InputException;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

public class InputValidator implements Validator {

    private StudentDao studentDao;
    private GroupDao groupDao;
    private CourseDao courseDao;

    public InputValidator(StudentDao studentDao, GroupDao groupDao, CourseDao courseDao) {
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
        boolean courseExists = courseDao.findAllCourses().stream()
                                                         .map(Course::getCourseName)
                                                         .toList().contains(courseName);
        if (courseExists) {
            students = studentDao.findStudentsRelatedToCourse(courseName);
        } else {
            throw new InputException("A course with that name does not exist.");
        }
        return students;
    }

    public void addNewStudent(String firstName, String lastName, int groupId) {
        boolean studentIsNotExists = !studentDao.findAllStudents().stream()
                                                                  .anyMatch(s -> firstName.equals(s.getFirstName()) && 
                                                                          lastName.equals(s.getLastName()));

        if(studentIsNotExists && (groupId >= 1 && groupId <= 10)) {
            studentDao.save(new Student(firstName, lastName, groupId));
        } else {
            throw new InputException("Error adding new student. Perhaps a student with such data is "
                    + "already registered. Also check that the group ID is correct.");
        }
    }

    public void deleteStudentById(int studentId) {
        boolean studentIdExists = studentDao.findAllStudents().stream()
                                                               .map(Student::getId)
                                                               .toList().contains(studentId);
        if (studentIdExists) {
            studentDao.deleteStudentById(studentId);
        } else {
            throw new InputException("There is no student with this ID.");
        }
    }

    public void addStudentToCourse(String firstName, String lastName, String courseName) {
        boolean studentExists = studentDao.findAllStudents().stream()
                                                            .anyMatch(s -> firstName.equals(s.getFirstName()) && 
                                                                    lastName.equals(s.getLastName()));
        boolean coursesExist = courseDao.findAllCourses().stream()
                                                         .map(Course::getCourseName)
                                                         .toList().contains(courseName);
        boolean  studentIsNotOnCourse = !studentDao.isStudentOnCourse(firstName, lastName, courseName);

        if (studentExists && coursesExist && studentIsNotOnCourse) {
            studentDao.addStudentToCourse(firstName, lastName, courseName);
        } else {
            throw new InputException("An error occurred when adding a student to the additional course. Perhaps "
                    + "this student does not exist, or he is already registered for this course. Also, check "
                    + "whether the name of the course is entered correctly.");
        }
    }

    public void deleteStudentFromCourse(String firstName, String lastName, String courseName) {
        boolean studentExists = studentDao.findAllStudents().stream()
                                                            .anyMatch(s -> firstName.equals(s.getFirstName()) && 
                                                                    lastName.equals(s.getLastName()));
        boolean coursesExist = courseDao.findAllCourses().stream()
                                                         .map(Course::getCourseName)
                                                         .toList().contains(courseName);
        boolean  studentOnCourse = studentDao.isStudentOnCourse(firstName, lastName, courseName);

        if (studentExists && coursesExist && studentOnCourse) {
            studentDao.deleteStudentFromCourse(firstName, lastName, courseName);
        } else {
            throw new InputException("An error occurred when removing a student from the course. Perhaps this "
                    + "student does not exist or is not registered in the specified course. Also check the "
                    + "correctness of the entered course name.");
        }
    }

    public Student findStudentById(int studentId) {
        boolean studentIdExists = studentDao.findAllStudents().stream()
                                                               .map(Student::getId)
                                                               .toList().contains(studentId);
        if (studentIdExists) {
            return studentDao.findStudentById(studentId);
        } else {
            throw new InputException("There is no student with this ID.");
        }
    }

    public List<Student> getAllStudents() {
        return studentDao.findAllStudents();
    }

}
