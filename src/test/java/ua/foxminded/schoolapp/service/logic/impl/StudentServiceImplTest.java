package ua.foxminded.schoolapp.service.logic.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.schoolapp.dao.StudentDao;
import ua.foxminded.schoolapp.dto.StudentDto;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.generate.Generatable;

@SpringBootTest(classes = { StudentServiceImpl.class })
class StudentServiceImplTest {

    @MockBean
    Generatable<StudentDto> studentsGeneratorMock;

    @MockBean
    StudentDao studentDaoMock;

    @Autowired
    StudentServiceImpl studentService;

    @Test
    void initStudents_shouldNotSavedAnyStudent_whenStudentsGeneratorReturnsStudentsListAndGroupsListIsEmpty() {
        List<StudentDto> generatedStudents = new ArrayList<>();
        generatedStudents.add(new StudentDto());
        generatedStudents.add(new StudentDto());
        generatedStudents.add(new StudentDto());
        List<Group> groups = new ArrayList<Group>();
        when(studentsGeneratorMock.toGenerate()).thenReturn(generatedStudents);

        studentService.initStudents(groups);

        verify(studentsGeneratorMock, times(1)).toGenerate();
        verify(studentDaoMock, never()).save(any(Student.class));
    }

    @Test
    void initStudents_shouldNotSavedAnyStudent_whenStudentsGeneratorReturnsEmptyStudentsListAndGroupsListContainsSeveralGroups() {
        List<StudentDto> generatedStudents = new ArrayList<>();
        List<Group> groups = new ArrayList<Group>();
        groups.add(new Group());
        groups.add(new Group());
        groups.add(new Group());
        when(studentsGeneratorMock.toGenerate()).thenReturn(generatedStudents);

        studentService.initStudents(groups);

        verify(studentsGeneratorMock, times(1)).toGenerate();
        verify(studentDaoMock, never()).save(any(Student.class));
    }

    @Test
    void initStudents_shouldSavedAllGeneratedStudents_whenStudentsGeneratorReturnSeveralStudentsAndGroupsListContainsSeveralGroups() {
        List<StudentDto> generatedStudents = new ArrayList<>();
        generatedStudents.add(new StudentDto());
        generatedStudents.add(new StudentDto());
        generatedStudents.add(new StudentDto());
        List<Group> groups = new ArrayList<Group>();
        groups.add(new Group());
        groups.add(new Group());
        groups.add(new Group());
        when(studentsGeneratorMock.toGenerate()).thenReturn(generatedStudents);

        studentService.initStudents(groups);

        verify(studentsGeneratorMock, times(1)).toGenerate();
        verify(studentDaoMock, times(generatedStudents.size())).save(any(Student.class));
    }

    @Test
    void initStudents_shouldSavedAllGeneratedStudents_whenStudentsGeneratorReturnSeveralStudentsAndGroupsListContainsOnlyOneGroup() {
        List<StudentDto> generatedStudents = new ArrayList<>();
        generatedStudents.add(new StudentDto());
        generatedStudents.add(new StudentDto());
        generatedStudents.add(new StudentDto());
        List<Group> groups = new ArrayList<Group>();
        groups.add(new Group());
        when(studentsGeneratorMock.toGenerate()).thenReturn(generatedStudents);

        studentService.initStudents(groups);

        verify(studentsGeneratorMock, times(1)).toGenerate();
        verify(studentDaoMock, times(generatedStudents.size())).save(any(Student.class));
    }

    @Test
    void initStudents_shouldSavedAllGeneratedStudents_whenStudentsGeneratorReturnOneStudentAndGroupsListContainsSeveralGroups() {
        List<StudentDto> generatedStudents = new ArrayList<>();
        generatedStudents.add(new StudentDto());
        List<Group> groups = new ArrayList<Group>();
        groups.add(new Group());
        groups.add(new Group());
        groups.add(new Group());
        when(studentsGeneratorMock.toGenerate()).thenReturn(generatedStudents);

        studentService.initStudents(groups);

        verify(studentsGeneratorMock, times(1)).toGenerate();
        verify(studentDaoMock, times(generatedStudents.size())).save(any(Student.class));
    }

