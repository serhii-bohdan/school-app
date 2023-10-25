package ua.foxminded.schoolapp.repository;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.schoolapp.TestApplicationConfig;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = { StudentRepository.class }
))
@ContextConfiguration(classes = TestApplicationConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = { "/sql/clear_tables.sql", "/sql/students_test_init.sql" },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class StudentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void save_shouldInvalidDataAccessApiUsageException_whenStudentIsNull() {
        Student student = null;

        assertThrows(InvalidDataAccessApiUsageException.class, () -> studentRepository.save(student));
    }

    @Test
    void save_shouldDataIntegrityViolationException_whenStudentFieldsNotInitialized() {
        Student student = new Student();

        assertThrows(DataIntegrityViolationException.class, () -> studentRepository.save(student));
    }

    @Test
    void save_shouldDataIntegrityViolationException_whenNoGroupWithGivenGroupIdAndNameInGroupsTable() {
        Group group = new Group("FS-12");
        group.setId(3);
        Student student = new Student("FirstName", "LastName", group);

        assertThrows(DataIntegrityViolationException.class, () -> studentRepository.save(student));
    }

    @Test
    void save_shouldDataIntegrityViolationException_whenStudentFirstNameIsNull() {
        Group group = new Group("MQ-90");
        group.setId(1);
        Student student = new Student(null, "LastName", group);

        assertThrows(DataIntegrityViolationException.class, () -> studentRepository.save(student));
    }

    @Test
    void save_shouldDataIntegrityViolationException_whenStudentLastNameIsNull() {
        Group group = new Group("MQ-90");
        group.setId(1);
        Student student = new Student("FirstName", null, group);

        assertThrows(DataIntegrityViolationException.class, () -> studentRepository.save(student));
    }

    @Test
    void save_shouldDataIntegrityViolationException_whenGroupIsNull() {
        Group group = null;
        Student student = new Student("FirstName", "LastName", group);

        assertThrows(DataIntegrityViolationException.class, () -> studentRepository.save(student));
    }

    @Test
    void save_shouldDataIntegrityViolationException_whenStudentFirstNameContainsMoreThanTwentyFiveLetters() {
        Group group = new Group("MQ-90");
        group.setId(1);
        Student student = new Student("FfffiiiiiirrrrrsssssttttName", "LastName", group);

        assertThrows(DataIntegrityViolationException.class, () -> studentRepository.save(student));
    }

    @Test
    void save_shouldDataIntegrityViolationException_whenStudentLastNameContainsMoreThanTwentyFiveLetters() {
        Group group = new Group("MQ-90");
        group.setId(1);
        Student student = new Student("FirstName", "LlllaaaassssttttNnnnaaaammmmeeee", group);

        assertThrows(DataIntegrityViolationException.class, () -> studentRepository.save(student));
    }

    @Test
    void save_shouldSavedStudent_whenStudentFirstNameAndLastNameAreEmpty() {
        Group group = new Group("MQ-90");
        group.setId(1);
        Student expectedStudent = new Student("", "", group);

        studentRepository.save(expectedStudent);
        Student actualStudent = entityManager.find(Student.class, 4);

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void save_shouldSavedStudent_whenStudentFieldsAreCorrect() {
        Group group = new Group("MQ-90");
        group.setId(1);
        Student expectedStudent = new Student("FirstName", "LastName", group);

        studentRepository.save(expectedStudent);
        Student actualStudent = entityManager.find(Student.class, 4);

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void save_shouldUpdatedStudentFirstNameAndLastName_whenStudentAlreadyExist() {
        Integer studentId = 3;
        String firstName = "NewFirstName";
        String lastName = "NewLastName";
        Student expectedStudent = entityManager.find(Student.class, studentId);
        expectedStudent.setFirstName(firstName);
        expectedStudent.setLastName(lastName);

        Student updatedStudent = studentRepository.save(expectedStudent);

        assertEquals(expectedStudent, updatedStudent);
    }

    @Test
    void save_shouldUpdatedStudentFirstNameAndLastName_whenStudentAlreadyExistAndStudentFirstNameAndLastNameAreNulls() {
        Integer studentId = 3;
        String firstName = null;
        String lastName = null;
        Student expectedStudent = entityManager.find(Student.class, studentId);
        expectedStudent.setFirstName(firstName);
        expectedStudent.setLastName(lastName);

        Student updatedStudent = studentRepository.save(expectedStudent);

        assertEquals(expectedStudent, updatedStudent);
        assertNull(updatedStudent.getFirstName());
        assertNull(updatedStudent.getLastName());
    }

    @Test
    void save_shouldUpdatedStudentGroup_whenStudentAlreadyExistAndNewGroupIsNull() {
        Integer studentId = 3;
        Student expectedNewStudent = entityManager.find(Student.class, studentId);
        expectedNewStudent.setGroup(null);

        Student updatedStudent = studentRepository.save(expectedNewStudent);

        assertEquals(expectedNewStudent, updatedStudent);
        assertNull(updatedStudent.getGroup());
    }

    @Test
    void findById_shouldEmptyOptional_whenNoStudentWithGivenId() {
        Integer studentIdThatNotExist = 5;

        Optional<Student> actualStudent = studentRepository.findById(studentIdThatNotExist);

        assertTrue(actualStudent.isEmpty());
    }

    @Test
    void findById_shouldFindedStudent_whenStudentWithGivenIdIsExist() {
        Integer studentIdThatExist = 3;

        Optional<Student> actualStudent = studentRepository.findById(studentIdThatExist);

        assertEquals(studentIdThatExist, actualStudent.get().getId());
    }

    @Test
    void findById_shouldInvalidDataAccessApiUsageException_whenStudentIdIsNull() {
        Integer studentId = null;

        assertThrows(InvalidDataAccessApiUsageException.class, () -> studentRepository.findById(studentId));
    }

    @Test
    void findByFirstNameAndLastName_shouldEmptyOptional_whenNoStudentWithGivenFirstnameAndLastName() {
        String firstName = "NotExistent";
        String lastName = "NotExistent";

        Optional<Student> actualStudent = studentRepository.findByFirstNameAndLastName(firstName, lastName);

        assertTrue(actualStudent.isEmpty());
    }

    @Test
    void findByFirstNameAndLastName_shouldEmptyOptional_whenStudentFirstNameIsNull() {
        String firstName = null;
        String lastName = "LastName_1";

        Optional<Student> actualStudent = studentRepository.findByFirstNameAndLastName(firstName, lastName);

        assertTrue(actualStudent.isEmpty());
    }

    @Test
    void findByFirstNameAndLastName_shouldEmptyOptional_whenStudentLastNameIsNull() {
        String firstName = "FirstName_1";
        String lastName = null;

        Optional<Student> actualStudent = studentRepository.findByFirstNameAndLastName(firstName, lastName);

        assertTrue(actualStudent.isEmpty());
    }

    @Test
    void findByFirstNameAndLastName_shouldFindedStudent_whenStudentWithGivenFirstNameAndLastNameExist() {
        String firstName = "FirstName_1";
        String lastName = "LastName_1";

        Student actualStudent = studentRepository.findByFirstNameAndLastName(firstName, lastName).get();

        assertEquals(firstName, actualStudent.getFirstName());
        assertEquals(lastName, actualStudent.getLastName());
    }

    @Test
    @Sql("/sql/clear_tables.sql")
    void findAll_shouldEmptyStudentsList_whenStudentsTableEmpty() {
        List<Student> actualAllAvailableStudnts = studentRepository.findAll();

        assertTrue(actualAllAvailableStudnts.isEmpty());
    }

    @Test
    void findAll_shouldStudentsList_whenStudentsTableContainsStudnets() {
        List<Student> actualAllAvailableStudnts = studentRepository.findAll();

        assertEquals(3, actualAllAvailableStudnts.size());
    }

    @Test
    void delete_shouldDeletedStudent_whenStudentWithGivenDataExists() {
        Group group = new Group("MQ-90");
        group.setId(1);
        Student studentToDelete = new Student("FirstName_3", "LastName_3", group);
        Integer studentId = 3;
        studentToDelete.setId(studentId);

        studentRepository.delete(studentToDelete);

        assertNull(entityManager.find(Student.class, studentId));
    }

    @Test
    void delete_shouldNothingDeleted_whenNoStudentWithGivenData() {
        Group group = new Group("MQ-90");
        group.setId(1);
        Student studentToDelete = new Student("FirstName_5", "LastName_5", group);
        int studentIdThatNotExist = 5;
        studentToDelete.setId(studentIdThatNotExist);

        studentRepository.delete(studentToDelete);

        assertNull(entityManager.find(Student.class, studentIdThatNotExist));
    }

    @Test
    void delete_shouldInvalidDataAccessApiUsageException_whenStudentIsNull() {
        Student student = null;

        assertThrows(InvalidDataAccessApiUsageException.class, () -> studentRepository.delete(student));
    }

}
