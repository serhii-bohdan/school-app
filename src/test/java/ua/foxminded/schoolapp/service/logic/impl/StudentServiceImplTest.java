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
import ua.foxminded.schoolapp.dao.StudentDao;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.generate.Generatable;

@SpringBootTest(classes = { StudentServiceImpl.class })
class StudentServiceImplTest {

    @MockBean
    Generatable<Student> studentsGeneratorMock;

    @MockBean
    StudentDao studentDaoMock;

    @Autowired
    StudentServiceImpl studentService;

    @Test
    void initStudents_shouldSavedAllStudentsWhichReturnsStudentsGenerator_whenStudentsGeneratorReturnsSeveralStudents() {
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
        List<Student> generatedStudents = new ArrayList<>();
        when(studentsGeneratorMock.toGenerate()).thenReturn(generatedStudents);

        studentService.initStudents();

        verify(studentsGeneratorMock, times(1)).toGenerate();
        verify(studentDaoMock, never()).save(any(Student.class));
    }

    @Test
    void initStudentsCoursesTable_shouldOneStudentAddedToCourses_whenStudentDaoReturnsListWithOneStudent() {
        List<Student> allStudents = new ArrayList<>();
        allStudents.add(new Student());
        when(studentDaoMock.findAll()).thenReturn(allStudents);

        studentService.initStudentsCoursesTable();

        verify(studentDaoMock, times(1)).findAll();
    }

    @Test
    void getStudentsRelatedToCourse_shouldStudentsRelatedToCourse_whenStudentDaoReturnsStudentsList() {
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
        String courseName = "CourseNameThatNotExist";
        List<Student> expectedStudentsRelatedToCourse = new ArrayList<>();
        when(studentDaoMock.findStudentsRelatedToCourse(courseName)).thenReturn(expectedStudentsRelatedToCourse);

        List<Student> actualStudentsRelatedToCourse = studentService.getStudentsRelatedToCourse(courseName);

        assertEquals(expectedStudentsRelatedToCourse, actualStudentsRelatedToCourse);
        verify(studentDaoMock, times(1)).findStudentsRelatedToCourse(courseName);
    }

    @Test
    void addStudent_shouldOneNewRecord_whenStudentDaoSaveNewStudent() {
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
    void deleteStudent_shouldNumberOfDeletedRecords_whenStudentDaoDeletedStudentById() {
        int expectedNumberOfDeletedRecords = 1;
        int studentId = 1;
        when(studentDaoMock.delete(studentId)).thenReturn(expectedNumberOfDeletedRecords);

        int actualNumberOfDeletedRecords = studentService.deleteStudent(studentId);

        assertEquals(expectedNumberOfDeletedRecords, actualNumberOfDeletedRecords);
        verify(studentDaoMock, times(1)).delete(studentId);
    }

    @Test
    void addStudentToCourse_shouldNumberOfNewRecordsInStudentsCoursesTable_whenStudentDaoAddStudentToCourse() {
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
    void deleteStudentFromCourse_shouldNumberOfDeletedRecordsFromStudentsCoursesTable_whenStudentDaoDeleteStudentFromCourse() {
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
    void getStudentById_shouldStudentWithEnteredId_whenStudentDaoReturnStudentById() {
        int studentId = 1;
        Student expectedStudent = new Student("FirstName", "LastName", 1);
        expectedStudent.setId(studentId);
        when(studentDaoMock.find(studentId)).thenReturn(expectedStudent);

        Student actualStudent = studentService.getStudentById(studentId);

        assertEquals(expectedStudent, actualStudent);
        verify(studentDaoMock, times(1)).find(studentId);
    }

    @Test
    void getAllStudents_shouldListWithAllStudents_whenStudentDaoReturnsThisList() {
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
