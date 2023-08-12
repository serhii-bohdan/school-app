package ua.foxminded.schoolapp.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ua.foxminded.schoolapp.TestAppConfig;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.CourseService;
import ua.foxminded.schoolapp.service.GroupService;
import ua.foxminded.schoolapp.service.StudentService;
import ua.foxminded.schoolapp.service.UserInputValidator;

@SpringBootTest
@ContextConfiguration(classes = TestAppConfig.class)
class ServiceFacadeImplTest {

    @Mock
    GroupService groupServiceMock;

    @Mock
    StudentService studentServiceMock;

    @Mock
    CourseService courseServiceMock;

    @Mock
    UserInputValidator validatorMock;

    @InjectMocks
    ServiceFacadeImpl serviceFacade;

    @Test
    void ServiceFacadeImpl_shouldNullPointerException_whenGroupServiceIsNull() {
        assertThrows(NullPointerException.class,
                () -> new ServiceFacadeImpl(null, studentServiceMock, courseServiceMock, validatorMock));
    }

    @Test
    void ServiceFacadeImpl_shouldNullPointerException_whenStudentServiceIsNull() {
        assertThrows(NullPointerException.class,
                () -> new ServiceFacadeImpl(groupServiceMock, null, courseServiceMock, validatorMock));
    }

    @Test
    void ServiceFacadeImpl_shouldNullPointerException_whenCourseServiceIsNull() {
        assertThrows(NullPointerException.class,
                () -> new ServiceFacadeImpl(groupServiceMock, studentServiceMock, null, validatorMock));
    }

