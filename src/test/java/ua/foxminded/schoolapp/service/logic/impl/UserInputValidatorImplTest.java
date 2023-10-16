package ua.foxminded.schoolapp.service.logic.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.schoolapp.dao.CourseDao;
import ua.foxminded.schoolapp.dao.GroupDao;
import ua.foxminded.schoolapp.dao.StudentDao;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

@SpringBootTest(classes = { UserInputValidatorImpl.class })
class UserInputValidatorImplTest {

    @MockBean
    GroupDao groupDaoMock;

    @MockBean
    StudentDao studentDaoMock;

    @MockBean
    CourseDao courseDaoMock;

    @Autowired
    UserInputValidatorImpl validator;

    @Test
    void validateAmountOfStudents_shouldTrue_whenAmountOfStudentsIsZero() {
        boolean expectedResult = validator.validateAmountOfStudents(0);

        assertTrue(expectedResult);
    }

    @Test
    void validateAmountOfStudents_shouldFalse_whenAmountOfStudentsLessThanZero() {
        boolean expectedResult = validator.validateAmountOfStudents(-9);

        assertFalse(expectedResult);
    }

    @Test
    void validateAmountOfStudents_shouldTrue_whenAmountOfStudentsMoreThanZero() {
        boolean expectedResult = validator.validateAmountOfStudents(5000);

        assertTrue(expectedResult);
    }

    @Test
    void validateAmountOfStudents_shouldFalse_whenAmountOfStudentsMoreThanIntegerMaxValue() {
        boolean expectedResult = validator.validateAmountOfStudents(Integer.MAX_VALUE + 2949494);

        assertFalse(expectedResult);
    }

    @Test
    void validateAmountOfStudents_shouldNullPointerException_whenAmountOfStudentsIsNull() {
        Integer amountOfStudents = null;

        assertThrows(NullPointerException.class, () -> validator.validateAmountOfStudents(amountOfStudents));
    }

