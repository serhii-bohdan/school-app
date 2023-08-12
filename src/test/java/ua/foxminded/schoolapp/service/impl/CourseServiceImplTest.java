package ua.foxminded.schoolapp.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ua.foxminded.schoolapp.TestAppConfig;
import ua.foxminded.schoolapp.dao.CourseDao;
import ua.foxminded.schoolapp.datasetup.Generatable;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Student;

@SpringBootTest
@ContextConfiguration(classes = TestAppConfig.class)
class CourseServiceImplTest {

    @Mock
    Generatable<Course> coursesGeneratorMock;

    @Mock
    CourseDao courseDaoMock;

    @InjectMocks
    CourseServiceImpl courseService;

    @Test
    void CourseServiceImpl_shouldNullPointerException_whenCoursesGeneratorIsNull() {
        assertThrows(NullPointerException.class, () -> new CourseServiceImpl(null, courseDaoMock));
    }

    @Test
    void CourseServiceImpl_shouldNullPointerException_whenCourseDaoIsNull() {
        assertThrows(NullPointerException.class, () -> new CourseServiceImpl(coursesGeneratorMock, null));
    }

    @Test
    void initCourses_shouldSavedAllCoursesWhichReturnsCoursesGenerator_whenCoursesGeneratorReturnsSeveralCourses() {
        courseService = new CourseServiceImpl(coursesGeneratorMock, courseDaoMock);
        List<Course> generatedCourses = new ArrayList<>();
        Course firstCourse = new Course("CourseName_1", "Description_1");
        Course secondCourse = new Course("CourseName_2", "Description_2");
        Course thirdCourse = new Course("CourseName_3", "Description_3");
        firstCourse.setId(1);
        secondCourse.setId(2);
        thirdCourse.setId(3);
        generatedCourses.add(firstCourse);
        generatedCourses.add(secondCourse);
        generatedCourses.add(thirdCourse);
        when(coursesGeneratorMock.toGenerate()).thenReturn(generatedCourses);

        courseService.initCourses();

        verify(coursesGeneratorMock, times(1)).toGenerate();
        verify(courseDaoMock, times(1)).save(firstCourse);
        verify(courseDaoMock, times(1)).save(secondCourse);
        verify(courseDaoMock, times(1)).save(thirdCourse);
    }

    @Test
    void initCourses_shouldNotSaveAnyCourse_whenCoursesGeneratorReturnsEmptyCoursesList() {
        courseService = new CourseServiceImpl(coursesGeneratorMock, courseDaoMock);
        List<Course> generatedCourses = new ArrayList<>();
        when(coursesGeneratorMock.toGenerate()).thenReturn(generatedCourses);

        courseService.initCourses();

        verify(coursesGeneratorMock, times(1)).toGenerate();
        verify(courseDaoMock, never()).save(any(Course.class));
    }

    @Test
    void getAllCourses_shouldCoursesListWithSeveralCourses_whenCourseDaoReturnThisList() {
        courseService = new CourseServiceImpl(coursesGeneratorMock, courseDaoMock);
        List<Course> expectedCourses = new ArrayList<>();
        Course firstCourse = new Course("CourseName_1", "Description_1");
        Course secondCourse = new Course("CourseName_2", "Description_2");
        Course thirdCourse = new Course("CourseName_3", "Description_3");
        firstCourse.setId(1);
        secondCourse.setId(2);
        thirdCourse.setId(3);
        expectedCourses.add(firstCourse);
        expectedCourses.add(secondCourse);
        expectedCourses.add(thirdCourse);
        when(courseDaoMock.findAll()).thenReturn(expectedCourses);

        List<Course> actualCourses = courseService.getAllCourses();

        assertEquals(expectedCourses, actualCourses);
        verify(courseDaoMock, times(1)).findAll();
    }

    @Test
    void getCoursesForStudent_shouldNullPointerException_whenStudentIsNull() {
        courseService = new CourseServiceImpl(coursesGeneratorMock, courseDaoMock);
        Student student = null;
        when(courseDaoMock.findCoursesForStudent(student)).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> courseService.getCoursesForStudent(student));
        verify(courseDaoMock, times(1)).findCoursesForStudent(student);
    }

    @Test
    void getCoursesForStudent_shouldCoursesListForStudent_whenCourseDaoReturnThisList() {
        courseService = new CourseServiceImpl(coursesGeneratorMock, courseDaoMock);
        Student student = new Student("FirstName", "LastName", 1);
        student.setId(1);
        List<Course> expectedCourses = new ArrayList<>();
        Course firstCourse = new Course("CourseName_1", "Description_1");
        Course secondCourse = new Course("CourseName_2", "Description_2");
        Course thirdCourse = new Course("CourseName_3", "Description_3");
        firstCourse.setId(1);
        secondCourse.setId(2);
        thirdCourse.setId(3);
        expectedCourses.add(firstCourse);
        expectedCourses.add(secondCourse);
        expectedCourses.add(thirdCourse);
        when(courseDaoMock.findCoursesForStudent(student)).thenReturn(expectedCourses);

        List<Course> actualCourses = courseService.getCoursesForStudent(student);

        assertEquals(expectedCourses, actualCourses);
        verify(courseDaoMock, times(1)).findCoursesForStudent(student);
    }

}
