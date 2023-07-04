package ua.foxminded.schoolapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolapp.dao.CourseDao;
import ua.foxminded.schoolapp.dao.GroupDao;
import ua.foxminded.schoolapp.dao.StudentDao;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

class SchoolServiceTest {

    Service service;
    StudentDao studentDaoMock;
    GroupDao groupDaoMock;
    CourseDao courseDaoMock;

    @BeforeEach
    void setUp() {
        studentDaoMock = mock(StudentDao.class);
        groupDaoMock = mock(GroupDao.class);
        courseDaoMock = mock(CourseDao.class);
    }

    @Test
    void schoolService_shouldNullPointerException_whenStudentDaoIsNull() {
        assertThrows(NullPointerException.class, () -> new SchoolService(null, groupDaoMock, courseDaoMock));
    }

    @Test
    void schoolService_shouldNullPointerException_whenGroupDaoIsNull() {
        assertThrows(NullPointerException.class, () -> new SchoolService(studentDaoMock, null, courseDaoMock));
    }

    @Test
    void schoolService_shouldNullPointerException_whenCourseDaoIsNull() {
        assertThrows(NullPointerException.class, () -> new SchoolService(studentDaoMock, groupDaoMock, null));
    }

    @Test
    void getGroupsWithGivenNumberStudents_shouldReturnedGroupsList_whenAmountOfStudentsInGroupsRangingFromTenToThirty() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        int amountOfStudentsInGroups = 23;
        List<Group> expectedGroupsWithGivenStudentsNumber = new ArrayList<>();
        expectedGroupsWithGivenStudentsNumber.add(new Group("HD-12"));
        expectedGroupsWithGivenStudentsNumber.add(new Group("KY-64"));
        expectedGroupsWithGivenStudentsNumber.add(new Group("VN-87"));
        when(groupDaoMock.findGroupsWithGivenNumberStudents(amountOfStudentsInGroups))
                .thenReturn(expectedGroupsWithGivenStudentsNumber);

        List<Group> actualGroupsWithGivenStudentsNumber = service
                .getGroupsWithGivenNumberStudents(amountOfStudentsInGroups);

