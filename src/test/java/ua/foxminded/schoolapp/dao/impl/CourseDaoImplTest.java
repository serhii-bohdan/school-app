package ua.foxminded.schoolapp.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
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
import ua.foxminded.schoolapp.TestApplicationConfig;
import ua.foxminded.schoolapp.dao.CourseDao;
import ua.foxminded.schoolapp.dao.mapper.CourseRowMapper;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Student;

@JdbcTest
@ContextConfiguration(classes = TestApplicationConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = { "/sql/clear_tables.sql", "/sql/courses_test_init.sql" },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class CourseDaoImplTest {

    final RowMapper<Course> courseRowMapper = new CourseRowMapper();

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CourseDao courseDao;

    @Test
    void save_shouldNullPointerException_whenCourseIsNull() {
        assertThrows(NullPointerException.class, () -> courseDao.save(null));
    }

    @Test
    void save_shouldDataIntegrityViolationException_whenCourseFieldsNotInitialized() {
        Course course = new Course();

        assertThrows(DataIntegrityViolationException.class, () -> courseDao.save(course));
    }

    @Test
    void save_shouldDataIntegrityViolationException_whenCourseNameContainsMoreThanTwentyFiveCharacters() {
        Course course = new Course("CourseNameThatContainsMoreThanTwentyFiveCharacters", "Description");

        assertThrows(DataIntegrityViolationException.class, () -> courseDao.save(course));
    }

    @Test
    void save_shouldSavedOneCourse_whenCourseNameAndDescriptionIsEmpty() {
        Course expectedCourse = new Course("", "");
        expectedCourse.setId(4);
        String selectTestDataScript = """
                SELECT * FROM courses
                WHERE course_id = 4;
                """;

        courseDao.save(expectedCourse);
        Course actualCourse = jdbcTemplate.queryForObject(selectTestDataScript, courseRowMapper);

        assertEquals(expectedCourse, actualCourse);
    }

    @Test
    void save_shouldSavedOneCourse_whenCourseIsCorrect() {
        Course expectedCourse = new Course("CourseName", "Description");
        expectedCourse.setId(4);
        String selectTestDataScript = """
                SELECT * FROM courses
                WHERE course_id = 4;
                """;

        courseDao.save(expectedCourse);
        Course actualCourse = jdbcTemplate.queryForObject(selectTestDataScript, courseRowMapper);

        assertEquals(expectedCourse, actualCourse);
    }

    @Test
    void find_shouldCourseThatExistInCourseTable_whenCourseWithEnteredIdExist() {
        int courseId = 2;
        Course expectedCourse = new Course("CourseName_2", "Description_2");
        expectedCourse.setId(courseId);

        Course actualCourse = courseDao.find(courseId);

        assertEquals(expectedCourse, actualCourse);
    }

    @Test
    void find_shouldNull_whenNoCourseWithGivenId() {
        int courseIdThatNotExist = 4;

        Course actualCourse = courseDao.find(courseIdThatNotExist);

        assertNull(actualCourse);
    }

    @Test
    @Sql("/sql/clear_tables.sql")
    void findAll_shouldEmptyCoursesList_whenCoursesTableEmpty() {
        List<Course> exceptAllAvailableCourses = courseDao.findAll();

        assertTrue(exceptAllAvailableCourses.isEmpty());
    }

    @Test
    void findAll_shouldListAllAvailableCoursesInTable_whenCoursesTableContainsCourses() {
        List<Course> actualAllAvailableCourses = courseDao.findAll();

        assertEquals(3, actualAllAvailableCourses.size());
    }

    @Test
    void update_shouldUpdatedCourseNameAndDescription_whenCourseWithGivenIdExist() {
        Course expectedNewCourse = new Course("NewCourseName", "NewDescription");
        expectedNewCourse.setId(2);
        String selectTestDataScript = """
                SELECT * FROM courses
                WHERE course_id = 2;
                """;

        int numberOfChanges = courseDao.update(expectedNewCourse);
        Course actualUpdatedCourse = jdbcTemplate.queryForObject(selectTestDataScript, courseRowMapper);

        assertEquals(1, numberOfChanges);
        assertEquals(expectedNewCourse, actualUpdatedCourse);
    }

    @Test
    void update_shouldNoAnuChanges_whenNoCourseWithGivenId() {
        Course expectedNewCourse = new Course("NewCourseName", "NewDescription");
        expectedNewCourse.setId(5);

        int numberOfChanges = courseDao.update(expectedNewCourse);

        assertEquals(0, numberOfChanges);
    }

    @Test
    void delete_shouldDeletedCourse_whenCourseWithGivenIdExist() {
        int courseId = 1;
        String selectTestDataScript = """
                SELECT * FROM courses
                WHERE course_id = 1;
                """;

        int numberOfChanges = courseDao.delete(courseId);

        assertEquals(1, numberOfChanges);
        assertThrows(EmptyResultDataAccessException.class,
                () -> jdbcTemplate.queryForObject(selectTestDataScript, courseRowMapper));
    }

    @Test
    void delete_shouldNothingDeleted_whenNoCourseWithGivenId() {
        int courseId = 6;

        int numberOfChanges = courseDao.delete(courseId);

        assertEquals(0, numberOfChanges);
    }

    @Test
    void findCoursesForStudent_shouldNullPointerException_whenStudentIsNull() {
        assertThrows(NullPointerException.class, () -> courseDao.findCoursesForStudent(null));
    }

    @Test
    @Sql(statements = "DELETE FROM students_courses;")
    void findCoursesForStudent_shouldEmptyCoursesListForStudent_whenStudentIsNotRegisteredOnCourses() {
        Student student = new Student("FirstName", "LastName", 1);
        student.setId(1);
        List<Course> actualStudentCourses = courseDao.findCoursesForStudent(student);

        assertTrue(actualStudentCourses.isEmpty());
    }

    @Test
    void findCoursesForStudent_shouldEmptyCoursesListForStudent_whenThereIsNoSuchStudentInStudentsTable() {
        Student otherStudent = new Student("OtherFirstName", "OtherLastName", 1);
        otherStudent.setId(2);

        List<Course> actualStudentCourses = courseDao.findCoursesForStudent(otherStudent);

        assertTrue(actualStudentCourses.isEmpty());
    }

    @Test
    void findCoursesForStudent_shouldCoursesListForStudent_whenStudentIsRegisteredForTheseCourses() {
        Student student = new Student("FirstName", "LastName", 1);
        student.setId(1);

        List<Course> actualStudentCourses = courseDao.findCoursesForStudent(student);

        assertEquals("CourseName_1", actualStudentCourses.get(0).getCourseName());
        assertEquals("CourseName_2", actualStudentCourses.get(1).getCourseName());
        assertEquals("CourseName_3", actualStudentCourses.get(2).getCourseName());
    }

}
