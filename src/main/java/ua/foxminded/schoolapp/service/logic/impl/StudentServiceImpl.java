package ua.foxminded.schoolapp.service.logic.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import ua.foxminded.schoolapp.dao.StudentDao;
import ua.foxminded.schoolapp.dto.StudentDto;
import ua.foxminded.schoolapp.dto.mapper.StudentMapper;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.generate.Generatable;
import ua.foxminded.schoolapp.service.logic.StudentService;

/**
 * The StudentServiceImpl class is an implementation of the
 * {@link StudentService} interface. It provides operations for managing
 * students, such as initializing students, adding, deleting students and
 * retrieving student data.
 * <p>
 * The class is annotated with {@code @Service} to indicate that it is a Spring
 * service, and it can be automatically discovered and registered as a bean in
 * the Spring context. The StudentServiceImpl requires instances of
 * {@link Generatable<StudentDto>} for generating students Dto and a
 * {@link StudentDao} for data access to perform its operations.
 *
 * @author Serhii Bohdan
 */
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    /**
     * The logger for logging events and messages in the {@link StudentServiceImpl}
     * class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final Generatable<StudentDto> studentsGenerator;
    private final StudentDao studentDao;

    /**
     * Constructs a new StudentServiceImpl with the specified students generator and
     * student Dao.
     *
     * @param studentsGenerator the generator for creating students
     * @param studentDao        the data access object for students
     */
    public StudentServiceImpl(Generatable<StudentDto> studentsGenerator, StudentDao studentDao) {
        this.studentsGenerator = studentsGenerator;
        this.studentDao = studentDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initStudents(List<Group> groups) {
        LOGGER.info("Filling with generated students");
        List<StudentDto> generatedStudents = studentsGenerator.toGenerate();
        List<Integer> groupsIndexes = getRandomGroupsIndexes(generatedStudents.size(), groups.size());

        for (int i = 0; i < groupsIndexes.size(); i++) {
            Student student = StudentMapper.mapDtoToStudent(generatedStudents.get(i));
            student.setGroup(groups.get(groupsIndexes.get(i)));
            studentDao.save(student);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Student> addStudent(String firstName, String lastName, Group group) {
        Student newStudent = new Student(firstName, lastName, group);
        LOGGER.debug("Adding a new student: {}", newStudent);

        return Optional.ofNullable(studentDao.save(newStudent));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Student> getStudentById(Integer studentId) {
        Student student = studentDao.find(studentId);
        LOGGER.debug("Received student by ID {}: {}", studentId, student);

        return Optional.ofNullable(student);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Student> getStudentByFullName(String firstName, String lastName) {
        Optional<Student> student = studentDao.findStudentByFullName(firstName, lastName);
        LOGGER.debug("Search student by full name {} {}: {}", firstName, lastName, student);

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

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Student> updateStudent(Student udatedStudent) {
        Student student = studentDao.update(udatedStudent);
        LOGGER.debug("Updating student data: {}", student);

        return Optional.ofNullable(student);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteStudent(Integer studentId) {
        LOGGER.debug("Deleting student with ID: {}", studentId);
        studentDao.delete(studentId);
    }

    private List<Integer> getRandomGroupsIndexes(Integer studentsNumber, Integer groupsNumber) {
        LOGGER.debug("Generating random group indexes");
        Random random = new Random();
        List<Integer> randomNumbers = new ArrayList<>();
        int maxCountStudentsInGroup = 30;
        int minCountStudentsInGroup = 10;

        if (studentsNumber > 0 && groupsNumber > 0) {
            Integer maxGroupIndex = groupsNumber - 1;

            while (randomNumbers.size() < studentsNumber) {
                int randomGroupId = random.nextInt(0, 10);

                if ((Collections.frequency(randomNumbers, randomGroupId) < maxCountStudentsInGroup)
                        && (randomGroupId <= maxGroupIndex)) {
                    randomNumbers.add(randomGroupId);
                }

                if ((Collections.frequency(randomNumbers, randomGroupId) < minCountStudentsInGroup)
                        && (randomGroupId <= maxGroupIndex) && (randomNumbers.size() < studentsNumber)) {
                    randomNumbers.add(randomGroupId);
                }
            }
        }

        Collections.shuffle(randomNumbers);
        LOGGER.debug("Generated random group indexes: {}", randomNumbers);
        return randomNumbers;
    }

}
