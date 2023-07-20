package ua.foxminded.schoolapp.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import ua.foxminded.schoolapp.dao.StudentDao;
import ua.foxminded.schoolapp.datasetup.Generatable;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.StudentService;

/**
 * The StudentServiceImpl class is an implementation of the
 * {@link StudentService} interface. It provides operations for managing
 * students.
 *
 * @author Serhii Bohdan
 */
public class StudentServiceImpl implements StudentService {

    private Generatable<Student> studentsGenerator;
    private StudentDao studentDao;

    /**
     * Constructs a new StudentServiceImpl with the specified students generator and
     * student Dao.
     *
     * @param studentsGenerator the generator for creating students
     * @param studentDao        the data access object for students
     * @throws NullPointerException if either studentsGenerator or studentDao is
     *                              null
     */
    public StudentServiceImpl(Generatable<Student> studentsGenerator, StudentDao studentDao) {
        Objects.requireNonNull(studentsGenerator, "studentsGenerator must not be null");
        Objects.requireNonNull(studentDao, "studentDao must not be null");
        this.studentsGenerator = studentsGenerator;
        this.studentDao = studentDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initStudents() {
        studentsGenerator.toGenerate().stream()
                                      .forEach(studentDao::save);
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
        return studentDao.deleteStudentById(studentId);
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
        return studentDao.findStudentById(studentId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Student> getAllStudents() {
        return studentDao.findAll();
    }

}
