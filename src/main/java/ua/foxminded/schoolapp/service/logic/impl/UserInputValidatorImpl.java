package ua.foxminded.schoolapp.service.logic.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.repository.CourseRepository;
import ua.foxminded.schoolapp.repository.GroupRepository;
import ua.foxminded.schoolapp.repository.StudentRepository;
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
 * {@link GroupRepository}, {@link StudentRepository}, and
 * {@link CourseRepository} for data access to perform its validation
 * operations.
 *
 * @author Serhii Bohdan
 */
@Service
@Transactional
public class UserInputValidatorImpl implements UserInputValidator {

    /**
     * The logger for logging events and messages in the
     * {@link UserInputValidatorImpl} class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserInputValidatorImpl.class);

    /**
     * A regular expression pattern for validating group names.
     */
    private static final String GROUP_NAME_PATTERN = "^[A-Z]{2}-[0-9]{2}$";

    /**
     * The maximum length allowed for names of entities.
     */
    private static final Integer MAX_NAME_LENGTH = 25;

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    /**
     * Constructs a new UserInputValidatorImpl with access to the specified
     * repositories.
     *
     * @param groupRepository   an instance of {@link GroupRepository} for accessing
     *                          group data
     * @param studentRepository an instance of {@link StudentRepository} for
     *                          accessing student data
     * @param courseRepository  an instance of {@link CourseRepository} for
     *                          accessing course data
     */
    public UserInputValidatorImpl(GroupRepository groupRepository, StudentRepository studentRepository,
            CourseRepository courseRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateAmountOfStudents(Integer amountOfStudents) {
        LOGGER.debug("Validating amount of students: {}", amountOfStudents);
        boolean isValid = amountOfStudents >= 0;

        LOGGER.debug("Amount of students validation result: {}", isValid);
        return isValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateGroupId(Integer groupId) {
        LOGGER.debug("Validating group ID: {}", groupId);
        boolean isValid = groupRepository.findAll().stream()
                .map(Group::getId)
                .toList().contains(groupId);

        LOGGER.debug("Group ID validation result: {}", isValid);
        return isValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateGroupNameExistence(String groupName) {
        LOGGER.debug("Validation of the presence of a group named: {}", groupName);
        boolean groupNameExist = groupRepository.findAll().stream()
                .map(Group::getGroupName)
                .toList().contains(groupName);

        LOGGER.debug("A group named {} exists: {}", groupName, groupNameExist);
        return groupNameExist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateGroupNamePattern(String groupName) {
        LOGGER.debug("Validation of the group name according to the pattern: {}", GROUP_NAME_PATTERN);
        boolean groupNameMatchesPattern = groupName.matches(GROUP_NAME_PATTERN);

        LOGGER.debug("The group name matches the pattern: {}", groupNameMatchesPattern);
        return groupNameMatchesPattern;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateCourseName(String courseName) {
        LOGGER.debug("Validating course name: {}", courseName);
        boolean isValid = courseRepository.findAll().stream()
                .map(Course::getCourseName)
                .toList().contains(courseName);

        LOGGER.debug("Course name validation result: {}", isValid);
        return isValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateDescription(String courseDescription) {
        LOGGER.debug("Validating course descrition: {}", courseDescription);
        boolean isValid = courseRepository.findAll().stream()
                .map(Course::getDescription)
                .toList().contains(courseDescription);

        LOGGER.debug("Course descrition validation result: {}", isValid);
        return isValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateStudentId(Integer studentId) {
        LOGGER.debug("Validating student ID: {}", studentId);
        boolean isValid = studentRepository.findAll().stream()
                .map(Student::getId)
                .toList().contains(studentId);

        LOGGER.debug("Student ID validation result: {}", isValid);
        return isValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateStudentFullName(String firstName, String lastName) {
        LOGGER.debug("Validating student full name: {} {}", firstName, lastName);
        boolean isValid = studentRepository.findAll().stream()
                .anyMatch(s -> s.getFirstName().equals(firstName)
                        && s.getLastName().equals(lastName));

        LOGGER.debug("Student full name validation result: {}", isValid);
        return isValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStudentOnCourse(String firstName, String lastName, String courseName) {
        LOGGER.debug("Checking if student is on course: {} {} - {}", firstName, lastName, courseName);
        Optional<Student> student = studentRepository.findByFirstNameAndLastName(firstName, lastName);
        Optional<Course> course = courseRepository.findByCourseName(courseName);
        boolean isOnCourse = false;

        if (student.isPresent() && course.isPresent()) {
            isOnCourse = student.get().getCourses().contains(course.get());
        }

        LOGGER.debug("Student on course check result: {}", isOnCourse);
        return isOnCourse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateNameLength(String name) {
        LOGGER.debug("Validating word length: {}", name);
        boolean isValid = name.length() <= MAX_NAME_LENGTH;

        LOGGER.debug("Word validation result: {}", isValid);
        return isValid;
    }

}
