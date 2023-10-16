package ua.foxminded.schoolapp.service.logic.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.schoolapp.dao.CourseDao;
import ua.foxminded.schoolapp.dto.CourseDto;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.service.generate.Generatable;

@SpringBootTest(classes = { CourseServiceImpl.class })
class CourseServiceImplTest {

    @MockBean
    Generatable<CourseDto> coursesGeneratorMock;

    @MockBean
    CourseDao courseDaoMock;

    @Autowired
    CourseServiceImpl courseService;

    @Test
    void initCourses_shouldSavedAllCoursesWhichReturnsCoursesGenerator_whenCoursesGeneratorReturnsSeveralCourses() {
        List<CourseDto> generatedCourses = new ArrayList<>();
        generatedCourses.add(new CourseDto());
        generatedCourses.add(new CourseDto());
        generatedCourses.add(new CourseDto());
        when(coursesGeneratorMock.toGenerate()).thenReturn(generatedCourses);

        courseService.initCourses();

        verify(coursesGeneratorMock, times(1)).toGenerate();
        verify(courseDaoMock, times(3)).save(any(Course.class));
    }

    @Test
    void initCourses_shouldNotSaveAnyCourse_whenCoursesGeneratorReturnsEmptyCoursesList() {
        List<CourseDto> generatedCourses = new ArrayList<>();
        when(coursesGeneratorMock.toGenerate()).thenReturn(generatedCourses);

        courseService.initCourses();

        verify(coursesGeneratorMock, times(1)).toGenerate();
        verify(courseDaoMock, never()).save(any(Course.class));
    }

    @Test
    void addCourse_shouldAddedNewCourseAndReturnedCourseOptional_whenCourseDaoSuccessfulSaveNewCourse() {
        String courseName = "CourseName";
        String descrittion = "Description";
        Course newExpectedCourse = new Course(courseName, descrittion);
        when(courseDaoMock.save(newExpectedCourse)).thenReturn(newExpectedCourse);

        Optional<Course> actualCourse = courseService.addCourse(courseName, descrittion);

        verify(courseDaoMock, times(1)).save(newExpectedCourse);
        assertTrue(actualCourse.isPresent());
        assertEquals(newExpectedCourse, actualCourse.get());
    }

