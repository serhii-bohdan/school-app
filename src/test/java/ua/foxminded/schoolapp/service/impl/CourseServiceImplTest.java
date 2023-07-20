package ua.foxminded.schoolapp.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolapp.dao.CourseDao;
import ua.foxminded.schoolapp.datasetup.Generatable;
import ua.foxminded.schoolapp.exception.DaoException;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.CourseService;

class CourseServiceImplTest {

    Generatable<Course> coursesGeneratorMock;
    CourseDao courseDaoMock;
    CourseService courseService;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        coursesGeneratorMock = mock(Generatable.class);
        courseDaoMock = mock(CourseDao.class);
    }

    @Test
    void CourseServiceImpl_shouldNullPointerException_whenCoursesGeneratorIsNull() {
        assertThrows(NullPointerException.class, () -> new CourseServiceImpl(null, courseDaoMock));
    }

    @Test
    void CourseServiceImpl_shouldNullPointerException_whenCourseDaoIsNull() {
        assertThrows(NullPointerException.class, () -> new CourseServiceImpl(coursesGeneratorMock, null));
    }

    @Test
    void initCourses_shouldDaoException_whenCourseDaoThrowDaoException() {
        courseService = new CourseServiceImpl(coursesGeneratorMock, courseDaoMock);
        List<Course> generatedCourses = new ArrayList<>();
        Course course = new Course("CourseName", "Description");
        course.setId(1);
        generatedCourses.add(course);
        when(coursesGeneratorMock.toGenerate()).thenReturn(generatedCourses);
        when(courseDaoMock.save(course)).thenThrow(DaoException.class);

        assertThrows(DaoException.class, () -> courseService.initCourses());
        verify(coursesGeneratorMock, times(1)).toGenerate();
        verify(courseDaoMock, times(1)).save(course);
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
    void getAllCourses_shouldDaoException_whenCourseDaoThrowDaoException() {
        courseService = new CourseServiceImpl(coursesGeneratorMock, courseDaoMock);
        when(courseDaoMock.findAll()).thenThrow(DaoException.class);

        assertThrows(DaoException.class, () -> courseService.getAllCourses());
        verify(courseDaoMock, times(1)).findAll();
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
    void getCoursesForStudent_shouldDaoException_whenCourseDaoThrowDaoException() {
        courseService = new CourseServiceImpl(coursesGeneratorMock, courseDaoMock);
        Student student = new Student("FirstName", "LastName", 1);
        student.setId(1);
        when(courseDaoMock.findCoursesForStudent(student)).thenThrow(DaoException.class);

        assertThrows(DaoException.class, () -> courseService.getCoursesForStudent(student));
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
