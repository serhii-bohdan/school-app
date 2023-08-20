package ua.foxminded.schoolapp.service.logic.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
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
        studentsGenerator.toGenerate().forEach(studentDao::save);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initStudentsCoursesTable() {
        Random random = new Random();
        int studentsNumberPresentInDatabase = studentDao.findAll().size();

        for (int studentId = 1; studentId <= studentsNumberPresentInDatabase; studentId++) {
            int coursesNumberForStudent = random.nextInt(3) + 1;
            Set<Integer> coursesForStudent = new HashSet<>();

            while (coursesForStudent.size() < coursesNumberForStudent) {
                int courseId = random.nextInt(1, 11);
                coursesForStudent.add(courseId);
            }

            for (Integer courseId : coursesForStudent) {
                studentDao.addStudentToCourse(studentId, courseId);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Student> getStudentsRelatedToCourse(String courseName) {
        return studentDao.findStudentsRelatedToCourse(courseName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addStudent(String firstName, String lastName, int groupId) {
        return studentDao.save(new Student(firstName, lastName, groupId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteStudent(int studentId) {
        return studentDao.delete(studentId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addStudentToCourse(String firstName, String lastName, String courseName) {
        return studentDao.addStudentToCourse(firstName, lastName, courseName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteStudentFromCourse(String firstName, String lastName, String courseName) {
        return studentDao.deleteStudentFromCourse(firstName, lastName, courseName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Student getStudentById(int studentId) {
        return studentDao.find(studentId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Student> getAllStudents() {
        return studentDao.findAll();
    }

}
