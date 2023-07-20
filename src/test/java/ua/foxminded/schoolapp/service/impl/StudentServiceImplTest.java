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
import ua.foxminded.schoolapp.dao.StudentDao;
import ua.foxminded.schoolapp.datasetup.Generatable;
import ua.foxminded.schoolapp.exception.DaoException;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.StudentService;

class StudentServiceImplTest {

    Generatable<Student> studentsGeneratorMock;
    StudentDao studentDaoMock;
    StudentService studentService;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        studentsGeneratorMock = mock(Generatable.class);
        studentDaoMock = mock(StudentDao.class);
    }

    @Test
    void StudentServiceImpl_shouldNullPointerException_whenStudentsGeneratorIsNull() {
        assertThrows(NullPointerException.class, () -> new StudentServiceImpl(null, studentDaoMock));
    }

    @Test
    void StudentServiceImpl_shouldNullPointerException_whenStudentDaoIsNull() {
        assertThrows(NullPointerException.class, () -> new StudentServiceImpl(studentsGeneratorMock, null));
    }

    @Test
    void initStudents_shouldDaoException_whenStudentDaoThrowDaoException() {
        studentService = new StudentServiceImpl(studentsGeneratorMock, studentDaoMock);
        List<Student> generatedStudents = new ArrayList<>();
        Student student = new Student("FirstName", "LastName", 1);
        generatedStudents.add(student);
        when(studentsGeneratorMock.toGenerate()).thenReturn(generatedStudents);
        when(studentDaoMock.save(student)).thenThrow(DaoException.class);

        assertThrows(DaoException.class, () -> studentService.initStudents());
        verify(studentsGeneratorMock, times(1)).toGenerate();
        verify(studentDaoMock, times(1)).save(student);
    }

    @Test
    void initStudents_shouldSavedAllStudentsWhichReturnsStudentsGenerator_whenStudentsGeneratorReturnsSeveralStudents() {
        studentService = new StudentServiceImpl(studentsGeneratorMock, studentDaoMock);
        List<Student> generatedStudents = new ArrayList<>();
        Student firstStudent = new Student("FirstName_1", "LastName_1", 1);
        Student secondStudent = new Student("FirstName_2", "LastName_2", 1);
        Student thirdStudent = new Student("FirstName_3", "LastName_3", 1);
        generatedStudents.add(firstStudent);
        generatedStudents.add(secondStudent);
        generatedStudents.add(thirdStudent);
        when(studentsGeneratorMock.toGenerate()).thenReturn(generatedStudents);

        studentService.initStudents();

        verify(studentsGeneratorMock, times(1)).toGenerate();
        verify(studentDaoMock, times(1)).save(firstStudent);
        verify(studentDaoMock, times(1)).save(secondStudent);
        verify(studentDaoMock, times(1)).save(thirdStudent);
    }

    @Test
    void initStudents_shouldNotSaveAnyStudent_whenStudentsGeneratorReturnsEmptyStudentsList() {
        studentService = new StudentServiceImpl(studentsGeneratorMock, studentDaoMock);
        List<Student> generatedStudents = new ArrayList<>();
        when(studentsGeneratorMock.toGenerate()).thenReturn(generatedStudents);

        studentService.initStudents();

        verify(studentsGeneratorMock, times(1)).toGenerate();
        verify(studentDaoMock, never()).save(any(Student.class));
    }

    @Test
    void initStudentsCoursesTable_shouldDaoException_whenStudentDaoThrowDaoException() {
        studentService = new StudentServiceImpl(studentsGeneratorMock, studentDaoMock);
        when(studentDaoMock.findAll()).thenThrow(DaoException.class);

        assertThrows(DaoException.class, () -> studentService.initStudentsCoursesTable());
        verify(studentDaoMock, times(1)).findAll();
    }

    @Test
    void initStudentsCoursesTable_shouldOneStudentAddedToCourses_whenStudentDaoReturnsListWithOneStudent() {
        studentService = new StudentServiceImpl(studentsGeneratorMock, studentDaoMock);
        List<Student> allStudents = new ArrayList<>();
        allStudents.add(new Student());
        when(studentDaoMock.findAll()).thenReturn(allStudents);

        studentService.initStudentsCoursesTable();

        verify(studentDaoMock, times(1)).findAll();
    }

    @Test
    void getStudentsRelatedToCourse_shouldDaoException_whenStudentDaoThrowDaoException() {
        studentService = new StudentServiceImpl(studentsGeneratorMock, studentDaoMock);
        String courseName = "CourseName";
        when(studentDaoMock.findStudentsRelatedToCourse(courseName)).thenThrow(DaoException.class);

        assertThrows(DaoException.class, () -> studentService.getStudentsRelatedToCourse(courseName));
        verify(studentDaoMock, times(1)).findStudentsRelatedToCourse(courseName);
    }

    @Test
    void getStudentsRelatedToCourse_shouldStudentsRelatedToCourse_whenStudentDaoReturnsStudentsList() {
        studentService = new StudentServiceImpl(studentsGeneratorMock, studentDaoMock);
        String courseName = "CourseName";
        List<Student> expectedStudentsRelatedToCourse = new ArrayList<>();
        Student firstStudent = new Student("FirstName_1", "LastName_1", 1);
        Student secondStudent = new Student("FirstName_2", "LastName_2", 1);
        Student thirdStudent = new Student("FirstName_3", "LastName_3", 1);
        expectedStudentsRelatedToCourse.add(firstStudent);
        expectedStudentsRelatedToCourse.add(secondStudent);
        expectedStudentsRelatedToCourse.add(thirdStudent);
        when(studentDaoMock.findStudentsRelatedToCourse(courseName)).thenReturn(expectedStudentsRelatedToCourse);

        List<Student> actualStudentsRelatedToCourse = studentService.getStudentsRelatedToCourse(courseName);

        assertEquals(expectedStudentsRelatedToCourse, actualStudentsRelatedToCourse);
        verify(studentDaoMock, times(1)).findStudentsRelatedToCourse(courseName);
    }

    @Test
    void getStudentsRelatedToCourse_shouldEmptyStudentsList_whenStudentDaoReturnsEmptyStudentsList() {
        studentService = new StudentServiceImpl(studentsGeneratorMock, studentDaoMock);
        String courseName = "CourseNameThatNotExist";
        List<Student> expectedStudentsRelatedToCourse = new ArrayList<>();
        when(studentDaoMock.findStudentsRelatedToCourse(courseName)).thenReturn(expectedStudentsRelatedToCourse);

        List<Student> actualStudentsRelatedToCourse = studentService.getStudentsRelatedToCourse(courseName);

        assertEquals(expectedStudentsRelatedToCourse, actualStudentsRelatedToCourse);
        verify(studentDaoMock, times(1)).findStudentsRelatedToCourse(courseName);
    }

    @Test
    void addStudent_shouldDaoException_whenStudentDaoThrowDaoException() {
        studentService = new StudentServiceImpl(studentsGeneratorMock, studentDaoMock);
        String firstName = "FirstName";
        String lastName = "LastName";
        int groupId = 1;
        Student student = new Student(firstName, lastName, groupId);
        when(studentDaoMock.save(student)).thenThrow(DaoException.class);

        assertThrows(DaoException.class, () -> studentService.addStudent(firstName, lastName, groupId));
        verify(studentDaoMock, times(1)).save(student);
    }

    @Test
    void addStudent_shouldOneNewRecord_whenStudentDaoSaveNewStudent() {
        studentService = new StudentServiceImpl(studentsGeneratorMock, studentDaoMock);
        int expectedNumberOfNewRecords = 1;
        String firstName = "FirstName";
        String lastName = "LastName";
        int groupId = 1;
        Student student = new Student(firstName, lastName, groupId);
        when(studentDaoMock.save(student)).thenReturn(expectedNumberOfNewRecords);

        int actualNumberOfNewRecords = studentService.addStudent(firstName, lastName, groupId);

        assertEquals(expectedNumberOfNewRecords, actualNumberOfNewRecords);
        verify(studentDaoMock, times(1)).save(student);
    }

    @Test
    void deleteStudent_shouldDaoException_whenStudentDaoThrowDaoException() {
        studentService = new StudentServiceImpl(studentsGeneratorMock, studentDaoMock);
        int studentId = 1;
        when(studentDaoMock.deleteStudentById(studentId)).thenThrow(DaoException.class);

        assertThrows(DaoException.class, () -> studentService.deleteStudent(studentId));
        verify(studentDaoMock, times(1)).deleteStudentById(studentId);
    }

    @Test
    void deleteStudent_shouldNumberOfDeletedRecords_whenStudentDaoDeletedStudentById() {
        studentService = new StudentServiceImpl(studentsGeneratorMock, studentDaoMock);
        int expectedNumberOfDeletedRecords = 1;
        int studentId = 1;
        when(studentDaoMock.deleteStudentById(studentId)).thenReturn(expectedNumberOfDeletedRecords);

        int actualNumberOfDeletedRecords = studentService.deleteStudent(studentId);

        assertEquals(expectedNumberOfDeletedRecords, actualNumberOfDeletedRecords);
        verify(studentDaoMock, times(1)).deleteStudentById(studentId);
    }

    @Test
    void addStudentToCourse_shouldDaoException_whenStudentDaoThrowDaoException() {
        studentService = new StudentServiceImpl(studentsGeneratorMock, studentDaoMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        when(studentDaoMock.addStudentToCourse(studentFirstName, studentLastName, courseName))
                .thenThrow(DaoException.class);

        assertThrows(DaoException.class,
                () -> studentService.addStudentToCourse(studentFirstName, studentLastName, courseName));
        verify(studentDaoMock, times(1)).addStudentToCourse(studentFirstName, studentLastName, courseName);
    }

    @Test
    void addStudentToCourse_shouldNumberOfNewRecordsInStudentsCoursesTable_whenStudentDaoAddStudentToCourse() {
        studentService = new StudentServiceImpl(studentsGeneratorMock, studentDaoMock);
        int expectedNumberOfNewRecords = 1;
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        when(studentDaoMock.addStudentToCourse(studentFirstName, studentLastName, courseName))
                .thenReturn(expectedNumberOfNewRecords);

        int actualNumberOfNewRecords = studentService.addStudentToCourse(studentFirstName, studentLastName, courseName);

        assertEquals(expectedNumberOfNewRecords, actualNumberOfNewRecords);
        verify(studentDaoMock, times(1)).addStudentToCourse(studentFirstName, studentLastName, courseName);
    }

    @Test
    void deleteStudentFromCourse_shouldDaoException_whenStudentDaoThrowDaoException() {
        studentService = new StudentServiceImpl(studentsGeneratorMock, studentDaoMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        when(studentDaoMock.deleteStudentFromCourse(studentFirstName, studentLastName, courseName))
                .thenThrow(DaoException.class);

        assertThrows(DaoException.class,
                () -> studentService.deleteStudentFromCourse(studentFirstName, studentLastName, courseName));
        verify(studentDaoMock, times(1)).deleteStudentFromCourse(studentFirstName, studentLastName, courseName);
    }

    @Test
    void deleteStudentFromCourse_shouldNumberOfDeletedRecordsFromStudentsCoursesTable_whenStudentDaoDeleteStudentFromCourse() {
        studentService = new StudentServiceImpl(studentsGeneratorMock, studentDaoMock);
        int expectedNumberOfDeletedRecords = 1;
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        when(studentDaoMock.deleteStudentFromCourse(studentFirstName, studentLastName, courseName))
                .thenReturn(expectedNumberOfDeletedRecords);

        int actualNumberOfDeletedRecords = studentService.deleteStudentFromCourse(studentFirstName, studentLastName,
                courseName);

        assertEquals(expectedNumberOfDeletedRecords, actualNumberOfDeletedRecords);
        verify(studentDaoMock, times(1)).deleteStudentFromCourse(studentFirstName, studentLastName, courseName);
    }

    @Test
    void getStudentById_shouldDaoException_whenStudentDaoThrowDaoException() {
        studentService = new StudentServiceImpl(studentsGeneratorMock, studentDaoMock);
        int studentId = 1;
        when(studentDaoMock.findStudentById(studentId)).thenThrow(DaoException.class);

        assertThrows(DaoException.class, () -> studentService.getStudentById(studentId));
        verify(studentDaoMock, times(1)).findStudentById(studentId);
    }

    @Test
    void getStudentById_shouldStudentWithEnteredId_whenStudentDaoReturnStudentById() {
        studentService = new StudentServiceImpl(studentsGeneratorMock, studentDaoMock);
        int studentId = 1;
        Student expectedStudent = new Student("FirstName", "LastName", 1);
        expectedStudent.setId(studentId);
        when(studentDaoMock.findStudentById(studentId)).thenReturn(expectedStudent);

        Student actualStudent = studentService.getStudentById(studentId);

        assertEquals(expectedStudent, actualStudent);
        verify(studentDaoMock, times(1)).findStudentById(studentId);
    }

    @Test
    void getAllStudents_shouldDaoException_whenStudentDaoThrowDaoException() {
        studentService = new StudentServiceImpl(studentsGeneratorMock, studentDaoMock);
        when(studentDaoMock.findAll()).thenThrow(DaoException.class);

        assertThrows(DaoException.class, () -> studentService.getAllStudents());
        verify(studentDaoMock, times(1)).findAll();
    }

    @Test
    void getAllStudents_shouldListWithAllStudents_whenStudentDaoReturnsThisList() {
        studentService = new StudentServiceImpl(studentsGeneratorMock, studentDaoMock);
        List<Student> expectedAllStudents = new ArrayList<>();
        Student firstStudent = new Student("FirstName_1", "LastName_1", 1);
        Student secondStudent = new Student("FirstName_2", "LastName_2", 1);
        Student thirdStudent = new Student("FirstName_3", "LastName_3", 1);
        expectedAllStudents.add(firstStudent);
        expectedAllStudents.add(secondStudent);
        expectedAllStudents.add(thirdStudent);
        when(studentDaoMock.findAll()).thenReturn(expectedAllStudents);

        List<Student> actualAllStudents = studentService.getAllStudents();

        assertEquals(expectedAllStudents, actualAllStudents);
        verify(studentDaoMock, times(1)).findAll();
    }

}
