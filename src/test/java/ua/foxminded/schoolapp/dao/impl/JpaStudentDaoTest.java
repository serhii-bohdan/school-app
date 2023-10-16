package ua.foxminded.schoolapp.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.schoolapp.TestApplicationConfig;
import ua.foxminded.schoolapp.dao.StudentDao;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = { JpaStudentDao.class }
))
@ContextConfiguration(classes = TestApplicationConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = { "/sql/clear_tables.sql", "/sql/students_test_init.sql" },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class JpaStudentDaoTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    StudentDao studentDao;

    @Test
    void save_shouldIllegalArgumentException_whenStudentIsNull() {
        Student student = null;

        assertThrows(IllegalArgumentException.class, () -> studentDao.save(student));
    }

    @Test
    void save_shouldPropertyValueException_whenStudentFieldsNotInitialized() {
        Student student = new Student();

        assertThrows(PropertyValueException.class, () -> studentDao.save(student));
    }

    @Test
    void save_shouldConstraintViolationException_whenNoGroupWithGivenGroupIdAndNameInGroupsTable() {
        Group group = new Group("MQ-90");
        group.setId(3);
        Student student = new Student("FirstName", "LastName", group);

        assertThrows(ConstraintViolationException.class, () -> studentDao.save(student));
    }

    @Test
    void save_shouldConstraintViolationException_whenStudentFirstNameIsNull() {
        Group group = new Group("MQ-90");
        group.setId(1);
        Student student = new Student(null, "LastName", group);

        assertThrows(ConstraintViolationException.class, () -> studentDao.save(student));
    }

    @Test
    void save_shouldConstraintViolationException_whenStudentLastNameIsNull() {
        Group group = new Group("MQ-90");
        group.setId(1);
        Student student = new Student("FirstName", null, group);

        assertThrows(ConstraintViolationException.class, () -> studentDao.save(student));
    }

    @Test
    void save_shouldPropertyValueException_whenGroupIsNull() {
        Group group = null;
        Student student = new Student("FirstName", "LastName", group);

        assertThrows(PropertyValueException.class, () -> studentDao.save(student));
    }

    @Test
    void save_shouldDataException_whenStudentFirstNameContainsMoreThanTwentyFiveLetters() {
        Group group = new Group("MQ-90");
        group.setId(1);
        Student student = new Student("FfffiiiiiirrrrrsssssttttName", "LastName", group);

        assertThrows(DataException.class, () -> studentDao.save(student));
    }

    @Test
    void save_shouldDataException_whenStudentLastNameContainsMoreThanTwentyFiveLetters() {
        Group group = new Group("MQ-90");
        group.setId(1);
        Student student = new Student("FirstName", "LlllaaaassssttttNnnnaaaammmmeeee", group);
        
        assertThrows(DataException.class, () -> studentDao.save(student));
    }

    @Test
    void save_shouldSavedStudent_whenStudentFirstNameAndLastNameAreEmpty() {
        Group group = new Group("MQ-90");
        group.setId(1);
        Student expectedStudent = new Student("", "", group);

        studentDao.save(expectedStudent);
        Student actualStudent = entityManager.find(Student.class, 4);

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void save_shouldSavedStudent_whenStudentFieldsAreCorrect() {
        Group group = new Group("MQ-90");
        group.setId(1);
        Student expectedStudent = new Student("FirstName", "LastName", group);

        studentDao.save(expectedStudent);
        Student actualStudent = entityManager.find(Student.class, 4);

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void find_shouldNull_whenNoStudentWithGivenId() {
        Integer studentIdThatNotExist = 5;

        Student actualStudent = studentDao.find(studentIdThatNotExist);

        assertNull(actualStudent);
    }

    @Test
    void find_shouldFindedStudent_whenStudentWithGivenIdIsExist() {
        Integer studentIdThatExist = 3;

        Student actualStudent = studentDao.find(studentIdThatExist);

        assertEquals(studentIdThatExist, actualStudent.getId());
    }

    @Test
    void find_shouldIllegalArgumentException_whenStudentIdIsNull() {
        Integer studentId = null;

        assertThrows(IllegalArgumentException.class, () -> studentDao.find(studentId));
    }

    @Test
    void findStudentByFullName_shouldEmptyOptional_whenNoStudentWithGivenFullName() {
        String firstName = "NotExistent";
        String lastName = "NotExistent";

        Optional<Student> actualStudent = studentDao.findStudentByFullName(firstName, lastName);

        assertTrue(actualStudent.isEmpty());
    }

    @Test
    void findStudentByFullName_shouldEmptyOptional_whenStudentFirstNameIsNull() {
        String firstName = null;
        String lastName = "LastName_1";

        Optional<Student> actualStudent = studentDao.findStudentByFullName(firstName, lastName);

        assertTrue(actualStudent.isEmpty());
    }

    @Test
    void findStudentByFullName_shouldEmptyOptional_whenStudentLastNameIsNull() {
        String firstName = "FirstName_1";
        String lastName = null;

        Optional<Student> actualStudent = studentDao.findStudentByFullName(firstName, lastName);

        assertTrue(actualStudent.isEmpty());
    }

    @Test
    void findStudentByFullName_shouldFindedStudent_whenStudentWithGivenFirstNameAndLastNameExist() {
        String firstName = "FirstName_1";
        String lastName = "LastName_1";

        Student actualStudent = studentDao.findStudentByFullName(firstName, lastName).get();

        assertEquals(firstName, actualStudent.getFirstName());
        assertEquals(lastName, actualStudent.getLastName());
    }

    @Test
    @Sql("/sql/clear_tables.sql")
    void findAll_shouldEmptyStudentsList_whenStudentsTableEmpty() {
        List<Student> actualAllAvailableStudnts = studentDao.findAll();

        assertTrue(actualAllAvailableStudnts.isEmpty());
    }

    @Test
    void findAll_shouldStudentsList_whenStudentsTableContainsStudnets() {
        List<Student> actualAllAvailableStudnts = studentDao.findAll();

        assertEquals(3, actualAllAvailableStudnts.size());
    }

    @Test
    void update_shouldUpdatedStudent_whenStudentWithGivenIdIsExists() {
        Group group = new Group("MQ-90");
        group.setId(1);
        Integer studentId = 3;
        String firstName = "NewFirstName";
        String lastName = "NewLastName";
        Student expectedNewStudent = new Student(firstName, lastName, group);
        expectedNewStudent.setId(studentId);

        Student updatedStudent = studentDao.update(expectedNewStudent);

        assertEquals(studentId, updatedStudent.getId());
        assertEquals(firstName, updatedStudent.getFirstName());
        assertEquals(lastName, updatedStudent.getLastName());
    }

    @Test
    void update_shouldSavedNewStudentWithAnotherId_whenNoStudentWithGivenId() {
        Group group = new Group("MQ-90");
        group.setId(1);
        Integer anotherStudentId = 4;
        String firstName = "NewFirstName";
        String lastName = "NewLastName";
        Student expectedNewStudent = new Student(firstName, lastName, group);
        expectedNewStudent.setId(5);

        Student newSavedStudent = studentDao.update(expectedNewStudent);

        assertEquals(anotherStudentId, newSavedStudent.getId());
        assertEquals(firstName, newSavedStudent.getFirstName());
        assertEquals(lastName, newSavedStudent.getLastName());
    }

    @Test
    void update_shouldUpdatedStudentFirstNameAndLastName_whenStudentWithGivenIdExistsAndNewFirstNameIsNull() {
        Group group = new Group("MQ-90");
        group.setId(1);
        Integer studentId = 1;
        String firstName = null;
        String lastName = "NewLastName";
        Student expectedNewStudent = new Student(firstName, lastName, group);
        expectedNewStudent.setId(studentId);

        Student updatedStudent = studentDao.update(expectedNewStudent);

        assertEquals(studentId, updatedStudent.getId());
        assertEquals(firstName, updatedStudent.getFirstName());
        assertEquals(lastName, updatedStudent.getLastName());
    }

    @Test
    void update_shouldUpdatedStudentFirstNameAndLastNameAndGroup_whenStudentWithGivenIdExistsAndGroupIsNull() {
        Group group = null;
        Integer studentId = 1;
        String firstName = "NewFirstName";
        String lastName = "NewLastName";
        Student expectedNewStudent = new Student(firstName, lastName, group);
        expectedNewStudent.setId(studentId);

        Student updatedStudent = studentDao.update(expectedNewStudent);

        assertEquals(studentId, updatedStudent.getId());
        assertEquals(firstName, updatedStudent.getFirstName());
        assertEquals(lastName, updatedStudent.getLastName());
    }

    @Test
    void update_shouldIllegalArgumentException_whenStudentIsNull() {
        Student expectedNewStudent = null;

        assertThrows(IllegalArgumentException.class, () -> studentDao.update(expectedNewStudent));
    }

    @Test
    void delete_shouldDeletedStudent_whenStudentWithGivenIdExists() {
        int studentId = 3;

        studentDao.delete(studentId);

        assertNull(entityManager.find(Student.class, studentId));
    }

    @Test
    void delete_shouldNothingDeleted_whenNoStudentWithGivenId() {
        int studentIdThatNotExist = 5;

        studentDao.delete(studentIdThatNotExist);

        assertNull(entityManager.find(Student.class, studentIdThatNotExist));
    }

    @Test
    void delete_shouldIllegalArgumentException_whenStudentIdIsNull() {
        Integer studentIdThatNotExist = null;

        assertThrows(IllegalArgumentException.class, () -> studentDao.delete(studentIdThatNotExist));
    }

}
