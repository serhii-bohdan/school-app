package ua.foxminded.schoolapp.service.impl;

import static org.junit.jupiter.api.Assertions.*;
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
import ua.foxminded.schoolapp.dao.GroupDao;
import ua.foxminded.schoolapp.dao.StudentDao;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

@SpringBootTest
@ContextConfiguration(classes = TestAppConfig.class)
class UserInputValidatorImplTest {

    @Mock
    GroupDao groupDaoMock;

    @Mock
    StudentDao studentDaoMock;

    @Mock
    CourseDao courseDaoMock;

    @InjectMocks
    UserInputValidatorImpl validator;

    @Test
    void UserInputValidatorImpl_shouldNullPointerException_whenGroupDaoIsNull() {
        assertThrows(NullPointerException.class, () -> new UserInputValidatorImpl(null, studentDaoMock, courseDaoMock));
    }

    @Test
    void UserInputValidatorImpl_shouldNullPointerException_whenStudentDaoIsNull() {
        assertThrows(NullPointerException.class, () -> new UserInputValidatorImpl(groupDaoMock, null, courseDaoMock));
    }

    @Test
    void UserInputValidatorImpl_shouldNullPointerException_whenCourseDaoIsNull() {
        assertThrows(NullPointerException.class, () -> new UserInputValidatorImpl(groupDaoMock, studentDaoMock, null));
    }

    @Test
    void validateAmountOfStudents_shouldTrue_whenAmountOfStudentsFallsBetweenTenAndThirtyInclusive() {
        validator = new UserInputValidatorImpl(groupDaoMock, studentDaoMock, courseDaoMock);

        boolean expectedResult = validator.validateAmountOfStudents(15);

        assertTrue(expectedResult);
    }

    @Test
    void validateAmountOfStudents_shouldFalse_whenAmountOfStudentsLessThanTen() {
        validator = new UserInputValidatorImpl(groupDaoMock, studentDaoMock, courseDaoMock);

        boolean expectedResult = validator.validateAmountOfStudents(-9);

        assertFalse(expectedResult);
    }

    @Test
    void validateAmountOfStudents_shouldFalse_whenAmountOfStudentsMoreThanThirty() {
        validator = new UserInputValidatorImpl(groupDaoMock, studentDaoMock, courseDaoMock);

        boolean expectedResult = validator.validateAmountOfStudents(50);

        assertFalse(expectedResult);
    }

    @Test
    void validateCourseName_shouldFalse_whenGivenCourseNameIsNull() {
        validator = new UserInputValidatorImpl(groupDaoMock, studentDaoMock, courseDaoMock);
        List<Course> coursesThatExist = new ArrayList<>();
        coursesThatExist.add(new Course("CourseName_1", "Description_1"));
        coursesThatExist.add(new Course("CourseName_2", "Description_2"));
        coursesThatExist.add(new Course("CourseName_3", "Description_3"));
        when(courseDaoMock.findAll()).thenReturn(coursesThatExist);

        boolean expectedResult = validator.validateCourseName(null);

        assertFalse(expectedResult);
    }

    @Test
    void validateCourseName_shouldTrue_whenCourseWithGivenNameExist() {
        validator = new UserInputValidatorImpl(groupDaoMock, studentDaoMock, courseDaoMock);
        List<Course> coursesThatExist = new ArrayList<>();
        coursesThatExist.add(new Course("CourseName_1", "Description_1"));
        coursesThatExist.add(new Course("CourseName_2", "Description_2"));
        coursesThatExist.add(new Course("CourseName_3", "Description_3"));
        when(courseDaoMock.findAll()).thenReturn(coursesThatExist);

        boolean expectedResult = validator.validateCourseName("CourseName_1");

        assertTrue(expectedResult);
    }

