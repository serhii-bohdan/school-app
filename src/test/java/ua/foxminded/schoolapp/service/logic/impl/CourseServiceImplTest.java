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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.schoolapp.dto.CourseDto;
import ua.foxminded.schoolapp.dto.mapper.CourseMapper;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.repository.CourseRepository;
import ua.foxminded.schoolapp.service.generate.Generatable;

@SpringBootTest(classes = { CourseServiceImpl.class })
class CourseServiceImplTest {

    @MockBean
    private Generatable<CourseDto> coursesGeneratorMock;

    @MockBean
    private CourseRepository courseRepositoryMock;

    @Autowired
    private CourseServiceImpl courseService;

    @Test
    void initCourses_shouldSavedAllCoursesWhichReturnsCoursesGenerator_whenCoursesGeneratorReturnsSeveralCourses() {
        List<CourseDto> generatedCourses = new ArrayList<>();
        generatedCourses.add(new CourseDto());
        generatedCourses.add(new CourseDto());
        generatedCourses.add(new CourseDto());
        when(coursesGeneratorMock.toGenerate()).thenReturn(generatedCourses);

        courseService.initCourses();

        verify(coursesGeneratorMock, times(1)).toGenerate();
        verify(courseRepositoryMock, times(3)).save(any(Course.class));
    }

    @Test
    void initCourses_shouldNotSaveAnyCourse_whenCoursesGeneratorReturnsEmptyCoursesList() {
        List<CourseDto> generatedCourses = new ArrayList<>();
        when(coursesGeneratorMock.toGenerate()).thenReturn(generatedCourses);

        courseService.initCourses();

        verify(coursesGeneratorMock, times(1)).toGenerate();
        verify(courseRepositoryMock, never()).save(any(Course.class));
    }

    @Test
    void addCourse_shouldIllegalArgumentException_whenCourseDtoIsNull() {
        CourseDto courseDto = null;

        assertThrows(IllegalArgumentException.class, () -> courseService.addCourse(courseDto));
    }

    @Test
    void addCourse_shouldAddedNewCourseAndReturnedCourseOptional_whenCourseRepositorySuccessfulSaveNewCourse() {
        CourseDto courseDto = new CourseDto("CourseName", "Description");
        Course expectedCourse = CourseMapper.mapDtoToCourse(courseDto);
        when(courseRepositoryMock.save(expectedCourse)).thenReturn(expectedCourse);

        Optional<Course> actualCourse = courseService.addCourse(courseDto);

        verify(courseRepositoryMock, times(1)).save(expectedCourse);
        assertTrue(actualCourse.isPresent());
        assertEquals(expectedCourse, actualCourse.get());
    }

    @Test
    void getCourseById_shouldReturnedCourseOptioanal_whenCourseRepositoryFoundCourseWithGivenId() {
        Integer courseId = 1;
        Course expectedCourse = new Course("NewCourse", "Description");
        expectedCourse.setId(courseId);
        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.of(expectedCourse));

        Optional<Course> actualCourse = courseService.getCourseById(courseId);

        verify(courseRepositoryMock, times(1)).findById(courseId);
        assertTrue(actualCourse.isPresent());
        assertEquals(expectedCourse, actualCourse.get());
    }

    @Test
    void getCourseById_shouldEmptyOptioanal_whenCourseRepositoryNotFoundCourseWithGivenId() {
        Integer courseId = -1;
        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.empty());

        Optional<Course> actualCourse = courseService.getCourseById(courseId);

        verify(courseRepositoryMock, times(1)).findById(courseId);
        assertTrue(actualCourse.isEmpty());
    }

    @Test
    void getCourseByName_shouldCourseOptioanal_whenCourseRepositoryFoundCourseWithGivenName() {
        String courseName = "CourseName";
        Course expectedCourse = new Course(courseName, "Description");
        when(courseRepositoryMock.findByCourseName(courseName)).thenReturn(Optional.of(expectedCourse));

        Optional<Course> actualCourse = courseService.getCourseByName(courseName);

        verify(courseRepositoryMock, times(1)).findByCourseName(courseName);
        assertTrue(actualCourse.isPresent());
        assertEquals(expectedCourse, actualCourse.get());
    }

    @Test
    void getCourseByName_shouldEmptyOptioanal_whenCourseRepositoryNotFoundCourseWithGivenName() {
        String courseName = "NotExistent";
        when(courseRepositoryMock.findByCourseName(courseName)).thenReturn(Optional.empty());

        Optional<Course> actualCourse = courseService.getCourseByName(courseName);

        verify(courseRepositoryMock, times(1)).findByCourseName(courseName);
        assertTrue(actualCourse.isEmpty());
    }

    @Test
    void getCourseByName_shouldEmptyOptioanal_whenGivenCourseNameIsNull() {
        String courseName = null;
        when(courseRepositoryMock.findByCourseName(courseName)).thenReturn(Optional.empty());

        Optional<Course> actualCourse = courseService.getCourseByName(courseName);

        verify(courseRepositoryMock, times(1)).findByCourseName(courseName);
        assertTrue(actualCourse.isEmpty());
    }

    @Test
    void getAllCourses_shouldCoursesList_whenCourseRepositoryFoundAllCourses() {
        List<Course> expectedCourses = new ArrayList<Course>();
        expectedCourses.add(new Course());
        expectedCourses.add(new Course());
        expectedCourses.add(new Course());
        when(courseRepositoryMock.findAll()).thenReturn(expectedCourses);

        List<Course> actualCourses = courseService.getAllCourses();

        verify(courseRepositoryMock, times(1)).findAll();
        assertEquals(expectedCourses, actualCourses);
    }

    @Test
    void getAllCourses_shouldEmptyCoursesList_whenCourseRepositoryNotFoundAnyCourse() {
        List<Course> expectedCourses = new ArrayList<Course>();
        when(courseRepositoryMock.findAll()).thenReturn(expectedCourses);

        List<Course> actualCourses = courseService.getAllCourses();

        verify(courseRepositoryMock, times(1)).findAll();
        assertTrue(actualCourses.isEmpty());
        assertEquals(expectedCourses, actualCourses);
    }

    @Test
    void updateCourse_shouldReturnedUpdatedCourseOptioanal_whenCourseRepositorySuccessfulUpdateCourse() {
        Course updatedCourse = new Course("CourseName", "Description");
        updatedCourse.setId(1);
        when(courseRepositoryMock.save(updatedCourse)).thenReturn(updatedCourse);

        Optional<Course> actualCourse = courseService.updateCourse(updatedCourse);

        verify(courseRepositoryMock, times(1)).save(updatedCourse);
        assertTrue(actualCourse.isPresent());
        assertEquals(updatedCourse, actualCourse.get());
    }

    @Test
    void deleteCourseByName_shouldDeletedCourse_whenCourseWithGivenNameIsExist() {
        Integer courseId = 1;
        String courseName = "CourseName";
        String description = "Description";
        Course courseToDelete = new Course(courseName, description);
        courseToDelete.setId(courseId);
        when(courseService.getCourseByName(courseName)).thenReturn(Optional.of(courseToDelete));

        courseService.deleteCourseByName(courseName);

        verify(courseRepositoryMock, times(1)).delete(courseToDelete);
    }

    @Test
    void deleteCourseByName_shouldNothingDeleted_whenNoCourseWithGivenName() {
        String courseName = "NotExistent";
        when(courseService.getCourseByName(courseName)).thenReturn(Optional.empty());

        courseService.deleteCourseByName(courseName);

        verify(courseRepositoryMock, never()).delete(any(Course.class));
    }

}
