package ua.foxminded.schoolapp.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.CourseService;
import ua.foxminded.schoolapp.service.GroupService;
import ua.foxminded.schoolapp.service.ServiceFacade;
import ua.foxminded.schoolapp.service.StudentService;
import ua.foxminded.schoolapp.service.UserInputValidator;

/**
 * The ServiceFacadeImpl class is an implementation of the {@link ServiceFacade}
 * interface. It provides access to the school application's services and
 * functionalities, serving as a single entry point for managing groups,
 * students, and courses.
 * <p>
 * The class is annotated with {@code @Service} to indicate that it is a Spring
 * service, and it can be automatically discovered and registered as a bean in
 * the Spring context. The ServiceFacadeImpl requires instances of
 * {@link GroupService}, {@link StudentService}, {@link CourseService}, and
 * {@link UserInputValidator} to perform its operations.
 *
 * @author Serhii Bohdan
 */
@Service
public class ServiceFacadeImpl implements ServiceFacade {

    private GroupService groupService;
    private StudentService studentService;
    private CourseService courseService;
    private UserInputValidator validator;

    /**
     * Constructs a new ServiceFacadeImpl with the specified group service, student
     * service, course service, and user input validator.
     *
     * @param groupService   the group service to use
     * @param studentService the student service to use
     * @param courseService  the course service to use
     * @param validator      the user input validator to use
     * @throws NullPointerException if any of the service objects or the validator
     *                              is null
     */
    public ServiceFacadeImpl(GroupService groupService, StudentService studentService, CourseService courseService,
            UserInputValidator validator) {
        Objects.requireNonNull(groupService, "groupService must not be null");
        Objects.requireNonNull(studentService, "studentService must not be null");
        Objects.requireNonNull(courseService, "courseService must not be null");
        Objects.requireNonNull(validator, "validator must not be null");
        this.groupService = groupService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.validator = validator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Group, Integer> getGroupsWithGivenNumberOfStudents(int amountOfStudents) {
        Map<Group, Integer> groupsWithTheirNumberOfStudents = null;

        if (validator.validateAmountOfStudents(amountOfStudents)) {
            groupsWithTheirNumberOfStudents = groupService.getGroupsWithGivenNumberOfStudents(amountOfStudents);
        }

        return groupsWithTheirNumberOfStudents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Student, List<Course>> getStudentsWithCoursesByCourseName(String courseName) {
        Map<Student, List<Course>> studentsWithTheirCourses = null;

        if (validator.validateCourseName(courseName)) {
            studentsWithTheirCourses = studentService.getStudentsRelatedToCourse(courseName).stream().collect(
                    Collectors.toMap(student -> student, student -> courseService.getCoursesForStudent(student)));
        }

        return studentsWithTheirCourses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addNewStudent(String firstName, String lastName, int groupId) {
        boolean newStudentIsAdded = false;
        boolean studentIsNotExists = !validator.validateStudentFullName(firstName, lastName);

        if (studentIsNotExists && validator.validateGroupId(groupId)) {
            studentService.addStudent(firstName, lastName, groupId);
            newStudentIsAdded = true;
        }

        return newStudentIsAdded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteStudentById(int studentId) {
        boolean studentIsdDeleted = false;

        if (validator.validateStudentId(studentId)) {
            studentService.deleteStudent(studentId);
            studentIsdDeleted = true;
        }

        return studentIsdDeleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addStudentToCourse(String firstName, String lastName, String courseName) {
        boolean studentIsAddedToCourse = false;
        boolean studentExists = validator.validateStudentFullName(firstName, lastName);
        boolean courseExist = validator.validateCourseName(courseName);
        boolean studentNotOnCourse = !validator.isStudentOnCourse(firstName, lastName, courseName);

        if (studentExists && courseExist && studentNotOnCourse) {
            studentService.addStudentToCourse(firstName, lastName, courseName);
            studentIsAddedToCourse = true;
        }

        return studentIsAddedToCourse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteStudentFromCourse(String firstName, String lastName, String courseName) {
        boolean studentDeletedFromCourse = false;
        boolean studentExists = validator.validateStudentFullName(firstName, lastName);
        boolean coursesExist = validator.validateCourseName(courseName);
        boolean studentOnCourse = validator.isStudentOnCourse(firstName, lastName, courseName);

        if (studentExists && coursesExist && studentOnCourse) {
            studentService.deleteStudentFromCourse(firstName, lastName, courseName);
            studentDeletedFromCourse = true;
        }

        return studentDeletedFromCourse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Student getStudentById(int studentId) {
        Student student = null;

        if (validator.validateStudentId(studentId)) {
            student = studentService.getStudentById(studentId);
        }

        return student;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Student, List<Course>> getAllStudentsWithTheirCourses() {
        return studentService.getAllStudents().stream()
                .collect(Collectors.toMap(student -> student, student -> courseService.getCoursesForStudent(student)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

}
