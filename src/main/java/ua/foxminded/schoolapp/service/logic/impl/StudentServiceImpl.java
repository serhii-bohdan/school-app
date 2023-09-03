package ua.foxminded.schoolapp.service.logic.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.foxminded.schoolapp.dao.StudentDao;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.generate.Generatable;
import ua.foxminded.schoolapp.service.logic.StudentService;

/**
 * The StudentServiceImpl class is an implementation of the
 * {@link StudentService} interface. It provides operations for managing
 * students, such as initializing students, adding and deleting students,
 * managing student enrollments in courses, and retrieving student data.
 * <p>
 * The class is annotated with {@code @Service} to indicate that it is a Spring
 * service, and it can be automatically discovered and registered as a bean in
 * the Spring context. The StudentServiceImpl requires instances of
 * {@link Generatable<Student>} for generating students and a {@link StudentDao}
 * for data access to perform its operations.
 *
 * @author Serhii Bohdan
 */
@Service
public class StudentServiceImpl implements StudentService {

    /**
     * The logger for logging events and messages in the {@link StudentServiceImpl}
     * class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final Generatable<Student> studentsGenerator;
    private final StudentDao studentDao;

    /**
     * Constructs a new StudentServiceImpl with the specified students generator and
     * student Dao.
     *
     * @param studentsGenerator the generator for creating students
     * @param studentDao        the data access object for students
     */
    public StudentServiceImpl(Generatable<Student> studentsGenerator, StudentDao studentDao) {
        this.studentsGenerator = studentsGenerator;
        this.studentDao = studentDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initStudents() {
        LOGGER.info("Filling with generated students");
        studentsGenerator.toGenerate().forEach(studentDao::save);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initStudentsCoursesTable() {
        Random random = new Random();
        int studentsNumberPresentInDatabase = studentDao.findAll().size();
        LOGGER.info("Adding students to courses");

        for (int studentId = 1; studentId <= studentsNumberPresentInDatabase; studentId++) {
            int coursesNumberForStudent = random.nextInt(3) + 1;
            Set<Integer> coursesForStudent = new HashSet<>();

            while (coursesForStudent.size() < coursesNumberForStudent) {
                int courseId = random.nextInt(1, 11);
                coursesForStudent.add(courseId);
            }

            for (Integer courseId : coursesForStudent) {
                studentDao.addStudentToCourse(studentId, courseId);
                LOGGER.debug("Added student with ID {} to course with ID {}", studentId, courseId);
            }
        }

        LOGGER.info("Students have been added to courses.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Student> getStudentsRelatedToCourse(String courseName) {
        List<Student> studentsRelatedToCourse = studentDao.findStudentsRelatedToCourse(courseName);
        LOGGER.debug("Received students who are registered for the course {}: {}", courseName, studentsRelatedToCourse);

        return studentsRelatedToCourse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addStudent(String firstName, String lastName, int groupId) {
        Student newStudent = new Student(firstName, lastName, groupId);
        LOGGER.debug("Adding a new student: {}", newStudent);

        return studentDao.save(newStudent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteStudent(int studentId) {
        LOGGER.debug("Deleting student with ID: {}", studentId);
        return studentDao.delete(studentId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addStudentToCourse(String firstName, String lastName, String courseName) {
        LOGGER.debug("Adding student with name: {} {} to course: {}", firstName, lastName, courseName);
        return studentDao.addStudentToCourse(firstName, lastName, courseName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteStudentFromCourse(String firstName, String lastName, String courseName) {
        LOGGER.debug("Deleting student with name: {} {}, from course: {}", firstName, lastName, courseName);
        return studentDao.deleteStudentFromCourse(firstName, lastName, courseName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Student getStudentById(int studentId) {
        Student student = studentDao.find(studentId);
        LOGGER.debug("Received student by ID {}: {}", studentId, student);

        return student;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Student> getAllStudents() {
        List<Student> allStudents = studentDao.findAll();
        LOGGER.debug("All received students: {}", allStudents);

        return allStudents;
    }

}