    @Test
    void addCourse_shouldConstraintViolationException_whenCourseNameIsNullAndCourseDaoThrowConstraintViolationException() {
        String courseName = null;
        String descrittion = "Description";
        Course newExpectedCourse = new Course(courseName, descrittion);
        when(courseDaoMock.save(newExpectedCourse)).thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class, () -> courseService.addCourse(courseName, descrittion));
        verify(courseDaoMock, times(1)).save(newExpectedCourse);
    }

    @Test
    void addCourse_shouldDataException_whenCourseNameContainsMoreThanTwentyFiveCharactersAndCourseDaoThrowDataException() {
        String courseName = "NnnneeeewwwwCcccooouuurrrssseeee";
        String descrittion = "Description";
        Course newExpectedCourse = new Course(courseName, descrittion);
        when(courseDaoMock.save(newExpectedCourse)).thenThrow(DataException.class);

        assertThrows(DataException.class, () -> courseService.addCourse(courseName, descrittion));
        verify(courseDaoMock, times(1)).save(newExpectedCourse);
    }

    @Test
    void getCourseById_shouldReturnedCourseOptioanal_whenCourseDaoFoundCourseWithGivenId() {
        Integer courseId = 1;
        Course expectedCourse = new Course("NewCourse", "Description");
        expectedCourse.setId(courseId);
        when(courseDaoMock.find(courseId)).thenReturn(expectedCourse);

        Optional<Course> actualCourse = courseService.getCourseById(courseId);

        verify(courseDaoMock, times(1)).find(courseId);
        assertTrue(actualCourse.isPresent());
        assertEquals(expectedCourse, actualCourse.get());
    }

    @Test
    void getCourseById_shouldEmptyOptioanal_whenCourseDaoNotFoundCourseWithGivenId() {
        Integer courseId = -1;
        when(courseDaoMock.find(courseId)).thenReturn(null);

        Optional<Course> actualCourse = courseService.getCourseById(courseId);

        verify(courseDaoMock, times(1)).find(courseId);
        assertFalse(actualCourse.isPresent());
    }

    @Test
    void getCourseById_shouldEmptyOptioanal_whenGivenCourseIdIsNull() {
        Integer courseId = null;
        when(courseDaoMock.find(courseId)).thenReturn(null);

        Optional<Course> actualCourse = courseService.getCourseById(courseId);

        verify(courseDaoMock, times(1)).find(courseId);
        assertFalse(actualCourse.isPresent());
    }

    @Test
    void getCourseByName_shouldCourseOptioanal_whenCourseDaoFoundCourseWithGivenName() {
        String courseName = "CourseName";
        Course expectedCourse = new Course(courseName, "Description");
        when(courseDaoMock.findCourseByName(courseName)).thenReturn(Optional.of(expectedCourse));

        Optional<Course> actualCourse = courseService.getCourseByName(courseName);

        verify(courseDaoMock, times(1)).findCourseByName(courseName);
        assertTrue(actualCourse.isPresent());
        assertEquals(expectedCourse, actualCourse.get());
    }

    @Test
    void getCourseByName_shouldEmptyOptioanal_whenCourseDaoNotFoundCourseWithGivenName() {
        String courseName = "NotExistent";
        when(courseDaoMock.findCourseByName(courseName)).thenReturn(Optional.empty());

        Optional<Course> actualCourse = courseService.getCourseByName(courseName);

        verify(courseDaoMock, times(1)).findCourseByName(courseName);
        assertTrue(actualCourse.isEmpty());
    }

    @Test
    void getCourseByName_shouldEmptyOptioanal_whenGivenCourseNameIsNull() {
        String courseName = null;
        when(courseDaoMock.findCourseByName(courseName)).thenReturn(Optional.empty());

        Optional<Course> actualCourse = courseService.getCourseByName(courseName);

        verify(courseDaoMock, times(1)).findCourseByName(courseName);
        assertTrue(actualCourse.isEmpty());
    }

    @Test
    void getAllCourses_shouldCoursesList_whenCourseDaoFoundAllCourses() {
        List<Course> expectedCourses = new ArrayList<Course>();
        expectedCourses.add(new Course());
        expectedCourses.add(new Course());
        expectedCourses.add(new Course());
        when(courseDaoMock.findAll()).thenReturn(expectedCourses);

        List<Course> actualCourses = courseService.getAllCourses();

        verify(courseDaoMock, times(1)).findAll();
        assertEquals(expectedCourses, actualCourses);
    }

    @Test
    void getAllCourses_shouldEmptyCoursesList_whenCourseDaoNotFoundAnyCourse() {
        List<Course> expectedCourses = new ArrayList<Course>();
        when(courseDaoMock.findAll()).thenReturn(expectedCourses);

        List<Course> actualCourses = courseService.getAllCourses();

        verify(courseDaoMock, times(1)).findAll();
        assertTrue(actualCourses.isEmpty());
        assertEquals(expectedCourses, actualCourses);
    }

    @Test
    void updateCourse_shouldReturnedUdatedCourseOptioanal_whenCourseDaoSuccessfulUpdateCourse() {
        Course updatedCourse = new Course("CourseName", "Description");
        updatedCourse.setId(1);
        when(courseDaoMock.update(updatedCourse)).thenReturn(updatedCourse);

        Optional<Course> actualCourse = courseService.updateCourse(updatedCourse);

        verify(courseDaoMock, times(1)).update(updatedCourse);
        assertTrue(actualCourse.isPresent());
        assertEquals(updatedCourse, actualCourse.get());
    }

    @Test
    void updateCourse_shouldIllegalArgumentException_whenUpdatedCourseIsNullAndCourseDaoThrowIllegalArgumentException() {
        Course uapdatedCourse = null;
        when(courseDaoMock.update(uapdatedCourse)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> courseService.updateCourse(uapdatedCourse));
        verify(courseDaoMock, times(1)).update(uapdatedCourse);
    }

    @Test
    void deleteCourseByName_shouldDeletingCourse_whenCourseWasFoundSuccessfullyByGivenName() {
        Integer courseId = 1;
        String courseName = "CourseName";
        String description = "Description";
        Course groupToDelete = new Course(courseName, description);
        groupToDelete.setId(courseId);
        when(courseService.getCourseByName(courseName)).thenReturn(Optional.of(groupToDelete));

        courseService.deleteCourseByName(courseName);

        verify(courseDaoMock, times(1)).delete(courseId);
    }

    @Test
    void deleteCourseByName_shouldNothingDeleted_whenCourseNotFoundByGivenName() {
        Integer courseId = 1;
        String courseName = null;
        String description = "Description";
        Course groupToDelete = new Course(courseName, description);
        groupToDelete.setId(courseId);
        when(courseService.getCourseByName(courseName)).thenReturn(Optional.empty());

        courseService.deleteCourseByName(courseName);

        verify(courseDaoMock, never()).delete(courseId);
    }

}
