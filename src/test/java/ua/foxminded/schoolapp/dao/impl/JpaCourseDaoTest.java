package ua.foxminded.schoolapp.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;
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
import ua.foxminded.schoolapp.dao.CourseDao;
import ua.foxminded.schoolapp.model.Course;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
       classes = { JpaCourseDao.class }
))
@ContextConfiguration(classes = TestApplicationConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = { "/sql/clear_tables.sql", "/sql/courses_test_init.sql" },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class JpaCourseDaoTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CourseDao courseDao;

    @Test
    void save_shouldIllegalArgumentException_whenCourseIsNull() {
        Course course = null;

        assertThrows(IllegalArgumentException.class, () -> courseDao.save(course));
    }

    @Test
    void save_shouldConstraintViolationException_whenCourseFieldsNotInitialized() {
        Course course = new Course();

        assertThrows(ConstraintViolationException.class, () -> courseDao.save(course));
    }

    @Test
    void save_shouldDataException_whenCourseNameContainsMoreThanTwentyFiveCharacters() {
        Course course = new Course("CourseNameThatContainsMoreThanTwentyFiveCharacters", "Description");

        assertThrows(DataException.class, () -> courseDao.save(course));
    }

    @Test
    void save_shouldSavedOneCourse_whenCourseNameAndDescriptionAreEmpty() {
        Course expectedCourse = new Course("", "");

        courseDao.save(expectedCourse);
        Course actualCourse = entityManager.find(Course.class, 4);

        assertEquals(expectedCourse, actualCourse);
    }

    @Test
    void save_shouldSavedOneCourse_whenCourseIsCorrect() {
        Course expectedCourse = new Course("CourseName", "Description");

        courseDao.save(expectedCourse);
        Course actualCourse = entityManager.find(Course.class, 4);

        assertEquals(expectedCourse, actualCourse);
    }

    @Test
    void find_shouldCourseThatExistInCourseTable_whenCourseWithGivenIdExist() {
        Integer expectedCourseId = 2;
        String expectedCourseCourseName = "CourseName_2";
        String expectedCourseDescription = "Description_2";

        Course actualCourse = courseDao.find(expectedCourseId);

        assertEquals(expectedCourseId, actualCourse.getId());
        assertEquals(expectedCourseCourseName, actualCourse.getCourseName());
        assertEquals(expectedCourseDescription, actualCourse.getDescription());
    }

    @Test
    void find_shouldNull_whenNoCourseWithGivenId() {
        Integer courseIdThatNotExist = 4;

        Course actualCourse = courseDao.find(courseIdThatNotExist);

        assertNull(actualCourse);
    }

    @Test
    void find_shouldIllegalArgumentException_whenCourseIdIsNull() {
        Integer courseIdThatNotExist = null;

        assertThrows(IllegalArgumentException.class, () -> courseDao.find(courseIdThatNotExist));
    }

    @Test
    void findCourseByName_shouldCourseThatExistInCourseTable_whenCourseWithGivenNameIsExist() {
        String expectedCourseCourseName = "CourseName_1";

        Course actualCourse = courseDao.findCourseByName(expectedCourseCourseName).get();

        assertEquals(expectedCourseCourseName, actualCourse.getCourseName());
    }

    @Test
    void findCourseByName_shouldEmptyOptional_whenCourseWithGivenNameExist() {
        String expectedCourseCourseName = "CourseName_4";

        Optional<Course> actualCourse = courseDao.findCourseByName(expectedCourseCourseName);

        assertTrue(actualCourse.isEmpty());
    }

    @Test
    void findCourseByName_shouldEmptyOptional_whenCourseNameIsNull() {
        String expectedCourseCourseName = null;

        Optional<Course> actualCourse = courseDao.findCourseByName(expectedCourseCourseName);

        assertTrue(actualCourse.isEmpty());
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
    void update_shouldUpdatedCourseNameAndDescription_whenCourseWithGivenIdIsExist() {
        Course expectedNewCourse = new Course("NewCourseName", "NewDescription");
        expectedNewCourse.setId(2);

        Course updatedCourse = courseDao.update(expectedNewCourse);

        assertEquals(expectedNewCourse, updatedCourse);
    }

    @Test
    void update_shouldUpdatedCourseNameAndDescription_whenCourseWithGivenIdExistAndNewDescriptionIsNull() {
        Course expectedNewCourse = new Course("NewCourseName", null);
        expectedNewCourse.setId(2);

        Course updatedCourse = courseDao.update(expectedNewCourse);

        assertEquals(expectedNewCourse, updatedCourse);
    }

    @Test
    void update_shouldSavedNewCourseWithAnotherId_whenNoCourseWithGivenId() {
        Course expectedNewCourse = new Course("NewCourseName", "NewDescription");
        expectedNewCourse.setId(5);

        Course updatedCourse = courseDao.update(expectedNewCourse);

        assertEquals(4, updatedCourse.getId());
        assertEquals(expectedNewCourse, updatedCourse);
    }

    @Test
    void update_shouldIllegalArgumentException_whenCourseIsNull() {
        Course expectedNewCourse = null;

        assertThrows(IllegalArgumentException.class, () -> courseDao.update(expectedNewCourse));
    }

    @Test
    void delete_shouldDeletedCourse_whenCourseWithGivenIdExist() {
        Integer courseId = 1;

        courseDao.delete(courseId);
        Course deletedCourse = entityManager.find(Course.class, courseId);

        assertNull(deletedCourse);
    }

    @Test
    void delete_shouldNothingDeleted_whenNoCourseWithGivenId() {
        Integer courseId = 6;

        courseDao.delete(courseId);
        Course deletedCourse = entityManager.find(Course.class, courseId);

        assertNull(deletedCourse);
    }

    @Test
    void delete_shouldIllegalArgumentException_whenCourseIdIsNull() {
        Integer courseId = null;

        assertThrows(IllegalArgumentException.class, () -> courseDao.delete(courseId));
    }

}
