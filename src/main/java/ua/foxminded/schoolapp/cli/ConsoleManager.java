package ua.foxminded.schoolapp.cli;

import ua.foxminded.schoolapp.dao.GroupDAO;
import ua.foxminded.schoolapp.dao.CourseDAO;
import ua.foxminded.schoolapp.dao.implement.CourseDAOImpl;
import ua.foxminded.schoolapp.dao.implement.GroupDAOImpl;
import ua.foxminded.schoolapp.dao.StudentDAO;
import ua.foxminded.schoolapp.dao.implement.StudentDAOImpl;
import ua.foxminded.schoolapp.datageneration.Reader;
import ua.foxminded.schoolapp.entity.Group;
import ua.foxminded.schoolapp.entity.Student;
import ua.foxminded.schoolapp.exception.InputException;
import java.util.List;

public class ConsoleManager {

    private Reader reader = new Reader();

    public String getGroupsWithGivenNumberStudents(int amountOfStudents) {
        GroupDAO groupDao = new GroupDAOImpl();
        StringBuilder result = new StringBuilder();
        List<Group> groups;

        if (amountOfStudents >= 0 && amountOfStudents <= 30) {
            groups = groupDao.findGroupsWithGivenNumberStudents(amountOfStudents);
        } else {
            throw new InputException("The entered number of students is not correct."
                    + "The number of students should be between 0 and 30 inclusive.");
        }

        for (Group group : groups) {
            result.append(group.getName() + "\n");
        }
        return result.toString();
    }

    public String getStudentsRelatedToCourse(String courseName) {
        CourseDAO courseDao = new CourseDAOImpl();
        List<String> groupsNamesThatExist = reader.readFileAndPopulateList("courses/courses.txt");
        StringBuilder result = new StringBuilder();
        List<Student> students;

        if (groupsNamesThatExist.contains(courseName)) {
            students = courseDao.findStudentsRelatedToCourse(courseName);
        } else {
            throw new InputException("A course with that name does not exist.");
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

}
