package ua.foxminded.schoolapp.service.logic.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import ua.foxminded.schoolapp.dto.CourseDto;
import ua.foxminded.schoolapp.dto.GroupDto;
import ua.foxminded.schoolapp.dto.StudentDto;
import ua.foxminded.schoolapp.dto.mapper.CourseMapper;
import ua.foxminded.schoolapp.dto.mapper.GroupMapper;
import ua.foxminded.schoolapp.dto.mapper.StudentMapper;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.logic.CourseService;
import ua.foxminded.schoolapp.service.logic.GroupService;
import ua.foxminded.schoolapp.service.logic.ServiceFacade;
import ua.foxminded.schoolapp.service.logic.StudentService;
import ua.foxminded.schoolapp.service.logic.UserInputValidator;

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
@Transactional
public class ServiceFacadeImpl implements ServiceFacade {

    /**
     * The logger for logging events and messages in the {@link ServiceFacadeImpl}
     * class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceFacadeImpl.class);

    private final GroupService groupService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final UserInputValidator validator;

    /**
     * Constructs a new ServiceFacadeImpl with the specified group service, student
     * service, course service, and user input validator.
     *
     * @param groupService   the group service to use
     * @param studentService the student service to use
     * @param courseService  the course service to use
     * @param validator      the user input validator to use
     */
    public ServiceFacadeImpl(GroupService groupService, StudentService studentService, CourseService courseService,
            UserInputValidator validator) {
        this.groupService = groupService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.validator = validator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initSchema() {
        boolean groupsTableIsEmpty = groupService.getAllGroups().isEmpty();
        boolean studentsTableIsEmpty = studentService.getAllStudents().isEmpty();
        boolean coursesTableIsEmpty = courseService.getAllCourses().isEmpty();

        if (groupsTableIsEmpty && studentsTableIsEmpty && coursesTableIsEmpty) {
            LOGGER.info("Filling database tables with generated data");
            groupService.initGroups();
            studentService.initStudents(groupService.getAllGroups());
            courseService.initCourses();
            addStudentsToCourses();
        } else {
            LOGGER.info("The database tables are already full");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addNewGroup(String groupName) {
        boolean groupNameNotExist = !validator.validateGroupNameExistence(groupName);
        boolean groupNameMatchesPattern = validator.validateGroupNamePattern(groupName);
        boolean newGroupAdded = false;

        if (groupNameNotExist && groupNameMatchesPattern) {
            GroupDto newGroup = new GroupDto(groupName);
            LOGGER.debug("Adding new group {}", newGroup);
            groupService.addGroup(newGroup);
            newGroupAdded = true;
        }

        return newGroupAdded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GroupDto getGroupByName(String groupName) {
        boolean groupNameExist = validator.validateGroupNameExistence(groupName);
        GroupDto groupDto = null;

        if (groupNameExist) {
            Group group = groupService.getGroupByName(groupName).get();
            groupDto = GroupMapper.mapGroupToDto(group);
        }

        LOGGER.debug("Received group by name {}: {}", groupName, groupDto);
        return groupDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GroupDto> getAllGroups() {
        return groupService.getAllGroups().stream()
                .map(GroupMapper::mapGroupToDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateGroup(String groupNameToUpdate, String newGroupName) {
        Optional<Group> groupOptional = groupService.getGroupByName(groupNameToUpdate);
        boolean newGroupNameNotExist = !validator.validateGroupNameExistence(newGroupName);
        boolean newGroupNameMatchesPattern = validator.validateGroupNamePattern(newGroupName);
        boolean groupIsUpdated = false;

        if (groupOptional.isPresent() && newGroupNameNotExist && newGroupNameMatchesPattern) {
            Group groupToUpdate = groupOptional.get();
            groupToUpdate.setGroupName(newGroupName);
            LOGGER.debug("Group updating. Updated group: {}", groupToUpdate);
            groupService.updateGroup(groupToUpdate);
            groupIsUpdated = true;
        }

        return groupIsUpdated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteGroupByName(String groupName) {
        boolean groupNameExist = validator.validateGroupNameExistence(groupName);
        boolean groupIsDeleted = false;

        if (groupNameExist) {
            LOGGER.debug("Deleting group with name: {}", groupName);
            groupService.deleteGroupByName(groupName);
            groupIsDeleted = true;
        }

        return groupIsDeleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addNewStudent(String firstName, String lastName, String groupName) {
        boolean studentIsNotExists = !validator.validateStudentFullName(firstName, lastName);
        boolean firstNameLengthIsValid = validator.validateNameLength(firstName);
        boolean lastNameLengthIsValid = validator.validateNameLength(lastName);
        boolean groupIsExists = validator.validateGroupNameExistence(groupName);
        boolean newStudentIsAdded = false;

        if (studentIsNotExists && firstNameLengthIsValid && lastNameLengthIsValid && groupIsExists) {
            Group group = groupService.getGroupByName(groupName).get();
            StudentDto newStudent = new StudentDto(firstName, lastName, group);
            LOGGER.debug("Adding new student: {}", newStudent);
            studentService.addStudent(newStudent);
            newStudentIsAdded = true;
        }

        return newStudentIsAdded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentDto getStudentById(Integer studentId) {
        boolean studentIdExist = validator.validateStudentId(studentId);
        StudentDto studentDto = null;

        if (studentIdExist) {
            Student student = studentService.getStudentById(studentId).get();
            studentDto = StudentMapper.mapStudentToDto(student);
        }

        LOGGER.debug("Received student by ID {}: {}", studentId, studentDto);
        return studentDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateStudent(String studentFirstNameToUpdate, String studentLastNameToUpdate, String newFirstName,
            String newLastName, String newGroupName) {
        Optional<Student> studentOptional = studentService.getStudentByFullName(studentFirstNameToUpdate,
                studentLastNameToUpdate);
        boolean newStudentFullNameNotExist = !validator.validateStudentFullName(newFirstName, newLastName);
        boolean newFirstNameLengthIsValid = validator.validateNameLength(newFirstName);
        boolean newLastNameLengthIsValid = validator.validateNameLength(newLastName);
        Optional<Group> groupOptional = groupService.getGroupByName(newGroupName);
        boolean studentIsUpdated = false;

        if (studentFirstNameToUpdate.equals(newFirstName) && studentLastNameToUpdate.equals(newLastName)) {
            newStudentFullNameNotExist = !newStudentFullNameNotExist;
        }

        if (studentOptional.isPresent() && newStudentFullNameNotExist && newFirstNameLengthIsValid
                && newLastNameLengthIsValid && groupOptional.isPresent()) {
            Student updatedStudent = studentOptional.get();
            Group group = groupOptional.get();
            updatedStudent.setFirstName(newFirstName);
            updatedStudent.setLastName(newLastName);
            updatedStudent.setGroup(group);
            LOGGER.debug("Student updating. Updated student: {}", updatedStudent);
            studentService.updateStudent(updatedStudent);
            studentIsUpdated = true;
        }

        return studentIsUpdated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteStudentById(Integer studentId) {
        boolean studentIdExist = validator.validateStudentId(studentId);
        boolean studentIsdDeleted = false;

        if (studentIdExist) {
            studentService.deleteStudentById(studentId);
            studentIsdDeleted = true;
        }

        LOGGER.debug("Student with ID {} is deleted: {}", studentId, studentIsdDeleted);
        return studentIsdDeleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addNewCourse(String courseName, String description) {
        boolean courseNameNotExist = !validator.validateCourseName(courseName);
        boolean courseNameLengthIsValid = validator.validateNameLength(courseName);
        boolean descriptionNotExist = !validator.validateDescription(description);
        boolean newCourseIsAdded = false;

        if (courseNameNotExist && courseNameLengthIsValid && descriptionNotExist) {
            CourseDto newCourse = new CourseDto(courseName, description);
            LOGGER.debug("Adding new course {}", newCourse);
            courseService.addCourse(newCourse);
            newCourseIsAdded = true;
        }

        return newCourseIsAdded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CourseDto getCourseByName(String courseName) {
        boolean courseNameExist = validator.validateCourseName(courseName);
        CourseDto courseDto = null;

        if (courseNameExist) {
            Course course = courseService.getCourseByName(courseName).get();
            courseDto = CourseMapper.mapCourseToDto(course);
        }

        LOGGER.debug("Received course by name {}: {}", courseName, courseDto);
        return courseDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CourseDto> getAllCourses() {
        return courseService.getAllCourses().stream()
                .map(CourseMapper::mapCourseToDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateCourse(String courseNameToUpdate, String newCourseName, String newDescription) {
        Optional<Course> courseOptional = courseService.getCourseByName(courseNameToUpdate);
        boolean courseIsPresent = courseOptional.isPresent();
        boolean newCourseNameNotExist = !validator.validateCourseName(newCourseName);
        boolean newCourseNameLengthIsValid = validator.validateNameLength(newCourseName);
        boolean newDescriptionNotExist = !validator.validateDescription(newDescription);
        boolean courseIsUdated = false;

        if (courseIsPresent && newCourseName.equals(courseOptional.get().getCourseName())) {
            newCourseNameNotExist = !newCourseNameNotExist;
        }

        if (courseIsPresent && newDescription.equals(courseOptional.get().getDescription())) {
            newDescriptionNotExist = !newDescriptionNotExist;
        }

        if (courseIsPresent && newCourseNameNotExist && newCourseNameLengthIsValid && newDescriptionNotExist) {
            Course course = courseOptional.get();
            course.setCourseName(newCourseName);
            course.setDescription(newDescription);
            LOGGER.debug("Course updating. Updated course: {}", course);
            courseService.updateCourse(course);
            courseIsUdated = true;
        }

        return courseIsUdated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteCourseByName(String courseName) {
        boolean courseNameExist = validator.validateCourseName(courseName);
        boolean courseIsDeleted = false;

        if (courseNameExist) {
            courseService.deleteCourseByName(courseName);
            courseIsDeleted = true;
        }

        LOGGER.debug("Curse with name {} is deleted: {}", courseName, courseIsDeleted);
        return courseIsDeleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<GroupDto, Integer> getGroupsWithGivenNumberOfStudents(Integer amountOfStudents) {
        boolean amountOfStudentsIsValid = validator.validateAmountOfStudents(amountOfStudents);
        Map<GroupDto, Integer> groupsWithTheirNumberOfStudents = null;

        if (amountOfStudentsIsValid) {
            groupsWithTheirNumberOfStudents = groupService.getGroupsWithGivenNumberOfStudents(amountOfStudents).stream()
                    .collect(Collectors.toMap(GroupMapper::mapGroupToDto, group -> group.getStudents().size()));
        }

        LOGGER.debug("Received groups with a given number of students: {}", groupsWithTheirNumberOfStudents);
        return groupsWithTheirNumberOfStudents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<StudentDto, Set<CourseDto>> getStudentsWithCoursesByCourseName(String courseName) {
        boolean courseIsExist = validator.validateCourseName(courseName);
        Map<StudentDto, Set<CourseDto>> studentsWithTheirCourses = null;

        if (courseIsExist) {
            Course course = courseService.getCourseByName(courseName).get();
            studentsWithTheirCourses = course.getStudents().stream()
                    .collect(Collectors.toMap(StudentMapper::mapStudentToDto, this::getCoursesDtosForStudent));
        }

        LOGGER.debug("Received students with their courses by course name {}: {}", courseName,
                studentsWithTheirCourses);
        return studentsWithTheirCourses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<StudentDto, GroupDto> getAllStudentsWithTheirGroups() {
        return studentService.getAllStudents().stream().collect(Collectors.toMap(StudentMapper::mapStudentToDto,
                student -> GroupMapper.mapGroupToDto(student.getGroup())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addStudentToCourse(String firstName, String lastName, String courseName) {
        boolean studentExists = validator.validateStudentFullName(firstName, lastName);
        boolean courseExist = validator.validateCourseName(courseName);
        boolean studentNotOnCourse = !validator.isStudentOnCourse(firstName, lastName, courseName);
        boolean studentIsAddedToCourse = false;

        if (studentExists && courseExist && studentNotOnCourse) {
            Student student = studentService.getStudentByFullName(firstName, lastName).get();
            Course course = courseService.getCourseByName(courseName).get();
            student.addCourse(course);
            studentIsAddedToCourse = true;
        }

        LOGGER.debug("Added student with name {} {} to course {}: {}", firstName, lastName, courseName,
                studentIsAddedToCourse);
        return studentIsAddedToCourse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteStudentFromCourse(String firstName, String lastName, String courseName) {
        boolean studentExists = validator.validateStudentFullName(firstName, lastName);
        boolean coursesExist = validator.validateCourseName(courseName);
        boolean studentOnCourse = validator.isStudentOnCourse(firstName, lastName, courseName);
        boolean studentDeletedFromCourse = false;

        if (studentExists && coursesExist && studentOnCourse) {
            Student student = studentService.getStudentByFullName(firstName, lastName).get();
            Course course = courseService.getCourseByName(courseName).get();
            student.deleteCourse(course);
            studentDeletedFromCourse = true;
        }

        LOGGER.debug("Deleted student with name {} {}, from course {}: {}", firstName, lastName, courseName,
                studentDeletedFromCourse);
        return studentDeletedFromCourse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<StudentDto, Set<CourseDto>> getAllStudentsWithTheirCourses() {
        Map<StudentDto, Set<CourseDto>> allStudentsWithTheirCourses = studentService.getAllStudents().stream()
                .collect(Collectors.toMap(StudentMapper::mapStudentToDto, this::getCoursesDtosForStudent));
        LOGGER.debug("Received all students with their courses: {}", allStudentsWithTheirCourses);

        return allStudentsWithTheirCourses;
    }

    private void addStudentsToCourses() {
        Random random = new Random();
        LOGGER.info("Adding students to courses");

        for (Student student : studentService.getAllStudents()) {
            int coursesNumberForStudent = random.nextInt(3) + 1;
            Set<Integer> coursesForStudent = new HashSet<>();

            while (coursesForStudent.size() < coursesNumberForStudent) {
                int courseId = random.nextInt(1, 11);
                coursesForStudent.add(courseId);
            }

            for (Integer courseId : coursesForStudent) {
                Course course = courseService.getCourseById(courseId).get();
                student.addCourse(course);
                LOGGER.debug("Added student {} to course {}", student, course);
            }
        }

        LOGGER.info("Students have been added to courses.");
    }

    private Set<CourseDto> getCoursesDtosForStudent(Student student) {
        return student.getCourses().stream()
                .map(CourseMapper::mapCourseToDto)
                .collect(Collectors.toSet());
    }

}
