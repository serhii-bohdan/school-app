package ua.foxminded.schoolapp.cli;

import ua.foxminded.schoolapp.dao.GroupDAO;
import ua.foxminded.schoolapp.dao.CourseDAO;
import ua.foxminded.schoolapp.dao.implement.CourseDAOImpl;
import ua.foxminded.schoolapp.dao.implement.GroupDAOImpl;
import ua.foxminded.schoolapp.dao.StudentDAO;
import ua.foxminded.schoolapp.dao.implement.StudentDAOImpl;
import ua.foxminded.schoolapp.entity.Course;
import ua.foxminded.schoolapp.entity.Group;
import ua.foxminded.schoolapp.entity.Student;
import java.util.ArrayList;
import java.util.List;

public class ConsoleManager {

    public String getGroupsWithGivenNumberStudents(int amountOfStudents) {
        GroupDAO groupDao = new GroupDAOImpl();
        StringBuilder result = new StringBuilder();
        List<Group> groups = new ArrayList<>();

        if (amountOfStudents >= 0 && amountOfStudents <= 30) {
            groups = groupDao.findGroupsWithGivenNumberStudents(amountOfStudents);
        } else {
          System.out.println("The entered number of students is not correct."
                    + "The number of students should be between 0 and 30 inclusive.");
        }

        for (Group group : groups) {
            result.append(group.getName() + "\n");
        }
        return result.toString();
    }

    public String getStudentsRelatedToCourse(String courseName) {
        CourseDAO courseDao = new CourseDAOImpl();
        StudentDAO stuentDao = new StudentDAOImpl();
        StringBuilder result = new StringBuilder();
        List<Student> students = new ArrayList<>();
        List<String> coursesNamesThatExist = courseDao.findAvailableCourses().stream()
                                                                             .map(Course::getName)
                                                                             .toList();
        if (coursesNamesThatExist.contains(courseName.trim())) {
            students = stuentDao.findStudentsRelatedToCourse(courseName.trim());
        } else {
            System.out.println("A course with that name does not exist.");
        }

        for (Student student : students) {
            result.append(student.getFirstName() + " " + student.getLastName() + "\n");
        }
        return result.toString();
    }

    public void addNewStudent(String firstName, String lastName, String groupName) {
        StudentDAO studentDao = new StudentDAOImpl();
        GroupDAO groupDao = new GroupDAOImpl();
        Student student = new Student(groupDao.findGroupIdByGroupName(groupName), firstName, lastName);
        studentDao.save(student);
    }

    public void deleteStudentById(int studentId) {
        StudentDAO studentDao = new StudentDAOImpl();
        List<Integer> studentIds = studentDao.findAvailableStudents().stream()
                                                                     .map(Student::getStudentId)
                                                                     .toList();
        if (studentIds.contains(studentId)) {
            studentDao.deleteStudentById(studentId);
            System.out.println("The student was successfully deleted.");
        } else {
            System.out.println("There is no student with this ID.");
        }
    }

    public void addStudentToCourse(String firstName, String lastName, String courseName) {
        StudentDAO studentDao = new StudentDAOImpl();

        if (!studentDao.isStudentOnCourse(firstName, lastName, courseName)) {
            studentDao.addStudentToCourse(firstName, lastName, courseName);
            System.out.println("The student has been successfully added to the course.");
        } else {
            System.out.println("The student was already registered for the course.");
        }
    }

    public void deleteStudentFromCourse(String firstName, String lastName, String courseName) {
        StudentDAO studentDao = new StudentDAOImpl();

        if (studentDao.isStudentOnCourse(firstName, lastName, courseName)) {
            studentDao.deleteStudentFromCourse(firstName, lastName, courseName);
            System.out.println("The student has been successfully removed from the course.");
        } else {
            System.out.println("There is no student registered for this course.");
        }
    }

}