    @Test
    void initStudents_shouldNullPointerException_whenStudentsGeneratorReturnSeveralStudentsAndGroupsListIsNull() {
        List<StudentDto> generatedStudents = new ArrayList<>();
        generatedStudents.add(new StudentDto());
        List<Group> groups = null;
        when(studentsGeneratorMock.toGenerate()).thenReturn(generatedStudents);

        assertThrows(NullPointerException.class, () -> studentService.initStudents(groups));
        verify(studentsGeneratorMock, times(1)).toGenerate();
    }

    @Test
    void addStudent_shouldAddedNewStudentAndReturnedStudentOptional_whenStudentDaoSuccessfulSaveNewStudent() {
        String firstName = "FirstName";
        String lastName = "LastName";
        Group group = new Group();
        Student expectedNewStudent = new Student(firstName, lastName, group);
        when(studentDaoMock.save(expectedNewStudent)).thenReturn(expectedNewStudent);

        Optional<Student> actualStudent = studentService.addStudent(firstName, lastName, group);

        verify(studentDaoMock, times(1)).save(expectedNewStudent);
        assertTrue(actualStudent.isPresent());
        assertEquals(expectedNewStudent, actualStudent.get());
    }

    @Test
    void addStudent_shouldConstraintViolationException_whenStudentFirstNameIsNullAndStudentDaoThrowConstraintViolationException() {
        String firstName = null;
        String lastName = "LastName";
        Group group = new Group();
        Student expectedNewStudent = new Student(firstName, lastName, group);
        when(studentDaoMock.save(expectedNewStudent)).thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class, () -> studentService.addStudent(firstName, lastName, group));
        verify(studentDaoMock, times(1)).save(expectedNewStudent);
    }

    @Test
    void addStudent_shouldPropertyValueException_whenStudentGroupIsNullAndStudentDaoThrowPropertyValueException() {
        String firstName = "FirstName";
        String lastName = "LastName";
        Group group = null;
        Student expectedNewStudent = new Student(firstName, lastName, group);
        when(studentDaoMock.save(expectedNewStudent)).thenThrow(PropertyValueException.class);

        assertThrows(PropertyValueException.class, () -> studentService.addStudent(firstName, lastName, group));
        verify(studentDaoMock, times(1)).save(expectedNewStudent);
    }

    @Test
    void addStudent_shouldDataException_whenStudentLastNameContainsMoreThanTwentiFiveLettersAndStudentDaoThrowDataException() {
        String firstName = "FirstName";
        String lastName = "LllllaaassssttttNnnnaaaammmmeeeee";
        Group group = new Group();
        Student expectedNewStudent = new Student(firstName, lastName, group);
        when(studentDaoMock.save(expectedNewStudent)).thenThrow(DataException.class);

        assertThrows(DataException.class, () -> studentService.addStudent(firstName, lastName, group));
        verify(studentDaoMock, times(1)).save(expectedNewStudent);
    }

    @Test
    void getStudentById_shouldReturnedStudentOptioanal_whenStudentDaoFoundStudentWithGivenId() {
        Integer studentId = 1;
        Student expectedStudent = new Student("FirstName", "LastName", new Group());
        expectedStudent.setId(studentId);
        when(studentDaoMock.find(studentId)).thenReturn(expectedStudent);

        Optional<Student> actualStudent = studentService.getStudentById(studentId);

        verify(studentDaoMock, times(1)).find(studentId);
        assertTrue(actualStudent.isPresent());
        assertEquals(expectedStudent, actualStudent.get());
    }

    @Test
    void getStudentById_shouldReturnedEmptyOptioanal_whenStudentDaoNotFoundStudentWithGivenId() {
        Integer studentId = -1;
        when(studentDaoMock.find(studentId)).thenReturn(null);

        Optional<Student> actualStudent = studentService.getStudentById(studentId);

        verify(studentDaoMock, times(1)).find(studentId);
        assertTrue(actualStudent.isEmpty());
    }

    @Test
    void getStudentById_shouldIllegalArgumentException_whenStudentIdIsNull() {
        Integer studentId = null;
        when(studentDaoMock.find(studentId)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> studentService.getStudentById(studentId));
        verify(studentDaoMock, times(1)).find(studentId);
    }

    @Test
    void getStudentByFullName_shouldReturnedStudentOptioanal_whenStudentDaoFoundStudentWithFirstNameAndLastName() {
        String firstName = "FirstName";
        String lastName = "LastName";
        Student expectedStudent = new Student(firstName, lastName, new Group());
        when(studentDaoMock.findStudentByFullName(firstName, lastName)).thenReturn(Optional.of(expectedStudent));

        Optional<Student> actualStudent = studentService.getStudentByFullName(firstName, lastName);

        verify(studentDaoMock, times(1)).findStudentByFullName(firstName, lastName);
        assertTrue(actualStudent.isPresent());
        assertEquals(expectedStudent, actualStudent.get());
    }

    @Test
    void getStudentByFullName_shouldEmptyOptioanal_whenStudentDaoNotFoundStudentWithFirstNameAndLastName() {
        String firstName = "NotExistent";
        String lastName = "NotExistent";
        when(studentDaoMock.findStudentByFullName(firstName, lastName)).thenReturn(Optional.empty());

        Optional<Student> actualStudent = studentService.getStudentByFullName(firstName, lastName);

        verify(studentDaoMock, times(1)).findStudentByFullName(firstName, lastName);
        assertTrue(actualStudent.isEmpty());
    }

    @Test
    void getStudentByFullName_shouldEmptyOptioanal_whenStudentFirstNameAndLastNameAreNulls() {
        String firstName = null;
        String lastName = null;
        when(studentDaoMock.findStudentByFullName(firstName, lastName)).thenReturn(Optional.empty());

        Optional<Student> actualStudent = studentService.getStudentByFullName(firstName, lastName);

        verify(studentDaoMock, times(1)).findStudentByFullName(firstName, lastName);
        assertTrue(actualStudent.isEmpty());
    }

    @Test
    void getAllStudents_shouldStudentsList_whenStudentDaoFoundAllStudents() {
        List<Student> expectedAllStudents = new ArrayList<>();
        expectedAllStudents.add(new Student());
        expectedAllStudents.add(new Student());
        expectedAllStudents.add(new Student());
        when(studentDaoMock.findAll()).thenReturn(expectedAllStudents);

        List<Student> actualAllStudents = studentService.getAllStudents();

        verify(studentDaoMock, times(1)).findAll();
        assertEquals(expectedAllStudents, actualAllStudents);
    }

    @Test
    void getAllStudents_shouldEmptyStudentsList_whenStudentDaoNotFoundAnyStudent() {
        List<Student> expectedAllStudents = new ArrayList<>();
        when(studentDaoMock.findAll()).thenReturn(expectedAllStudents);

        List<Student> actualAllStudents = studentService.getAllStudents();

        verify(studentDaoMock, times(1)).findAll();
        assertTrue(actualAllStudents.isEmpty());
        assertEquals(expectedAllStudents, actualAllStudents);
    }

    @Test
    void updateStudent_shouldReturnedUpdatedStudentOptional_whenStudentDaoSuccessfullyUpdateStudent() {
        Student updatedStudent = new Student("NewFirstName", "NewLastName", new Group());
        when(studentDaoMock.update(updatedStudent)).thenReturn(updatedStudent);

        Optional<Student> actualStudent = studentService.updateStudent(updatedStudent);

        verify(studentDaoMock, times(1)).update(updatedStudent);
        assertTrue(actualStudent.isPresent());
        assertEquals(updatedStudent, actualStudent.get());
    }

    @Test
    void updateStudent_shouldIllegalArgumentException_whenStudenIsNullAndStudentDaoThrowIllegalArgumentException() {
        Student updatedStudent = null;
        when(studentDaoMock.update(updatedStudent)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> studentService.updateStudent(updatedStudent));
        verify(studentDaoMock, times(1)).update(updatedStudent);
    }

    @Test
    void deleteStudent_shouldDeletedStudent_whenStudentIdExist() {
        Integer studentId = 1;

        studentService.deleteStudent(studentId);

        verify(studentDaoMock, times(1)).delete(studentId);
    }

}