    @Test
    void validateCourseName_shouldFalse_whenNoCourseWithGivenName() {
        validator = new UserInputValidatorImpl(groupDaoMock, studentDaoMock, courseDaoMock);
        List<Course> coursesThatExist = new ArrayList<>();
        coursesThatExist.add(new Course("CourseName_1", "Description_1"));
        coursesThatExist.add(new Course("CourseName_2", "Description_2"));
        coursesThatExist.add(new Course("CourseName_3", "Description_3"));
        when(courseDaoMock.findAll()).thenReturn(coursesThatExist);

        boolean expectedResult = validator.validateCourseName("CourseName_4");

        assertFalse(expectedResult);
    }

    @Test
    void validateStudentFullName_shouldFalse_whenGivenStudentFirstNameIsNull() {
        validator = new UserInputValidatorImpl(groupDaoMock, studentDaoMock, courseDaoMock);
        List<Student> studentsThatExist = new ArrayList<>();
        studentsThatExist.add(new Student("FirstName_1", "LastName_1", 1));
        studentsThatExist.add(new Student("FirstName_2", "LastName_2", 2));
        studentsThatExist.add(new Student("FirstName_3", "LastName_3", 3));
        when(studentDaoMock.findAll()).thenReturn(studentsThatExist);

        boolean expectedResult = validator.validateStudentFullName(null, "LastName_1");

        assertFalse(expectedResult);
    }

    @Test
    void validateStudentFullName_shouldFalse_whenGivenStudentLastNameIsNull() {
        validator = new UserInputValidatorImpl(groupDaoMock, studentDaoMock, courseDaoMock);
        List<Student> studentsThatExist = new ArrayList<>();
        studentsThatExist.add(new Student("FirstName_1", "LastName_1", 1));
        studentsThatExist.add(new Student("FirstName_2", "LastName_2", 2));
        studentsThatExist.add(new Student("FirstName_3", "LastName_3", 3));
        when(studentDaoMock.findAll()).thenReturn(studentsThatExist);

        boolean expectedResult = validator.validateStudentFullName("FirstName_3", null);

        assertFalse(expectedResult);
    }

    @Test
    void validateStudentFullName_shouldTrue_whenStudnetWithGivenFullNameExist() {
        validator = new UserInputValidatorImpl(groupDaoMock, studentDaoMock, courseDaoMock);
        List<Student> studentsThatExist = new ArrayList<>();
        studentsThatExist.add(new Student("FirstName_1", "LastName_1", 1));
        studentsThatExist.add(new Student("FirstName_2", "LastName_2", 2));
        studentsThatExist.add(new Student("FirstName_3", "LastName_3", 3));
        when(studentDaoMock.findAll()).thenReturn(studentsThatExist);

        boolean expectedResult = validator.validateStudentFullName("FirstName_2", "LastName_2");

        assertTrue(expectedResult);
    }

    @Test
    void validateStudentFullName_shouldFalse_whenNoStudnetWithGivenFullName() {
        validator = new UserInputValidatorImpl(groupDaoMock, studentDaoMock, courseDaoMock);
        List<Student> studentsThatExist = new ArrayList<>();
        studentsThatExist.add(new Student("FirstName_1", "LastName_1", 1));
        studentsThatExist.add(new Student("FirstName_2", "LastName_2", 2));
        studentsThatExist.add(new Student("FirstName_3", "LastName_3", 3));
        when(studentDaoMock.findAll()).thenReturn(studentsThatExist);

        boolean expectedResult = validator.validateStudentFullName("FirstName_5", "LastName_2");

        assertFalse(expectedResult);
    }

    @Test
    void validateGroupId_shouldTrue_whenGrouWithGivenGroupIdExist() {
        validator = new UserInputValidatorImpl(groupDaoMock, studentDaoMock, courseDaoMock);
        List<Group> groupsThatExist = new ArrayList<>();
        Group firstGroup = new Group("FH-62");
        Group secondGroup = new Group("JK-04");
        Group thirdGroup = new Group("LB-15");
        firstGroup.setId(1);
        secondGroup.setId(2);
        thirdGroup.setId(3);
        groupsThatExist.add(firstGroup);
        groupsThatExist.add(secondGroup);
        groupsThatExist.add(thirdGroup);
        when(groupDaoMock.findAll()).thenReturn(groupsThatExist);

        boolean expectedResult = validator.validateGroupId(3);

        assertTrue(expectedResult);
    }