    @Test
    void validateGroupId_shouldTrue_whenGrouWithGivenGroupIdExist() {
        List<Group> groupsThatExist = new ArrayList<>();
        Group firstGroup = new Group();
        Group secondGroup = new Group();
        Group thirdGroup = new Group();
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
        List<Group> groupsThatExist = new ArrayList<>();
        Group firstGroup = new Group();
        Group secondGroup = new Group();
        Group thirdGroup = new Group();
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
    void validateGroupId_shouldFalse_whenGroupIdIsNull() {
        Integer groupId = null;
        List<Group> groupsThatExist = new ArrayList<>();
        Group firstGroup = new Group();
        Group secondGroup = new Group();
        Group thirdGroup = new Group();
        firstGroup.setId(1);
        secondGroup.setId(2);
        thirdGroup.setId(3);
        groupsThatExist.add(firstGroup);
        groupsThatExist.add(secondGroup);
        groupsThatExist.add(thirdGroup);
        when(groupDaoMock.findAll()).thenReturn(groupsThatExist);

        boolean expectedResult = validator.validateGroupId(groupId);

        assertFalse(expectedResult);
    }

    @Test
    void validateGroupNameExistence_shouldTrue_whenGrouWithGivenNameExist() {
        List<Group> groupsThatExist = new ArrayList<>();
        Group firstGroup = new Group("WD-73");
        Group secondGroup = new Group("MQ-44");
        Group thirdGroup = new Group("SA-49");
        groupsThatExist.add(firstGroup);
        groupsThatExist.add(secondGroup);
        groupsThatExist.add(thirdGroup);
        when(groupDaoMock.findAll()).thenReturn(groupsThatExist);

        boolean expectedResult = validator.validateGroupNameExistence("MQ-44");

        assertTrue(expectedResult);
    }

    @Test
    void validateGroupNameExistence_shouldFalse_whenNoGrouWithGivenName() {
        List<Group> groupsThatExist = new ArrayList<>();
        Group firstGroup = new Group("WD-73");
        Group secondGroup = new Group("MQ-44");
        Group thirdGroup = new Group("SA-49");
        groupsThatExist.add(firstGroup);
        groupsThatExist.add(secondGroup);
        groupsThatExist.add(thirdGroup);
        when(groupDaoMock.findAll()).thenReturn(groupsThatExist);

        boolean expectedResult = validator.validateGroupNameExistence("BG-00");

        assertFalse(expectedResult);
    }

    @Test
    void validateGroupNameExistence_shouldFalse_whenGrouNameIsNull() {
        List<Group> groupsThatExist = new ArrayList<>();
        Group firstGroup = new Group("WD-73");
        Group secondGroup = new Group("MQ-44");
        Group thirdGroup = new Group("SA-49");
        groupsThatExist.add(firstGroup);
        groupsThatExist.add(secondGroup);
        groupsThatExist.add(thirdGroup);
        when(groupDaoMock.findAll()).thenReturn(groupsThatExist);

        boolean expectedResult = validator.validateGroupNameExistence(null);

        assertFalse(expectedResult);
    }

    @Test
    void validateGroupNamePattern_shouldTrue_whenGroupNameMatchesPattern() {
        String groupName = "FL-43";

        boolean expectedResult = validator.validateGroupNamePattern(groupName);

        assertTrue(expectedResult);
    }

    @Test
    void validateGroupNamePattern_shouldFalse_whenGroupNameNotMatchesPattern() {
        String groupName = "NotMatchesPattern";

        boolean expectedResult = validator.validateGroupNamePattern(groupName);

        assertFalse(expectedResult);
    }

    @Test
    void validateGroupNamePattern_shouldFalse_whenGroupNameIsNull() {
        String groupName = null;

        assertThrows(NullPointerException.class, () -> validator.validateGroupNamePattern(groupName));
    }

    @Test
    void validateCourseName_shouldFalse_whenGivenCourseNameIsNull() {
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
        List<Course> coursesThatExist = new ArrayList<>();
        coursesThatExist.add(new Course("CourseName_1", "Description_1"));
        coursesThatExist.add(new Course("CourseName_2", "Description_2"));
        coursesThatExist.add(new Course("CourseName_3", "Description_3"));
        when(courseDaoMock.findAll()).thenReturn(coursesThatExist);

        boolean expectedResult = validator.validateCourseName("CourseName_4");

        assertFalse(expectedResult);
    }

    @Test
    void validateCourseName_shouldFalse_whenCourseNameIsNull() {
        List<Course> coursesThatExist = new ArrayList<>();
        coursesThatExist.add(new Course("CourseName_1", "Description_1"));
        coursesThatExist.add(new Course("CourseName_2", "Description_2"));
        coursesThatExist.add(new Course("CourseName_3", "Description_3"));
        when(courseDaoMock.findAll()).thenReturn(coursesThatExist);

        boolean expectedResult = validator.validateCourseName(null);

        assertFalse(expectedResult);
    }

    @Test
    void validateDescription_shouldTrue_whenCourseWithGivenDescriptionExist() {
        List<Course> coursesThatExist = new ArrayList<>();
        coursesThatExist.add(new Course("CourseName_1", "Description_1"));
        coursesThatExist.add(new Course("CourseName_2", "Description_2"));
        coursesThatExist.add(new Course("CourseName_3", "Description_3"));
        when(courseDaoMock.findAll()).thenReturn(coursesThatExist);

        boolean expectedResult = validator.validateDescription("Description_1");

        assertTrue(expectedResult);
    }

    @Test
    void validateDescription_shouldFalse_whenNoCourseWithGivenDescription() {
        List<Course> coursesThatExist = new ArrayList<>();
        coursesThatExist.add(new Course("CourseName_1", "Description_1"));
        coursesThatExist.add(new Course("CourseName_2", "Description_2"));
        coursesThatExist.add(new Course("CourseName_3", "Description_3"));
        when(courseDaoMock.findAll()).thenReturn(coursesThatExist);

        boolean expectedResult = validator.validateDescription("NotExistentDescription");

        assertFalse(expectedResult);
    }

    @Test
    void validateDescription_shouldFalse_whenDescriptionIsNull() {
        List<Course> coursesThatExist = new ArrayList<>();
        coursesThatExist.add(new Course("CourseName_1", "Description_1"));
        coursesThatExist.add(new Course("CourseName_2", "Description_2"));
        coursesThatExist.add(new Course("CourseName_3", "Description_3"));
        when(courseDaoMock.findAll()).thenReturn(coursesThatExist);

        boolean expectedResult = validator.validateDescription(null);

        assertFalse(expectedResult);
    }

    @Test
    void validateStudntId_shouldTrue_whenStudnetWithGivenIdExist() {
        List<Student> studentsThatExist = new ArrayList<>();
        Student firstStudent = new Student();
        Student secondStudent = new Student();
        Student thirdStudent = new Student();
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
        List<Student> studentsThatExist = new ArrayList<>();
        Student firstStudent = new Student();
        Student secondStudent = new Student();
        Student thirdStudent = new Student();
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
    void validateStudntId_shouldFalse_whenStudnetIdIsNull() {
        Integer studentId = null;
        List<Student> studentsThatExist = new ArrayList<>();
        Student firstStudent = new Student();
        Student secondStudent = new Student();
        Student thirdStudent = new Student();
        firstStudent.setId(1);
        secondStudent.setId(2);
        thirdStudent.setId(3);
        studentsThatExist.add(firstStudent);
        studentsThatExist.add(secondStudent);
        studentsThatExist.add(thirdStudent);
        when(studentDaoMock.findAll()).thenReturn(studentsThatExist);

        boolean expectedResult = validator.validateStudentId(studentId);

        assertFalse(expectedResult);
    }

    @Test
    void validateStudentFullName_shouldFalse_whenGivenStudentFirstNameIsNull() {
        List<Student> studentsThatExist = new ArrayList<>();
        studentsThatExist.add(new Student("FirstName_1", "LastName_1", new Group()));
        studentsThatExist.add(new Student("FirstName_2", "LastName_2", new Group()));
        studentsThatExist.add(new Student("FirstName_3", "LastName_3", new Group()));
        when(studentDaoMock.findAll()).thenReturn(studentsThatExist);

        boolean expectedResult = validator.validateStudentFullName(null, "LastName_1");

        assertFalse(expectedResult);
    }

    @Test
    void validateStudentFullName_shouldFalse_whenGivenStudentLastNameIsNull() {
        List<Student> studentsThatExist = new ArrayList<>();
        studentsThatExist.add(new Student("FirstName_1", "LastName_1", new Group()));
        studentsThatExist.add(new Student("FirstName_2", "LastName_2", new Group()));
        studentsThatExist.add(new Student("FirstName_3", "LastName_3", new Group()));
        when(studentDaoMock.findAll()).thenReturn(studentsThatExist);

        boolean expectedResult = validator.validateStudentFullName("FirstName_3", null);

        assertFalse(expectedResult);
    }

    @Test
    void validateStudentFullName_shouldTrue_whenStudnetWithGivenFullNameExist() {
        List<Student> studentsThatExist = new ArrayList<>();
        studentsThatExist.add(new Student("FirstName_1", "LastName_1", new Group()));
        studentsThatExist.add(new Student("FirstName_2", "LastName_2", new Group()));
        studentsThatExist.add(new Student("FirstName_3", "LastName_3", new Group()));
        when(studentDaoMock.findAll()).thenReturn(studentsThatExist);

        boolean expectedResult = validator.validateStudentFullName("FirstName_2", "LastName_2");

        assertTrue(expectedResult);
    }

    @Test
    void validateStudentFullName_shouldFalse_whenNoStudnetWithGivenFullName() {
        List<Student> studentsThatExist = new ArrayList<>();
        studentsThatExist.add(new Student("FirstName_1", "LastName_1", new Group()));
        studentsThatExist.add(new Student("FirstName_2", "LastName_2", new Group()));
        studentsThatExist.add(new Student("FirstName_3", "LastName_3", new Group()));
        when(studentDaoMock.findAll()).thenReturn(studentsThatExist);

        boolean expectedResult = validator.validateStudentFullName("FirstName_5", "LastName_2");

        assertFalse(expectedResult);
    }

    @Test
    void isStudentOnCourse_shouldTrue_whenStudentCoursesSetContainsCourse() {
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        Student studentMock = mock(Student.class);
        Course courseMock = mock(Course.class);
        Set<Course> coursesForStudent = new HashSet<>();
        coursesForStudent.add(courseMock);
        when(studentDaoMock.findStudentByFullName(studentFirstName, studentLastName))
                .thenReturn(Optional.of(studentMock));
        when(courseDaoMock.findCourseByName(courseName)).thenReturn(Optional.of(courseMock));
        when(studentMock.getCourses()).thenReturn(coursesForStudent);

        boolean expectedResult = validator.isStudentOnCourse(studentFirstName, studentLastName, courseName);

        assertTrue(expectedResult);
    }

    @Test
    void isStudentOnCourse_shouldFalse_whenStudentCoursesSetNotContainsCourse() {
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        Student studentMock = mock(Student.class);
        Course courseMock = mock(Course.class);
        Set<Course> coursesForStudent = new HashSet<>();
        when(studentDaoMock.findStudentByFullName(studentFirstName, studentLastName))
                .thenReturn(Optional.of(studentMock));
        when(courseDaoMock.findCourseByName(courseName)).thenReturn(Optional.of(courseMock));
        when(studentMock.getCourses()).thenReturn(coursesForStudent);

        boolean expectedResult = validator.isStudentOnCourse(studentFirstName, studentLastName, courseName);

        assertFalse(expectedResult);
    }

    @Test
    void isStudentOnCourse_shouldFalse_whenNoStudentWithGivenFullName() {
        String studentFirstName = "NotExistent";
        String studentLastName = "NotExistent";
        String courseName = "CourseName";
        Course courseMock = mock(Course.class);
        when(studentDaoMock.findStudentByFullName(studentFirstName, studentLastName)).thenReturn(Optional.empty());
        when(courseDaoMock.findCourseByName(courseName)).thenReturn(Optional.of(courseMock));

        boolean expectedResult = validator.isStudentOnCourse(studentFirstName, studentLastName, courseName);

        assertFalse(expectedResult);
    }

    @Test
    void isStudentOnCourse_shouldFalse_whenNoCourseWithGivenName() {
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "NotExistent";
        Student studentMock = mock(Student.class);
        when(studentDaoMock.findStudentByFullName(studentFirstName, studentLastName))
                .thenReturn(Optional.of(studentMock));
        when(courseDaoMock.findCourseByName(courseName)).thenReturn(Optional.empty());

        boolean expectedResult = validator.isStudentOnCourse(studentFirstName, studentLastName, courseName);

        assertFalse(expectedResult);
    }

    @Test
    void validateNameLength_shouldTrue_whenWordLengthLessThanTwentyFive() {
        String name = "FirstName";

        boolean expectedResult = validator.validateNameLength(name);

        assertTrue(expectedResult);
    }

    @Test
    void validateNameLength_shouldTrue_whenWordLengthLargerThanTwentyFive() {
        String name = "FfffiiiirrrrsssstttttNnnnaaaammmeee";

        boolean expectedResult = validator.validateNameLength(name);

        assertFalse(expectedResult);
    }

    @Test
    void validateNameLength_shouldFalse_whenWordLengthIsZero() {
        String name = "";

        boolean expectedResult = validator.validateNameLength(name);

        assertTrue(expectedResult);
    }

    @Test
    void validateNameLength_shouldNullPointerException_whenGivenWordIsNull() {
        String name = null;

        assertThrows(NullPointerException.class, () -> validator.validateNameLength(name));
    }

}
