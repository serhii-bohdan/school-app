package ua.foxminded.schoolapp.service.logic.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.schoolapp.dao.CourseDao;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.generate.Generatable;

@SpringBootTest(classes = { CourseServiceImpl.class })
class CourseServiceImplTest {

    @MockBean
    Generatable<Course> coursesGeneratorMock;

    @MockBean
    CourseDao courseDaoMock;

    @Autowired
    CourseServiceImpl courseService;

    @Test
    void initCourses_shouldSavedAllCoursesWhichReturnsCoursesGenerator_whenCoursesGeneratorReturnsSeveralCourses() {
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
        List<Course> generatedCourses = new ArrayList<>();
        when(coursesGeneratorMock.toGenerate()).thenReturn(generatedCourses);

        courseService.initCourses();

        verify(coursesGeneratorMock, times(1)).toGenerate();
        verify(courseDaoMock, never()).save(any(Course.class));
    }

    @Test
    void getAllCourses_shouldCoursesListWithSeveralCourses_whenCourseDaoReturnThisList() {
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
        Student student = null;
        when(courseDaoMock.findCoursesForStudent(student)).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> courseService.getCoursesForStudent(student));
        verify(courseDaoMock, times(1)).findCoursesForStudent(student);
    }

    @Test
    void getCoursesForStudent_shouldCoursesListForStudent_whenCourseDaoReturnThisList() {
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