    @Test
    void validateGroupId_shouldFalse_whenNoGrouWithGivenGroupId() {
        validator = new UserInputValidatorImpl(groupDaoMock, studentDaoMock, courseDaoMock);
        List<Group> groupsThatExist = new ArrayList<>();
        Group firstGroup = new Group("FH-62");
        Group secondGroup = new Group("JK-04");
        Group thirdGroup = new Group("LB-15");
        firstGroup.setId(1);
        secondGroup.setId(2);
        thirdGroup.setId(3);
        groupsThatExist.add(firstGroup);
        groupsThatExist.add(secondGroup);
        groupsThatExist.add(thirdGroup);
        when(groupDaoMock.findAll()).thenReturn(groupsThatExist);

        boolean expectedResult = validator.validateGroupId(-8);

        assertFalse(expectedResult);
    }

    @Test
    void validateStudntId_shouldTrue_whenStudnetWithGivenIdExist() {
        validator = new UserInputValidatorImpl(groupDaoMock, studentDaoMock, courseDaoMock);
        List<Student> studentsThatExist = new ArrayList<>();
        Student firstStudent = new Student("FirstName_1", "LastName_1", 1);
        Student secondStudent = new Student("FirstName_2", "LastName_2", 2);
        Student thirdStudent = new Student("FirstName_3", "LastName_3", 3);
        firstStudent.setId(1);
        secondStudent.setId(2);
        thirdStudent.setId(3);
        studentsThatExist.add(firstStudent);
        studentsThatExist.add(secondStudent);
        studentsThatExist.add(thirdStudent);
        when(studentDaoMock.findAll()).thenReturn(studentsThatExist);

        boolean expectedResult = validator.validateStudentId(1);

        assertTrue(expectedResult);
    }

    @Test
    void validateStudntId_shouldFalse_whenNoStudnetWithGivenId() {
        validator = new UserInputValidatorImpl(groupDaoMock, studentDaoMock, courseDaoMock);
        List<Student> studentsThatExist = new ArrayList<>();
        Student firstStudent = new Student("FirstName_1", "LastName_1", 1);
        Student secondStudent = new Student("FirstName_2", "LastName_2", 2);
        Student thirdStudent = new Student("FirstName_3", "LastName_3", 3);
        firstStudent.setId(1);
        secondStudent.setId(2);
        thirdStudent.setId(3);
        studentsThatExist.add(firstStudent);
        studentsThatExist.add(secondStudent);
        studentsThatExist.add(thirdStudent);
        when(studentDaoMock.findAll()).thenReturn(studentsThatExist);

        boolean expectedResult = validator.validateStudentId(100);

        assertFalse(expectedResult);
    }

    @Test
    void isStudentOnCourse_shouldTrue_whenStudentDaoReturnsTrue() {
        validator = new UserInputValidatorImpl(groupDaoMock, studentDaoMock, courseDaoMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        when(studentDaoMock.isStudentOnCourse(studentFirstName, studentLastName, courseName)).thenReturn(true);

        boolean expectedResult = validator.isStudentOnCourse(studentFirstName, studentLastName, courseName);

        assertTrue(expectedResult);
    }

    @Test
    void isStudentOnCourse_shouldFalse_whenStudentDaoReturnsFalse() {
        validator = new UserInputValidatorImpl(groupDaoMock, studentDaoMock, courseDaoMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        when(studentDaoMock.isStudentOnCourse(studentFirstName, studentLastName, courseName)).thenReturn(false);

        boolean expectedResult = validator.isStudentOnCourse(studentFirstName, studentLastName, courseName);

        assertFalse(expectedResult);
    }

}
