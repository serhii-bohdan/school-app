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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.schoolapp.dto.StudentDto;
import ua.foxminded.schoolapp.dto.mapper.StudentMapper;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.repository.StudentRepository;
import ua.foxminded.schoolapp.service.generate.Generatable;

@SpringBootTest(classes = { StudentServiceImpl.class })
class StudentServiceImplTest {

    @MockBean
    private Generatable<StudentDto> studentsGeneratorMock;

    @MockBean
    private StudentRepository studentRepositoryMock;

    @Autowired
    private StudentServiceImpl studentService;

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
        verify(studentRepositoryMock, never()).save(any(Student.class));
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
        verify(studentRepositoryMock, never()).save(any(Student.class));
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
        verify(studentRepositoryMock, times(generatedStudents.size())).save(any(Student.class));
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
        verify(studentRepositoryMock, times(generatedStudents.size())).save(any(Student.class));
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
        verify(studentRepositoryMock, times(generatedStudents.size())).save(any(Student.class));
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
    void addStudent_shouldIllegalArgumentException_whenStudentDtoIsNull() {
        StudentDto studentDto = null;

        assertThrows(IllegalArgumentException.class, () -> studentService.addStudent(studentDto));
    }

    @Test
    void addStudent_shouldAddedNewStudentAndReturnedStudentOptional_whenStudentRepositorySuccessfulSaveNewStudent() {
        StudentDto studentDto = new StudentDto("FirstName", "LastName", new Group());
        Student expectedNewStudent = StudentMapper.mapDtoToStudent(studentDto);
        when(studentRepositoryMock.save(expectedNewStudent)).thenReturn(expectedNewStudent);

        Optional<Student> actualStudent = studentService.addStudent(studentDto);

        verify(studentRepositoryMock, times(1)).save(expectedNewStudent);
        assertTrue(actualStudent.isPresent());
        assertEquals(expectedNewStudent, actualStudent.get());
    }

    @Test
    void getStudentById_shouldReturnedStudentOptioanal_whenStudentRepositoryFoundStudentWithGivenId() {
        Integer studentId = 1;
        Student expectedStudent = new Student("FirstName", "LastName", new Group());
        expectedStudent.setId(studentId);
        when(studentRepositoryMock.findById(studentId)).thenReturn(Optional.of(expectedStudent));

        Optional<Student> actualStudent = studentService.getStudentById(studentId);

        verify(studentRepositoryMock, times(1)).findById(studentId);
        assertTrue(actualStudent.isPresent());
        assertEquals(expectedStudent, actualStudent.get());
    }

    @Test
    void getStudentById_shouldReturnedEmptyOptioanal_whenStudentRepositoryNotFoundStudentWithGivenId() {
        Integer studentId = -1;
        when(studentRepositoryMock.findById(studentId)).thenReturn(Optional.empty());

        Optional<Student> actualStudent = studentService.getStudentById(studentId);

        verify(studentRepositoryMock, times(1)).findById(studentId);
        assertTrue(actualStudent.isEmpty());
    }

    @Test
    void getStudentByFullName_shouldReturnedStudentOptioanal_whenStudentRepositoryFoundStudentWithGivenFirstNameAndLastName() {
        String firstName = "FirstName";
        String lastName = "LastName";
        Student expectedStudent = new Student(firstName, lastName, new Group());
        when(studentRepositoryMock.findByFirstNameAndLastName(firstName, lastName)).thenReturn(Optional.of(expectedStudent));

        Optional<Student> actualStudent = studentService.getStudentByFullName(firstName, lastName);

        verify(studentRepositoryMock, times(1)).findByFirstNameAndLastName(firstName, lastName);
        assertTrue(actualStudent.isPresent());
        assertEquals(expectedStudent, actualStudent.get());
    }

    @Test
    void getStudentByFullName_shouldEmptyOptioanal_whenStudentRepositoryNotFoundStudentWithGivenFirstNameAndLastName() {
        String firstName = "NotExistent";
        String lastName = "NotExistent";
        when(studentRepositoryMock.findByFirstNameAndLastName(firstName, lastName)).thenReturn(Optional.empty());

        Optional<Student> actualStudent = studentService.getStudentByFullName(firstName, lastName);

        verify(studentRepositoryMock, times(1)).findByFirstNameAndLastName(firstName, lastName);
        assertTrue(actualStudent.isEmpty());
    }

    @Test
    void getStudentByFullName_shouldEmptyOptioanal_whenStudentFirstNameAndLastNameAreNulls() {
        String firstName = null;
        String lastName = null;
        when(studentRepositoryMock.findByFirstNameAndLastName(firstName, lastName)).thenReturn(Optional.empty());

        Optional<Student> actualStudent = studentService.getStudentByFullName(firstName, lastName);

        verify(studentRepositoryMock, times(1)).findByFirstNameAndLastName(firstName, lastName);
        assertTrue(actualStudent.isEmpty());
    }

    @Test
    void getAllStudents_shouldStudentsList_whenStudentRepositoryFoundAllStudents() {
        List<Student> expectedAllStudents = new ArrayList<>();
        expectedAllStudents.add(new Student());
        expectedAllStudents.add(new Student());
        expectedAllStudents.add(new Student());
        when(studentRepositoryMock.findAll()).thenReturn(expectedAllStudents);

        List<Student> actualAllStudents = studentService.getAllStudents();

        verify(studentRepositoryMock, times(1)).findAll();
        assertEquals(expectedAllStudents, actualAllStudents);
    }

    @Test
    void getAllStudents_shouldEmptyStudentsList_whenStudentRepositoryNotFoundAnyStudent() {
        List<Student> expectedAllStudents = new ArrayList<>();
        when(studentRepositoryMock.findAll()).thenReturn(expectedAllStudents);

        List<Student> actualAllStudents = studentService.getAllStudents();

        verify(studentRepositoryMock, times(1)).findAll();
        assertTrue(actualAllStudents.isEmpty());
        assertEquals(expectedAllStudents, actualAllStudents);
    }

    @Test
    void updateStudent_shouldReturnedUpdatedStudentOptional_whenStudentRepositorySuccessfullyUpdateStudent() {
        Student updatedStudent = new Student("NewFirstName", "NewLastName", new Group());
        when(studentRepositoryMock.save(updatedStudent)).thenReturn(updatedStudent);

        Optional<Student> actualStudent = studentService.updateStudent(updatedStudent);

        verify(studentRepositoryMock, times(1)).save(updatedStudent);
        assertTrue(actualStudent.isPresent());
        assertEquals(updatedStudent, actualStudent.get());
    }

    @Test
    void deleteStudentById_shouldDeletedStudent_whenStudentWithGivenIdExist() {
        Integer studentId = 1;
        Student studentToDelete = new Student("FirstName", "LastName", new Group());
        studentToDelete.setId(studentId);
        when(studentService.getStudentById(studentId)).thenReturn(Optional.of(studentToDelete));

        studentService.deleteStudentById(studentId);

        verify(studentRepositoryMock, times(1)).delete(studentToDelete);
    }

    @Test
    void deleteStudentById_shouldNothingDeleted_whenNoStudentWithGivenId() {
        Integer studentId = 5;
        when(studentService.getStudentById(studentId)).thenReturn(Optional.empty());

        studentService.deleteStudentById(studentId);

        verify(studentRepositoryMock, never()).delete(any(Student.class));
    }

}