        assertEquals(expectedGroupsWithGivenStudentsNumber, actualGroupsWithGivenStudentsNumber);
    }

    @Test
    void getGroupsWithGivenNumberStudents_shouldReturnedGroupsList_whenAmountOfStudentsInGroupsIsTen() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        int amountOfStudentsInGroups = 10;
        List<Group> expectedGroupsWithGivenStudentsNumber = new ArrayList<>();
        expectedGroupsWithGivenStudentsNumber.add(new Group("HD-12"));
        expectedGroupsWithGivenStudentsNumber.add(new Group("KY-64"));
        expectedGroupsWithGivenStudentsNumber.add(new Group("VN-87"));
        when(groupDaoMock.findGroupsWithGivenNumberStudents(amountOfStudentsInGroups))
                .thenReturn(expectedGroupsWithGivenStudentsNumber);

        List<Group> actualGroupsWithGivenStudentsNumber = service
                .getGroupsWithGivenNumberStudents(amountOfStudentsInGroups);

        assertEquals(expectedGroupsWithGivenStudentsNumber, actualGroupsWithGivenStudentsNumber);
    }

    @Test
    void getGroupsWithGivenNumberStudents_shouldReturnedGroupsList_whenAmountOfStudentsInGroupsIsThirty() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        int amountOfStudentsInGroups = 30;
        List<Group> expectedGroupsWithGivenStudentsNumber = new ArrayList<>();
        expectedGroupsWithGivenStudentsNumber.add(new Group("HD-12"));
        expectedGroupsWithGivenStudentsNumber.add(new Group("KY-64"));
        expectedGroupsWithGivenStudentsNumber.add(new Group("VN-87"));
        when(groupDaoMock.findGroupsWithGivenNumberStudents(amountOfStudentsInGroups))
                .thenReturn(expectedGroupsWithGivenStudentsNumber);

        List<Group> actualGroupsWithGivenStudentsNumber = service
                .getGroupsWithGivenNumberStudents(amountOfStudentsInGroups);

        assertEquals(expectedGroupsWithGivenStudentsNumber, actualGroupsWithGivenStudentsNumber);
    }

    @Test
    void getGroupsWithGivenNumberStudents_shouldReturnedNull_whenAmountOfStudentsInGroupsLessThenTen() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        int amountOfStudentsInGroups = 0;

        List<Group> actualGroupsWithGivenStudentsNumber = service
                .getGroupsWithGivenNumberStudents(amountOfStudentsInGroups);

        assertEquals(null, actualGroupsWithGivenStudentsNumber);
    }

    @Test
    void getGroupsWithGivenNumberStudents_shouldReturnedNull_whenAmountOfStudentsInGroupsMoreThenThirty() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        int amountOfStudentsInGroups = 43;

        List<Group> actualGroupsWithGivenStudentsNumber = service
                .getGroupsWithGivenNumberStudents(amountOfStudentsInGroups);

        assertEquals(null, actualGroupsWithGivenStudentsNumber);
    }

    @Test
    void getStudentsRelatedToCourse_shouldReturnedStudentsList_whenCourseWithGivenNameExists() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        String courseName = "CourseName";
        List<Course> allСoursesThatExist = new ArrayList<>();
        List<Student> expectedStudentsOnCourse = new ArrayList<>();
        allСoursesThatExist.add(new Course(courseName, "Description"));
        expectedStudentsOnCourse.add(new Student("FirstName_1", "LastName_1", 1));
        expectedStudentsOnCourse.add(new Student("FirstName_2", "LastName_2", 1));
        expectedStudentsOnCourse.add(new Student("FirstName_3", "LastName_3", 1));
        when(courseDaoMock.findAllCourses()).thenReturn(allСoursesThatExist);
        when(studentDaoMock.findStudentsRelatedToCourse(courseName)).thenReturn(expectedStudentsOnCourse);

        List<Student> actualStudentsOnCourse = service.getStudentsRelatedToCourse(courseName);

        assertEquals(expectedStudentsOnCourse, actualStudentsOnCourse);
    }

    @Test
    void getStudentsRelatedToCourse_shouldReturnedNull_whenCourseWithGivenNameExists() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        String courseName = "CourseName_1";
        List<Course> allСoursesThatExist = new ArrayList<>();
        allСoursesThatExist.add(new Course("CourseName_2", "Description"));
        when(courseDaoMock.findAllCourses()).thenReturn(allСoursesThatExist);

        List<Student> actualStudentsOnCourse = service.getStudentsRelatedToCourse(courseName);

        assertEquals(null, actualStudentsOnCourse);
    }

    @Test
    void addNewStudent_shouldAddedNewStudent_whenStudentWithSuchDataNotExistInDatabaseAndGroupIdBetweenOneAndTen() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        List<Student> allStudentsThatExist = new ArrayList<>();
        allStudentsThatExist.add(new Student("FirstName_1", "LastName_1", 1));
        when(studentDaoMock.findAllStudents()).thenReturn(allStudentsThatExist);

        boolean newStudentIsAdded = service.addNewStudent("FirstName_2", "LastName_2", 1);

        assertTrue(newStudentIsAdded);
        ;
    }

    @Test
    void addNewStudent_shouldNotAddedNewStudent_whenStudentWithSuchDataExistInDatabase() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        List<Student> allStudentsThatExist = new ArrayList<>();
        allStudentsThatExist.add(new Student("FirstName_1", "LastName_1", 1));
        when(studentDaoMock.findAllStudents()).thenReturn(allStudentsThatExist);

        boolean newStudentIsAdded = service.addNewStudent("FirstName_1", "LastName_1", 1);

        assertFalse(newStudentIsAdded);
    }

    @Test
    void addNewStudent_shouldNotAddedNewStudent_whenGroupIdLessThenOne() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        List<Student> allStudentsThatExist = new ArrayList<>();
        allStudentsThatExist.add(new Student("FirstName_1", "LastName_1", 1));
        when(studentDaoMock.findAllStudents()).thenReturn(allStudentsThatExist);

        boolean newStudentIsAdded = service.addNewStudent("FirstName_2", "LastName_2", 0);

        assertFalse(newStudentIsAdded);
    }

    @Test
    void addNewStudent_shouldNotAddedNewStudent_whenGroupIdMoreThenTen() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        List<Student> allStudentsThatExist = new ArrayList<>();
        allStudentsThatExist.add(new Student("FirstName_1", "LastName_1", 1));
        when(studentDaoMock.findAllStudents()).thenReturn(allStudentsThatExist);

        boolean newStudentIsAdded = service.addNewStudent("FirstName_2", "LastName_2", 29);

        assertFalse(newStudentIsAdded);
    }

    @Test
    void deleteStudentById_shouldDeletedStudent_whenStudentWithGivenIdExists() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        int givenStudentId = 1;
        Student student = new Student("FirstName_1", "LastName_1", 4);
        student.setId(givenStudentId);
        List<Student> allStudentsThatExist = new ArrayList<>();
        allStudentsThatExist.add(student);
        when(studentDaoMock.findAllStudents()).thenReturn(allStudentsThatExist);

        boolean studentIsDeleted = service.deleteStudentById(givenStudentId);

        assertTrue(studentIsDeleted);
    }

    @Test
    void deleteStudentById_shouldNotDeletedStudent_whenStudentWithGivenIdNotExists() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        int givenStudentId = 1;
        Student student = new Student("FirstName_1", "LastName_1", 4);
        student.setId(givenStudentId);
        List<Student> allStudentsThatExist = new ArrayList<>();
        allStudentsThatExist.add(student);
        when(studentDaoMock.findAllStudents()).thenReturn(allStudentsThatExist);

        boolean studentIsDeleted = service.deleteStudentById(2);

        assertFalse(studentIsDeleted);
    }

    @Test
    void addStudentToCourse_shouldAddedStudentToCourse_whenStudentWithGivenNameExistsAndCourseWithGivenNameExistsAndStudentNotOnCourse() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        String studentFirstName = "FirstName_1";
        String studentLastName = "LastName_1";
        String courseName = "CourseName";
        List<Student> allStudentsThatExist = new ArrayList<>();
        List<Course> allCoursesThatExist = new ArrayList<>();
        allStudentsThatExist.add(new Student(studentFirstName, studentLastName, 1));
        allCoursesThatExist.add(new Course(courseName, "Description"));
        when(studentDaoMock.findAllStudents()).thenReturn(allStudentsThatExist);
        when(courseDaoMock.findAllCourses()).thenReturn(allCoursesThatExist);
        when(studentDaoMock.isStudentOnCourse(studentFirstName, studentLastName, courseName)).thenReturn(false);

        boolean studentIsAddedToCourse = service.addStudentToCourse(studentFirstName, studentLastName, courseName);

        assertTrue(studentIsAddedToCourse);
    }

    @Test
    void addStudentToCourse_shouldNotAddedStudentToCourse_whenStudentWithGivenNameNotExists() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        String courseName = "CourseName";
        List<Student> allStudentsThatExist = new ArrayList<>();
        List<Course> allCoursesThatExist = new ArrayList<>();
        allStudentsThatExist.add(new Student("FirstName_1", "LastName_1", 1));
        allCoursesThatExist.add(new Course(courseName, "Description"));
        when(studentDaoMock.findAllStudents()).thenReturn(allStudentsThatExist);
        when(courseDaoMock.findAllCourses()).thenReturn(allCoursesThatExist);
        when(studentDaoMock.isStudentOnCourse("FirstName_2", "LastName_2", courseName)).thenReturn(false);

        boolean studentIsAddedToCourse = service.addStudentToCourse("FirstName_2", "LastName_2", courseName);

        assertFalse(studentIsAddedToCourse);
    }

    @Test
    void addStudentToCourse_shouldNotAddedStudentToCourse_whenCourseWithGivenNameNotExists() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        String studentFirstName = "FirstName_1";
        String studentLastName = "LastName_1";
        List<Student> allStudentsThatExist = new ArrayList<>();
        List<Course> allCoursesThatExist = new ArrayList<>();
        allStudentsThatExist.add(new Student(studentFirstName, studentLastName, 1));
        allCoursesThatExist.add(new Course("CourseName_1", "Description"));
        when(studentDaoMock.findAllStudents()).thenReturn(allStudentsThatExist);
        when(courseDaoMock.findAllCourses()).thenReturn(allCoursesThatExist);
        when(studentDaoMock.isStudentOnCourse(studentFirstName, studentLastName, "CourseName_2")).thenReturn(false);

        boolean studentIsAddedToCourse = service.addStudentToCourse(studentFirstName, studentLastName, "CourseName_2");

        assertFalse(studentIsAddedToCourse);
    }

    @Test
    void addStudentToCourse_shouldNotAddedStudentToCourse_whenStudentAlreadyOnCourse() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        String studentFirstName = "FirstName_1";
        String studentLastName = "LastName_1";
        String courseName = "CourseName";
        List<Student> allStudentsThatExist = new ArrayList<>();
        List<Course> allCoursesThatExist = new ArrayList<>();
        allStudentsThatExist.add(new Student(studentFirstName, studentLastName, 1));
        allCoursesThatExist.add(new Course(courseName, "Description"));
        when(studentDaoMock.findAllStudents()).thenReturn(allStudentsThatExist);
        when(courseDaoMock.findAllCourses()).thenReturn(allCoursesThatExist);
        when(studentDaoMock.isStudentOnCourse(studentFirstName, studentLastName, courseName)).thenReturn(true);

        boolean studentIsAddedToCourse = service.addStudentToCourse(studentFirstName, studentLastName, courseName);

        assertFalse(studentIsAddedToCourse);
    }
    
    @Test
    void addStudentToCourse_shouldDeletedStudentFromCourse_whenStudentWithGivenNameExistsAndCourseWithGivenNameExistsAndStudentAlreadyOnCourse() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        String studentFirstName = "FirstName_1";
        String studentLastName = "LastName_1";
        String courseName = "CourseName";
        List<Student> allStudentsThatExist = new ArrayList<>();
        List<Course> allCoursesThatExist = new ArrayList<>();
        allStudentsThatExist.add(new Student(studentFirstName, studentLastName, 1));
        allCoursesThatExist.add(new Course(courseName, "Description"));
        when(studentDaoMock.findAllStudents()).thenReturn(allStudentsThatExist);
        when(courseDaoMock.findAllCourses()).thenReturn(allCoursesThatExist);
        when(studentDaoMock.isStudentOnCourse(studentFirstName, studentLastName, courseName)).thenReturn(true);

        boolean studentDeletedFromCourse = service.deleteStudentFromCourse(studentFirstName, studentLastName, courseName);

        assertTrue(studentDeletedFromCourse);
    }
    
    @Test
    void addStudentToCourse_shouldNotDeletedStudentFromCourse_whenStudentWithGivenNameNotExists() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        String courseName = "CourseName";
        List<Student> allStudentsThatExist = new ArrayList<>();
        List<Course> allCoursesThatExist = new ArrayList<>();
        allStudentsThatExist.add(new Student("FirstName_1", "LastName_1", 1));
        allCoursesThatExist.add(new Course(courseName, "Description"));
        when(studentDaoMock.findAllStudents()).thenReturn(allStudentsThatExist);
        when(courseDaoMock.findAllCourses()).thenReturn(allCoursesThatExist);
        when(studentDaoMock.isStudentOnCourse("FirstName_2", "LastName_2", courseName)).thenReturn(false);

        boolean studentDeletedFromCourse = service.deleteStudentFromCourse("FirstName_2", "LastName_2", courseName);

        assertFalse(studentDeletedFromCourse);
    }
    
    @Test
    void addStudentToCourse_shouldNotDeletedStudentFromCourse_whenCourseWithGivenNameNotExists() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        String studentFirstName = "FirstName_1";
        String studentLastName = "LastName_1";
        List<Student> allStudentsThatExist = new ArrayList<>();
        List<Course> allCoursesThatExist = new ArrayList<>();
        allStudentsThatExist.add(new Student(studentFirstName, studentLastName, 1));
        allCoursesThatExist.add(new Course("CourseName_1", "Description"));
        when(studentDaoMock.findAllStudents()).thenReturn(allStudentsThatExist);
        when(courseDaoMock.findAllCourses()).thenReturn(allCoursesThatExist);
        when(studentDaoMock.isStudentOnCourse(studentFirstName, studentLastName, "CourseName_2")).thenReturn(false);

        boolean studentDeletedFromCourse = service.deleteStudentFromCourse(studentFirstName, studentLastName, "CourseName_2");

        assertFalse(studentDeletedFromCourse);
    }

    @Test
    void addStudentToCourse_shouldNotDeletedStudentFromCourse_whenStudentNotOnCourse() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        String studentFirstName = "FirstName_1";
        String studentLastName = "LastName_1";
        String courseName = "CourseName";
        List<Student> allStudentsThatExist = new ArrayList<>();
        List<Course> allCoursesThatExist = new ArrayList<>();
        allStudentsThatExist.add(new Student(studentFirstName, studentLastName, 1));
        allCoursesThatExist.add(new Course(courseName, "Description"));
        when(studentDaoMock.findAllStudents()).thenReturn(allStudentsThatExist);
        when(courseDaoMock.findAllCourses()).thenReturn(allCoursesThatExist);
        when(studentDaoMock.isStudentOnCourse(studentFirstName, studentLastName, courseName)).thenReturn(false);

        boolean studentDeletedFromCourse = service.deleteStudentFromCourse(studentFirstName, studentLastName, courseName);

        assertFalse(studentDeletedFromCourse);
    }

    @Test
    void getStudentById_shouldReturnedStudent_whenStudentWithGivenIdExists() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        Student expectedStudent = new Student("FirstName_1", "LastName_1", 1);
        expectedStudent.setId(1);
        List<Student> allStudentsThatExist = new ArrayList<>();
        allStudentsThatExist.add(expectedStudent);
        when(studentDaoMock.findAllStudents()).thenReturn(allStudentsThatExist);
        when(studentDaoMock.findStudentById(1)).thenReturn(expectedStudent);
        
        Student actualStudent = service.getStudentById(1);
        
        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void getStudentById_shouldReturnedNull_whenStudentWithGivenIdNotExists() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        Student student = new Student("FirstName_1", "LastName_1", 1);
        student.setId(1);
        List<Student> allStudentsThatExist = new ArrayList<>();
        allStudentsThatExist.add(student);
        when(studentDaoMock.findAllStudents()).thenReturn(allStudentsThatExist);

        Student actualStudent = service.getStudentById(2);

        assertEquals(null, actualStudent);
    }

    @Test
    void getAllStudents_shouldStudentsList_whenstudentDaoMockReturnStudentList() {
        service = new SchoolService(studentDaoMock, groupDaoMock, courseDaoMock);
        Student student = new Student("FirstName_1", "LastName_1", 1);
        student.setId(1);
        List<Student> expecteAllStudentsThatExist = new ArrayList<>();
        expecteAllStudentsThatExist.add(student);
        when(studentDaoMock.findAllStudents()).thenReturn(expecteAllStudentsThatExist);

        List<Student> actualAllStudentsThatExist = service.getAllStudents();

        assertEquals(expecteAllStudentsThatExist, actualAllStudentsThatExist);
    }

}
