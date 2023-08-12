package ua.foxminded.schoolapp.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.schoolapp.TestAppConfig;
import ua.foxminded.schoolapp.dao.StudentDao;
import ua.foxminded.schoolapp.dao.mapper.StudentRowMapper;
import ua.foxminded.schoolapp.model.Student;

@JdbcTest
@ContextConfiguration(classes = TestAppConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = { "/sql/clear_tables.sql", "/sql/students_test_init.sql" },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class StudentDaoImplTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    StudentDao studentDao;
    final RowMapper<Student> studentRowMapper = new StudentRowMapper();

    @BeforeEach
    void setUp() {
        studentDao = new StudentDaoImpl(jdbcTemplate);
    }

    @Test
    void save_shouldNullPointerException_whenStudentIsNull() {
        assertThrows(NullPointerException.class, () -> studentDao.save(null));
    }

    @Test
    void save_shouldDataIntegrityViolationException_whenStudentFieldsNotInitialized() {
        Student student = new Student();

        assertThrows(DataIntegrityViolationException.class, () -> studentDao.save(student));
    }

    @Test
    void save_shouldDataIntegrityViolationException_whenNoGroupWithSpecifiedGroupIdInGroupsTable() {
        Student student = new Student("FirstName", "LastName", 2);

        assertThrows(DataIntegrityViolationException.class, () -> studentDao.save(student));
    }

    @Test
    void save_shouldSavedStudent_whenStudentFirstNameAndLastNameIsEmpty() {
        Student expectedStudent = new Student("", "", 1);
        expectedStudent.setId(4);
        String selectTestDataScript = """
                SELECT * FROM students
                WHERE student_id = 4;
                """;

        studentDao.save(expectedStudent);
        Student actualStudent = jdbcTemplate.queryForObject(selectTestDataScript, studentRowMapper);

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void save_shouldSavedStudent_whenStudentFieldsIsCorrect() {
        Student expectedStudent = new Student("FirstName", "LastName", 1);
        expectedStudent.setId(4);
        String selectTestDataScript = """
                SELECT * FROM students
                WHERE student_id = 4;
                """;

        studentDao.save(expectedStudent);
        Student actualStudent = jdbcTemplate.queryForObject(selectTestDataScript, studentRowMapper);

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void find_shouldNull_whenNoStudentWithGivenId() {
        int studentIdThatNotExist = 5;
        Student actualStudent = studentDao.find(studentIdThatNotExist);

        assertNull(actualStudent);
    }

    @Test
    void find_shouldStudentThatExistInStudentsTable_whenStudentWithGivenIdExists() {
        int expectedStudentId = 2;
        Student expectedStudent = new Student("FirstName_2", "LastName_2", 1);
        expectedStudent.setId(expectedStudentId);

        Student actualStudent = studentDao.find(expectedStudentId);

        assertEquals(expectedStudent, actualStudent);
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
    void update_shouldUpdatedStudent_whenStudentWithGivenIdExists() {
        Student expectedNewStudent = new Student("NewFirstName", "NewLastName", 1);
        expectedNewStudent.setId(3);
        String selectTestDataScript = """
                SELECT * FROM students
                WHERE student_id = 3;
                """;

        int numberOfChanges = studentDao.update(expectedNewStudent);
        Student actualNewStudent = jdbcTemplate.queryForObject(selectTestDataScript, studentRowMapper);

        assertEquals(1, numberOfChanges);
        assertEquals(expectedNewStudent, actualNewStudent);
    }

    @Test
    void update_shouldNoAnuChanges_whenNoStudentWithGivenId() {
        Student expectedNewStudent = new Student("NewFirstName", "NewLastName", 1);
        expectedNewStudent.setId(5);

        int numberOfChanges = studentDao.update(expectedNewStudent);

        assertEquals(0, numberOfChanges);
    }

    @Test
    void delete_shouldDeletedStudent_whenStudentWithGivenIdExists() {
        int studentId = 3;
        String selectTestDataScript = """
                SELECT * FROM students
                WHERE student_id = 3;
                """;

        int numberOfChanges = studentDao.delete(studentId);

        assertEquals(1, numberOfChanges);
        assertThrows(EmptyResultDataAccessException.class,
                () -> jdbcTemplate.queryForObject(selectTestDataScript, studentRowMapper));
    }

    @Test
    void delete_shouldNothingDeleted_whenNoStudentWithGivenId() {
        int studentIdThatNotExist = 5;

        int numberOfChanges = studentDao.delete(studentIdThatNotExist);

        assertEquals(0, numberOfChanges);
    }

    @Test
    void findStudentsRelatedToCourse_shouldEmptyStudentsList_whenGivenCourseNameIsNull() {
        List<Student> actualStudentsOnCourse = studentDao.findStudentsRelatedToCourse(null);

        assertTrue(actualStudentsOnCourse.isEmpty());
    }

    @Test
    void findStudentsRelatedToCourse_shouldEmptyStudentsList_whenNoCourseWithGivenName() {
        List<Student> actualStudentsOnCourse = studentDao.findStudentsRelatedToCourse("NonExistentCourse");

        assertTrue(actualStudentsOnCourse.isEmpty());
    }

    @Test
    @Sql(statements = "DELETE FROM students_courses;")
    void findStudentsRelatedToCourse_shouldEmptyStudentsList_whenNoStudentsOnCourse() {
        List<Student> actualStudentsOnCourse = studentDao.findStudentsRelatedToCourse("CourseName");

        assertTrue(actualStudentsOnCourse.isEmpty());
    }

    @Test
    void findStudentsRelatedToCourse_shouldListOfStudentsOnCourseWithGivenName_whenStudentsRegisteredForCourse() {
        List<Student> actualStudentsOnCourse = studentDao.findStudentsRelatedToCourse("CourseName");

        assertEquals(3, actualStudentsOnCourse.size());
    }

    @Test
    void isStudentOnCourse_shouldTrue_whenStudentOnCourse() {
        boolean studentOnCourse = studentDao.isStudentOnCourse("FirstName_1", "LastName_1", "CourseName");

        assertTrue(studentOnCourse);
    }

    @Test
    @Sql(statements = "DELETE FROM students_courses;")
    void isStudentOnCourse_shouldFalse_whenStudentNotOnCourse() {
        boolean studentOnCourse = studentDao.isStudentOnCourse("FirstName_1", "LastName_1", "CourseName");

        assertFalse(studentOnCourse);
    }

    @Test
    void isStudentOnCourse_shouldFalse_whenNoStudentWithGivenFirstNameAndLastName() {
        boolean studentOnCourse = studentDao.isStudentOnCourse("NotExists", "NotExists", "CourseName");

        assertFalse(studentOnCourse);
    }

    @Test
    void isStudentOnCourse_shouldFalse_whenNoCourseWithGivenName() {
        boolean studentOnCourse = studentDao.isStudentOnCourse("FirstName_1", "LastName_1", "NotExists");

        assertFalse(studentOnCourse);
    }

    @Test
    void isStudentOnCourse_shouldFalse_whenGivenStudentFirstNameIsNull() {
        boolean studentOnCourse = studentDao.isStudentOnCourse(null, "LastName_1", "CourseName");

        assertFalse(studentOnCourse);
    }

    @Test
    void isStudentOnCourse_shouldFalse_whenGivenCourseNameIsNull() {
        boolean studentOnCourse = studentDao.isStudentOnCourse("FirstName_1", "LastName_1", null);

        assertFalse(studentOnCourse);
    }

    @Test
    void addStudentToCourse_shouldAddedStudentToCourse_whenExistsStudentWithGivenFirstNameAndLastNameAndExistCourseWithGivenName() {
        int numberOfChanges = studentDao.addStudentToCourse("FirstName_1", "LastName_1", "CourseName");

        assertEquals(1, numberOfChanges);
    }

    @Test
    void addStudentToCourse_shouldDataIntegrityViolationException_whenNotExistsStudentWithGivenLastName() {
        assertThrows(DataIntegrityViolationException.class,
                () -> studentDao.addStudentToCourse("FirstName_1", "NotExists", "CourseName"));
    }

    @Test
    void addStudentToCourse_shouldDaoDataIntegrityViolationException_whenNotExistsCourseWithGivenName() {
        assertThrows(DataIntegrityViolationException.class,
                () -> studentDao.addStudentToCourse("FirstName_1", "LastName_2", "NotExists"));
    }

    @Test
    void addStudentToCourse_shouldDataIntegrityViolationException_whenStudentLastNameIsNull() {
        assertThrows(DataIntegrityViolationException.class,
                () -> studentDao.addStudentToCourse("FirstName_1", null, "CourseName"));
    }

    @Test
    void addStudentToCourse_shouldDataIntegrityViolationException_whenCourseNameIsNull() {
        assertThrows(DataIntegrityViolationException.class,
                () -> studentDao.addStudentToCourse("FirstName_1", "LastName_1", null));
    }

    @Test
    void addStudentToCourseCongested_shouldAddedStudentToCourse_whenExistsStudentWithGivenIdAndExistCourseWithGivenId() {
        int numberOfChanges = studentDao.addStudentToCourse(1, 1);

        assertEquals(1, numberOfChanges);
    }

    @Test
    void addStudentToCourseCongested_shouldDataIntegrityViolationException_whenNotExistsStudentWithGivenStudentId() {
        int notExistentStudentId = 4;

        assertThrows(DataIntegrityViolationException.class,
                () -> studentDao.addStudentToCourse(notExistentStudentId, 1));
    }

    @Test
    void addStudentToCourseCongested_shouldDataIntegrityViolationException_whenNotExistsCourseWithGivenCoursetId() {
        int notExistentCourseId = 2;

        assertThrows(DataIntegrityViolationException.class,
                () -> studentDao.addStudentToCourse(1, notExistentCourseId));
    }

    @Test
    void deleteStudentFromCourse_shouldDeletedStudentFromCourse_whenStudentWasPreviouslyOnCourse() {
        int numberOfChanges = studentDao.deleteStudentFromCourse("FirstName_2", "LastName_2", "CourseName");

        assertEquals(1, numberOfChanges);
    }

    @Test
    @Sql(statements = "INSERT INTO courses (course_name, course_description) VALUES('CourseName_2', 'Description_2');")
    void deleteStudentFromCourse_shouldNothingDeleted_whenNotExistsStudentWithGivenFirstNameOnCourse() {
        int numberOfChanges = studentDao.deleteStudentFromCourse("FirstName_1", "LastName_1", "CourseName_2");

        assertEquals(0, numberOfChanges);
    }

    @Test
    void deleteStudentFromCourse_shouldNothingDeleted_whenNoStudentWithGivenFirstName() {
        int numberOfChanges = studentDao.deleteStudentFromCourse("NotExists", "LastName_1", "CourseName");

        assertEquals(0, numberOfChanges);
    }

    @Test
    void deleteStudentFromCourse_shouldNothingDeleted_whenNoCourseWithGivenName() {
        int numberOfChanges = studentDao.deleteStudentFromCourse("FirstName_2", "LastName_2", "NotExists");

        assertEquals(0, numberOfChanges);
    }

    @Test
    void deleteStudentFromCourse_shouldNothingDeleted_whenStudentFirstNameIsNull() {
        int numberOfChanges = studentDao.deleteStudentFromCourse(null, "LastName_1", "CourseName");

        assertEquals(0, numberOfChanges);
    }

    @Test
    void deleteStudentFromCourse_shouldNothingDeleted_whenCourseNameIsNull() {
        int numberOfChanges = studentDao.deleteStudentFromCourse("FirstName_1", "LastName_1", null);

        assertEquals(0, numberOfChanges);
    }

}
