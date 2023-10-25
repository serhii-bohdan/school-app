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
import ua.foxminded.schoolapp.dto.StudentDto;
import ua.foxminded.schoolapp.dto.mapper.StudentMapper;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.repository.StudentRepository;
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
 * {@link Generatable} for generating students Dto and a
 * {@link StudentRepository} for data access to perform its operations.
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
    private final StudentRepository studentRepository;

    /**
     * Constructs a new StudentServiceImpl with the specified students generator and
     * student repository.
     *
     * @param studentsGenerator an instance of {@link Generatable} for generating
     *                          student data
     * @param studentRepository an instance of {@link StudentRepository} for
     *                          accessing and managing student information
     */
    public StudentServiceImpl(Generatable<StudentDto> studentsGenerator, StudentRepository studentRepository) {
        this.studentsGenerator = studentsGenerator;
        this.studentRepository = studentRepository;
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
            studentRepository.save(student);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Student> addStudent(StudentDto newStudent) {
        Student student = StudentMapper.mapDtoToStudent(newStudent);
        LOGGER.debug("Adding a new student: {}", student);

        return Optional.ofNullable(studentRepository.save(student));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Student> getStudentById(Integer studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        LOGGER.debug("Received student by ID {}: {}", studentId, student);

        return student;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Student> getStudentByFullName(String firstName, String lastName) {
        Optional<Student> student = studentRepository.findByFirstNameAndLastName(firstName, lastName);
        LOGGER.debug("Search student by full name {} {}: {}", firstName, lastName, student);

        return student;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Student> getAllStudents() {
        List<Student> allStudents = studentRepository.findAll();
        LOGGER.debug("All received students: {}", allStudents);

        return allStudents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Student> updateStudent(Student udatedStudent) {
        LOGGER.debug("Updating student data: {}", udatedStudent);
        Student student = studentRepository.save(udatedStudent);

        return Optional.ofNullable(student);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteStudentById(Integer studentId) {
        Optional<Student> student = getStudentById(studentId);

        if (student.isPresent()) {
            LOGGER.debug("Deleting student with ID {}: {}", studentId, student);
            studentRepository.delete(student.get());
        }
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
