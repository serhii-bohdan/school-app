package ua.foxminded.schoolapp.service.logic.impl;

import org.springframework.stereotype.Service;
import ua.foxminded.schoolapp.dao.CourseDao;
import ua.foxminded.schoolapp.dao.GroupDao;
import ua.foxminded.schoolapp.dao.StudentDao;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.logic.UserInputValidator;

/**
 * The UserInputValidatorImpl class is an implementation of the
 * {@link UserInputValidator} interface. It provides methods for validating user
 * input in the school application, including checking the amount of students,
 * validating course names, student names, group IDs, and student IDs.
 * <p>
 * The class is annotated with {@code @Service} to indicate that it is a Spring
 * service, and it can be automatically discovered and registered as a bean in
 * the Spring context. The UserInputValidatorImpl requires instances of
 * {@link GroupDao}, {@link StudentDao}, and {@link CourseDao} for data access
 * to perform its validation operations.
 *
 * @author Serhii Bohdan
 */
@Service
public class UserInputValidatorImpl implements UserInputValidator {

    private final GroupDao groupDao;
    private final StudentDao studentDao;
    private final CourseDao courseDao;

    /**
     * Constructs a new UserInputValidatorImpl with the specified group Dao, student
     * Dao, and course Dao.
     *
     * @param groupDao   the data access object for groups
     * @param studentDao the data access object for students
     * @param courseDao  the data access object for courses
     */
    public UserInputValidatorImpl(GroupDao groupDao, StudentDao studentDao, CourseDao courseDao) {
        this.groupDao = groupDao;
        this.studentDao = studentDao;
        this.courseDao = courseDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateAmountOfStudents(int amountOfStudents) {
        return amountOfStudents >= 10 && amountOfStudents <= 30;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateCourseName(String courseName) {
        return courseDao.findAll().stream()
                                  .map(Course::getCourseName)
                                  .toList().contains(courseName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateStudentFullName(String firstName, String lastName) {
        return studentDao.findAll().stream()
                                   .anyMatch(s -> s.getFirstName().equals(firstName) &&
                                           s.getLastName().equals(lastName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateGroupId(int groupId) {
        return groupDao.findAll().stream()
                                 .map(Group::getId)
                                 .toList().contains(groupId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateStudentId(int studentId) {
        return studentDao.findAll().stream()
                                   .map(Student::getId)
                                   .toList().contains(studentId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStudentOnCourse(String firstName, String lastName, String courseName) {
        return studentDao.isStudentOnCourse(firstName, lastName, courseName);
    }

}
