package ua.foxminded.schoolapp.service.logic.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /**
     * The logger for logging events and messages in the
     * {@link UserInputValidatorImpl} class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserInputValidatorImpl.class);

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
        LOGGER.debug("Validating amount of students: {}", amountOfStudents);
        boolean isValid = amountOfStudents >= 10 && amountOfStudents <= 30;

        LOGGER.debug("Amount of students validation result: {}", isValid);
        return isValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateCourseName(String courseName) {
        LOGGER.debug("Validating course name: {}", courseName);
        boolean isValid = courseDao.findAll().stream()
                .map(Course::getCourseName)
                .toList().contains(courseName);

        LOGGER.debug("Course name validation result: {}", isValid);
        return isValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateStudentFullName(String firstName, String lastName) {
        LOGGER.debug("Validating student full name: {} {}", firstName, lastName);
        boolean isValid = studentDao.findAll().stream()
                .anyMatch(s -> s.getFirstName().equals(firstName)
                        && s.getLastName().equals(lastName));

        LOGGER.debug("Student full name validation result: {}", isValid);
        return isValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateGroupId(int groupId) {
        LOGGER.debug("Validating group ID: {}", groupId);
        boolean isValid = groupDao.findAll().stream()
                .map(Group::getId)
                .toList().contains(groupId);

        LOGGER.debug("Group ID validation result: {}", isValid);
        return isValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateStudentId(int studentId) {
        LOGGER.debug("Validating student ID: {}", studentId);
        boolean isValid = studentDao.findAll().stream()
                .map(Student::getId)
                .toList().contains(studentId);

        LOGGER.debug("Student ID validation result: {}", isValid);
        return isValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStudentOnCourse(String firstName, String lastName, String courseName) {
        LOGGER.debug("Checking if student is on course: {} {} - {}", firstName, lastName, courseName);
        boolean isOnCourse = studentDao.isStudentOnCourse(firstName, lastName, courseName);

        LOGGER.debug("Student on course check result: {}", isOnCourse);
        return isOnCourse;
    }

}
