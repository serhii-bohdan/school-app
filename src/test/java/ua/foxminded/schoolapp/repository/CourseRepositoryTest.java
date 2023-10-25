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
import ua.foxminded.schoolapp.model.Course;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
       classes = { CourseRepository.class }
))
@ContextConfiguration(classes = TestApplicationConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = { "/sql/clear_tables.sql", "/sql/courses_test_init.sql" },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class CourseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void save_shouldInvalidDataAccessApiUsageException_whenCourseIsNull() {
        Course course = null;

        assertThrows(InvalidDataAccessApiUsageException.class, () -> courseRepository.save(course));
    }

    @Test
    void save_shouldDataIntegrityViolationException_whenCourseFieldsAreNulls() {
        Course course = new Course(null, null);

        assertThrows(DataIntegrityViolationException.class, () -> courseRepository.save(course));
    }

    @Test
    void save_shouldDataIntegrityViolationException_whenCourseNameContainsMoreThanTwentyFiveCharacters() {
        Course course = new Course("CourseNameThatContainsMoreThanTwentyFiveCharacters", "Description");

        assertThrows(DataIntegrityViolationException.class, () -> courseRepository.save(course));
    }

    @Test
    void save_shouldSavedOneCourse_whenCourseNameAndDescriptionAreEmpty() {
        Course expectedCourse = new Course("", "");

        courseRepository.save(expectedCourse);
        Course actualCourse = entityManager.find(Course.class, 4);

        assertEquals(expectedCourse, actualCourse);
    }

    @Test
    void save_shouldSavedOneCourse_whenCourseIsCorrect() {
        Course expectedCourse = new Course("CourseName", "Description");

        courseRepository.save(expectedCourse);
        Course actualCourse = entityManager.find(Course.class, 4);

        assertEquals(expectedCourse, actualCourse);
    }

    @Test
    void save_shouldUpdatedCourseNameAndDescription_whenCourseAlreadyExist() {
        Course courseToUpdate = entityManager.find(Course.class, 1);
        String newCourseName = "NewCourseName";
        String newDescription = "NewDescription";
        courseToUpdate.setCourseName(newCourseName);
        courseToUpdate.setDescription(newDescription);

        Course updatedCourse = courseRepository.save(courseToUpdate);

        assertEquals(courseToUpdate, updatedCourse);
  }

    @Test
    void save_shouldUpdatedCourseNameAndDescription_whenCourseAlreadyExistAndNewCourseNameAndDescriptionAreNulls() {
        Course courseToUpdate = entityManager.find(Course.class, 1);
        String newCourseName = null;
        String newDescription = null;
        courseToUpdate.setCourseName(newCourseName);
        courseToUpdate.setDescription(newDescription);

        Course updatedCourse = courseRepository.save(courseToUpdate);

        assertEquals(courseToUpdate, updatedCourse);
        assertNull(updatedCourse.getCourseName());
        assertNull(updatedCourse.getDescription());
    }

    @Test
    void findById_shouldCourseThatExistInCourseTable_whenCourseWithGivenIdExist() {
        Integer expectedCourseId = 2;
        String expectedCourseCourseName = "CourseName_2";
        String expectedCourseDescription = "Description_2";

        Optional<Course> actualCourse = courseRepository.findById(expectedCourseId);

        assertEquals(expectedCourseId, actualCourse.get().getId());
        assertEquals(expectedCourseCourseName, actualCourse.get().getCourseName());
        assertEquals(expectedCourseDescription, actualCourse.get().getDescription());
    }

    @Test
    void findById_shouldEmptyOptional_whenNoCourseWithGivenId() {
        Integer courseIdThatNotExist = 4;

        Optional<Course> actualCourse = courseRepository.findById(courseIdThatNotExist);

        assertTrue(actualCourse.isEmpty());
    }

    @Test
    void findById_shouldInvalidDataAccessApiUsageException_whenCourseIdIsNull() {
        Integer courseIdIsNull = null;

        assertThrows(InvalidDataAccessApiUsageException.class, () -> courseRepository.findById(courseIdIsNull));
    }

    @Test
    void findByCourseName_shouldCourseThatExistInCourseTable_whenCourseWithGivenNameIsExist() {
        String expectedCourseCourseName = "CourseName_1";

        Course actualCourse = courseRepository.findByCourseName(expectedCourseCourseName).get();

        assertEquals(expectedCourseCourseName, actualCourse.getCourseName());
    }

    @Test
    void findByCourseName_shouldEmptyOptional_whenCourseWithGivenNameExist() {
        String expectedCourseCourseName = "CourseName_4";

        Optional<Course> actualCourse = courseRepository.findByCourseName(expectedCourseCourseName);

        assertTrue(actualCourse.isEmpty());
    }

    @Test
    void findByCourseName_shouldEmptyOptional_whenCourseNameIsNull() {
        String expectedCourseCourseName = null;

        Optional<Course> actualCourse = courseRepository.findByCourseName(expectedCourseCourseName);

        assertTrue(actualCourse.isEmpty());
    }

    @Test
    @Sql("/sql/clear_tables.sql")
    void findAll_shouldEmptyCoursesList_whenCoursesTableEmpty() {
        List<Course> exceptAllAvailableCourses = courseRepository.findAll();

        assertTrue(exceptAllAvailableCourses.isEmpty());
    }

    @Test
    void findAll_shouldListAllAvailableCoursesInTable_whenCoursesTableContainsCourses() {
        List<Course> actualAllAvailableCourses = courseRepository.findAll();

        assertEquals(3, actualAllAvailableCourses.size());
    }

    @Test
    void delete_shouldDeletedCourse_whenCourseWithGivenIdExist() {
        Course course = new Course("CourseName_1", "Description_1");
        Integer courseId = 1;
        course.setId(courseId);

        courseRepository.delete(course);
        Course deletedCourse = entityManager.find(Course.class, courseId);

        assertNull(deletedCourse);
    }

    @Test
    void delete_shouldNothingDeleted_whenNoCourseWithGivenData() {
        Course course = new Course("CourseName_6", "Description_6");
        Integer courseId = 6;
        course.setId(courseId);

        courseRepository.delete(course);
        Course deletedCourse = entityManager.find(Course.class, courseId);

        assertNull(deletedCourse);
    }

    @Test
    void delete_shouldInvalidDataAccessApiUsageException_whenCourseIsNull() {
        Course course = null;

        assertThrows(InvalidDataAccessApiUsageException.class, () -> courseRepository.delete(course));
    }

}