    @Test
    void ServiceFacadeImpl_shouldNullPointerException_whenValidatorIsNull() {
        assertThrows(NullPointerException.class,
                () -> new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, null));
    }

    @Test
    void getGroupsWithGivenNumberOfStudents_shouldReturnedGroupsListWithStudentsNumber_whenAmountOfStudentsInGroupsRangingFromTenToThirty() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        int amountOfStudentsInGroups = 23;
        Map<Group, Integer> expectedGroupsWithTheirNumberOfStudents = new HashMap<>();
        expectedGroupsWithTheirNumberOfStudents.put(new Group("FH-62"), 22);
        expectedGroupsWithTheirNumberOfStudents.put(new Group("JK-04"), 19);
        expectedGroupsWithTheirNumberOfStudents.put(new Group("LB-15"), 15);
        when(validatorMock.validateAmountOfStudents(amountOfStudentsInGroups)).thenReturn(true);
        when(groupServiceMock.getGroupsWithGivenNumberOfStudents(amountOfStudentsInGroups))
                .thenReturn(expectedGroupsWithTheirNumberOfStudents);

        Map<Group, Integer> actualGroupsWithTheirNumberOfStudents = serviceFacade
                .getGroupsWithGivenNumberOfStudents(amountOfStudentsInGroups);

        assertEquals(expectedGroupsWithTheirNumberOfStudents, actualGroupsWithTheirNumberOfStudents);
    }

    @Test
    void getGroupsWithGivenNumberOfStudents_shouldReturnedNull_whenAmountOfStudentsInGroupsLessThenTen() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        int amountOfStudentsInGroups = 3;
        when(validatorMock.validateAmountOfStudents(amountOfStudentsInGroups)).thenReturn(false);

        Map<Group, Integer> actualGroupsWithTheirNumberOfStudents = serviceFacade
                .getGroupsWithGivenNumberOfStudents(amountOfStudentsInGroups);

        assertEquals(null, actualGroupsWithTheirNumberOfStudents);
    }

    @Test
    void getGroupsWithGivenNumberOfStudents_shouldReturnedNull_whenAmountOfStudentsInGroupsMoreThenThirty() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        int amountOfStudentsInGroups = 45;
        when(validatorMock.validateAmountOfStudents(amountOfStudentsInGroups)).thenReturn(false);

        Map<Group, Integer> actualGroupsWithTheirNumberOfStudents = serviceFacade
                .getGroupsWithGivenNumberOfStudents(amountOfStudentsInGroups);

        assertEquals(null, actualGroupsWithTheirNumberOfStudents);
    }

    @Test
    void getStudentsWithCoursesByCourseName_shouldMapWithStudentAndTheirCourses_whenCourseWithGivenNameExist() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        List<Student> studentsRelatedToCourse = new ArrayList<>();
        Student student = new Student("FirstName", "LastName", 1);
        studentsRelatedToCourse.add(student);
        List<Course> coursesForStudent = new ArrayList<>();
        String courseName = "CourseName_1";
        coursesForStudent.add(new Course(courseName, "Description_1"));
        coursesForStudent.add(new Course("CourseName_2", "Description_2"));
        Map<Student, List<Course>> expectedStudentWithTheirCourses = new HashMap<>();
        expectedStudentWithTheirCourses.put(student, coursesForStudent);
        when(validatorMock.validateCourseName(courseName)).thenReturn(true);
        when(studentServiceMock.getStudentsRelatedToCourse(courseName)).thenReturn(studentsRelatedToCourse);
        when(courseServiceMock.getCoursesForStudent(student)).thenReturn(coursesForStudent);

        Map<Student, List<Course>> actualStudentWithTheirCourses = serviceFacade
                .getStudentsWithCoursesByCourseName(courseName);

        assertEquals(expectedStudentWithTheirCourses, actualStudentWithTheirCourses);
    }

    @Test
    void getStudentsWithCoursesByCourseName_shouldEmptyMap_whenCourseWithGivenNameExistButThereNoStudentsRegisteredForThisCourse() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        String courseName = "CourseName_1";
        List<Student> studentsRelatedToCourse = new ArrayList<>();
        Map<Student, List<Course>> expectedStudentWithTheirCourses = new HashMap<>();
        when(validatorMock.validateCourseName(courseName)).thenReturn(true);
        when(studentServiceMock.getStudentsRelatedToCourse(courseName)).thenReturn(studentsRelatedToCourse);

        Map<Student, List<Course>> actualStudentWithTheirCourses = serviceFacade
                .getStudentsWithCoursesByCourseName(courseName);

        assertEquals(expectedStudentWithTheirCourses, actualStudentWithTheirCourses);
        verify(studentServiceMock, times(1)).getStudentsRelatedToCourse(courseName);
        verify(courseServiceMock, never()).getCoursesForStudent(any(Student.class));
    }

    @Test
    void getStudentsWithCoursesByCourseName_shouldReturnedNull_whenNoCourseWithGivenName() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        String courseName = "CourseNameThatNotExist";
        when(validatorMock.validateCourseName(courseName)).thenReturn(false);

        Map<Student, List<Course>> actualStudentWithTheirCourses = serviceFacade
                .getStudentsWithCoursesByCourseName(courseName);

        assertEquals(null, actualStudentWithTheirCourses);
    }

    @Test
    void addNewStudent_shouldAddedNewStudent_whenThereIsNoStudentWithThatNameYetAndCorrectGroupId() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        int groupId = 1;
        when(validatorMock.validateStudentFullName(studentFirstName, studentLastName)).thenReturn(false);
        when(validatorMock.validateGroupId(groupId)).thenReturn(true);

        boolean newStudentAdded = serviceFacade.addNewStudent(studentFirstName, studentLastName, groupId);

        assertTrue(newStudentAdded);
        verify(studentServiceMock, times(1)).addStudent(studentFirstName, studentLastName, groupId);
    }

    @Test
    void addNewStudent_shouldNotAddedNewStudent_whenStudentWithThisNameAlreadyExist() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        int groupId = 1;
        when(validatorMock.validateStudentFullName(studentFirstName, studentLastName)).thenReturn(true);
        when(validatorMock.validateGroupId(groupId)).thenReturn(true);

        boolean newStudentAdded = serviceFacade.addNewStudent(studentFirstName, studentLastName, groupId);

        assertFalse(newStudentAdded);
    }

    @Test
    void addNewStudent_shouldNotAddedNewStudent_whenGivenGroupIdIsNotCorrect() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        int groupId = 1;
        when(validatorMock.validateStudentFullName(studentFirstName, studentLastName)).thenReturn(false);
        when(validatorMock.validateGroupId(groupId)).thenReturn(false);

        boolean newStudentAdded = serviceFacade.addNewStudent(studentFirstName, studentLastName, groupId);

        assertFalse(newStudentAdded);
    }

    @Test
    void deleteStudentById_shouldDeletedStudent_whenGivenStudentIdCorrect() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        int studentId = 1;
        when(validatorMock.validateStudentId(studentId)).thenReturn(true);

        boolean studentDeleted = serviceFacade.deleteStudentById(studentId);

        assertTrue(studentDeleted);
        verify(studentServiceMock, times(1)).deleteStudent(studentId);
    }

    @Test
    void deleteStudentById_shouldNotDeletedStudent_whenGivenStudentIdIsNotCorrect() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        int studentId = 1;
        when(validatorMock.validateStudentId(studentId)).thenReturn(false);

        boolean studentDeleted = serviceFacade.deleteStudentById(studentId);

        assertFalse(studentDeleted);
    }

    @Test
    void addStudentToCourse_shouldAddedStudentToCourse_whenStudentWithGivenNameExistAndCourseWithGivenNameExistAndStudentNotAlreadyRegisteredOnCourse() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String scourseName = "CourseName";
        when(validatorMock.validateStudentFullName(studentFirstName, studentLastName)).thenReturn(true);
        when(validatorMock.validateCourseName(scourseName)).thenReturn(true);
        when(validatorMock.isStudentOnCourse(studentFirstName, studentLastName, scourseName)).thenReturn(false);

        boolean studentAddedToCourse = serviceFacade.addStudentToCourse(studentFirstName, studentLastName, scourseName);

        assertTrue(studentAddedToCourse);
        verify(studentServiceMock, times(1)).addStudentToCourse(studentFirstName, studentLastName, scourseName);
    }

    @Test
    void addStudentToCourse_shouldNotAddedStudentToCourse_whenNoStudentWithGivenName() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String scourseName = "CourseName";
        when(validatorMock.validateStudentFullName(studentFirstName, studentLastName)).thenReturn(false);
        when(validatorMock.validateCourseName(scourseName)).thenReturn(true);
        when(validatorMock.isStudentOnCourse(studentFirstName, studentLastName, scourseName)).thenReturn(false);

        boolean studentAddedToCourse = serviceFacade.addStudentToCourse(studentFirstName, studentLastName, scourseName);

        assertFalse(studentAddedToCourse);
    }

    @Test
    void addStudentToCourse_shouldNotAddedStudentToCourse_whenNoCourseWithGivenName() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String scourseName = "CourseName";
        when(validatorMock.validateStudentFullName(studentFirstName, studentLastName)).thenReturn(true);
        when(validatorMock.validateCourseName(scourseName)).thenReturn(false);
        when(validatorMock.isStudentOnCourse(studentFirstName, studentLastName, scourseName)).thenReturn(false);

        boolean studentAddedToCourse = serviceFacade.addStudentToCourse(studentFirstName, studentLastName, scourseName);

        assertFalse(studentAddedToCourse);
    }

    @Test
    void addStudentToCourse_shouldNotAddedStudentToCourse_whenStudentAlreadyRegisteredOnCourse() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String scourseName = "CourseName";
        when(validatorMock.validateStudentFullName(studentFirstName, studentLastName)).thenReturn(true);
        when(validatorMock.validateCourseName(scourseName)).thenReturn(true);
        when(validatorMock.isStudentOnCourse(studentFirstName, studentLastName, scourseName)).thenReturn(true);

        boolean studentAddedToCourse = serviceFacade.addStudentToCourse(studentFirstName, studentLastName, scourseName);

        assertFalse(studentAddedToCourse);
    }

    @Test
    void deleteStudentFromCourse_shouldDeletedStudentFromCourse_whenStudentWithGivenNameExistAndCourseWithGivenNameExistAndStudentAlreadyRegisteredOnCourse() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String scourseName = "CourseName";
        when(validatorMock.validateStudentFullName(studentFirstName, studentLastName)).thenReturn(true);
        when(validatorMock.validateCourseName(scourseName)).thenReturn(true);
        when(validatorMock.isStudentOnCourse(studentFirstName, studentLastName, scourseName)).thenReturn(true);

        boolean studentDeletedFromCourse = serviceFacade.deleteStudentFromCourse(studentFirstName, studentLastName,
                scourseName);

        assertTrue(studentDeletedFromCourse);
        verify(studentServiceMock, times(1)).deleteStudentFromCourse(studentFirstName, studentLastName, scourseName);
    }

    @Test
    void deleteStudentFromCourse_shouldNotDeletedStudentFromCourse_whenNoStudentWithGivenName() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String scourseName = "CourseName";
        when(validatorMock.validateStudentFullName(studentFirstName, studentLastName)).thenReturn(false);
        when(validatorMock.validateCourseName(scourseName)).thenReturn(true);
        when(validatorMock.isStudentOnCourse(studentFirstName, studentLastName, scourseName)).thenReturn(false);

        boolean studentDeletedFromCourse = serviceFacade.deleteStudentFromCourse(studentFirstName, studentLastName,
                scourseName);

        assertFalse(studentDeletedFromCourse);
    }

    @Test
    void deleteStudentFromCourse_shouldNotDeletedStudentFromCourse_whenNoCourseWithGivenName() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String scourseName = "CourseName";
        when(validatorMock.validateStudentFullName(studentFirstName, studentLastName)).thenReturn(true);
        when(validatorMock.validateCourseName(scourseName)).thenReturn(false);
        when(validatorMock.isStudentOnCourse(studentFirstName, studentLastName, scourseName)).thenReturn(false);

        boolean studentDeletedFromCourse = serviceFacade.deleteStudentFromCourse(studentFirstName, studentLastName,
                scourseName);

        assertFalse(studentDeletedFromCourse);
    }

    @Test
    void deleteStudentFromCourse_shouldNotDeletedStudentFromCourse_whenStudentNotAlreadyRegisteredOnCourse() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String scourseName = "CourseName";
        when(validatorMock.validateStudentFullName(studentFirstName, studentLastName)).thenReturn(true);
        when(validatorMock.validateCourseName(scourseName)).thenReturn(true);
        when(validatorMock.isStudentOnCourse(studentFirstName, studentLastName, scourseName)).thenReturn(false);

        boolean studentDeletedFromCourse = serviceFacade.deleteStudentFromCourse(studentFirstName, studentLastName,
                scourseName);

        assertFalse(studentDeletedFromCourse);
    }

    @Test
    void getStudentById_shouldReturnedStudent_whenStudentWithGivenIdExist() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        int studentId = 1;
        Student expectedStudent = new Student("FirstName", "LastName", 1);
        expectedStudent.setId(studentId);
        when(validatorMock.validateStudentId(studentId)).thenReturn(true);
        when(studentServiceMock.getStudentById(studentId)).thenReturn(expectedStudent);

        Student actualStudent = serviceFacade.getStudentById(studentId);

        assertEquals(expectedStudent, actualStudent);
        verify(studentServiceMock, times(1)).getStudentById(studentId);
    }

    @Test
    void getStudentById_shouldReturnedNull_whenNoStudentWithGivenId() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        int studentId = 1;
        when(validatorMock.validateStudentId(studentId)).thenReturn(false);

        Student actualStudent = serviceFacade.getStudentById(studentId);

        assertEquals(null, actualStudent);
    }

    @Test
    void getAllStudentsWithTheirCourses_shouldMapWithStudentAndTheirCourses_whenStudentServiceReturnsOneExistentStudent() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        List<Student> studentsWhoExist = new ArrayList<>();
        Student student = new Student("FirstName", "LastName", 1);
        studentsWhoExist.add(student);
        List<Course> coursesForStudent = new ArrayList<>();
        coursesForStudent.add(new Course("CourseName_1", "Description_1"));
        coursesForStudent.add(new Course("CourseName_2", "Description_2"));
        Map<Student, List<Course>> expectedStudentWithTheirCourses = new HashMap<>();
        expectedStudentWithTheirCourses.put(student, coursesForStudent);
        when(studentServiceMock.getAllStudents()).thenReturn(studentsWhoExist);
        when(courseServiceMock.getCoursesForStudent(student)).thenReturn(coursesForStudent);

        Map<Student, List<Course>> actualStudentWithTheirCourses = serviceFacade.getAllStudentsWithTheirCourses();

        assertEquals(expectedStudentWithTheirCourses, actualStudentWithTheirCourses);
    }

    @Test
    void getAllStudentsWithTheirCourses_shouldEmptyMap_whenNoExistentStudents() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        List<Student> studentsWhoExist = new ArrayList<>();
        Map<Student, List<Course>> expectedStudentWithTheirCourses = new HashMap<>();
        when(studentServiceMock.getAllStudents()).thenReturn(studentsWhoExist);

        Map<Student, List<Course>> actualStudentWithTheirCourses = serviceFacade.getAllStudentsWithTheirCourses();

        assertEquals(expectedStudentWithTheirCourses, actualStudentWithTheirCourses);
        verify(courseServiceMock, never()).getCoursesForStudent(any(Student.class));
    }

    @Test
    void getAllStudentsWithTheirCourses_shouldNullPointerException_whenStudentServiceReturnsNull() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        when(studentServiceMock.getAllStudents()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> serviceFacade.getAllStudentsWithTheirCourses());
        verify(studentServiceMock, times(1)).getAllStudents();
    }

    @Test
    void getAllCourses_shouldCoursesList_whenCourseServiceReturnsListOfAllCourses() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        List<Course> expectedCourses = new ArrayList<>();
        expectedCourses.add(new Course("CourseName_1", "Description_1"));
        expectedCourses.add(new Course("CourseName_2", "Description_2"));
        when(courseServiceMock.getAllCourses()).thenReturn(expectedCourses);

        List<Course> actualCourses = serviceFacade.getAllCourses();

        assertEquals(expectedCourses, actualCourses);
    }

}
