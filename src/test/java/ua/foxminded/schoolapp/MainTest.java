package ua.foxminded.schoolapp;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ua.foxminded.schoolapp.cli.SchoolController;
import ua.foxminded.schoolapp.service.CourseService;
import ua.foxminded.schoolapp.service.GroupService;
import ua.foxminded.schoolapp.service.StudentService;

@SpringBootTest
@ContextConfiguration(classes = TestAppConfig.class)
class MainTest {

    @Mock
    GroupService groupServiceMock;

    @Mock
    StudentService studentServiceMock;

    @Mock
    CourseService courseServiceMock;

    @Mock
    SchoolController controllerMock;

    @InjectMocks
    Main mainMock;

    @Test
    void runSchoolApp_shouldInvokedAllMethodsInMainMethod_whenTheseMethodsWorkCorrectly() {
        mainMock.runSchoolApp();

        verify(groupServiceMock, times(1)).initGroups();
        verify(studentServiceMock, times(1)).initStudents();
        verify(courseServiceMock, times(1)).initCourses();
        verify(studentServiceMock, times(1)).initStudentsCoursesTable();
        verify(controllerMock, times(1)).runSchoolApp();
    }

}
