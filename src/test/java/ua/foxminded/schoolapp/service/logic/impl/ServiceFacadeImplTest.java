package ua.foxminded.schoolapp.service.logic.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyInt;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.schoolapp.dto.CourseDto;
import ua.foxminded.schoolapp.dto.GroupDto;
import ua.foxminded.schoolapp.dto.StudentDto;
import ua.foxminded.schoolapp.dto.mapper.CourseMapper;
import ua.foxminded.schoolapp.dto.mapper.GroupMapper;
import ua.foxminded.schoolapp.dto.mapper.StudentMapper;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.logic.CourseService;
import ua.foxminded.schoolapp.service.logic.GroupService;
import ua.foxminded.schoolapp.service.logic.StudentService;
import ua.foxminded.schoolapp.service.logic.UserInputValidator;

@SpringBootTest(classes = { ServiceFacadeImpl.class })
class ServiceFacadeImplTest {

    @MockBean
    private GroupService groupServiceMock;

    @MockBean
    private StudentService studentServiceMock;

    @MockBean
    private CourseService courseServiceMock;

    @MockBean
    private UserInputValidator validatorMock;

    @Autowired
    private ServiceFacadeImpl serviceFacade;

    @Test
    void initSchema_shouldInitializedGroupsStudentsCoursesTables_whenTheyWereAllEmptyBeforeThat() {
        when(groupServiceMock.getAllGroups()).thenReturn(new ArrayList<Group>());
        when(studentServiceMock.getAllStudents()).thenReturn(new ArrayList<Student>());
        when(courseServiceMock.getAllCourses()).thenReturn(new ArrayList<Course>());

        serviceFacade.initSchema();

        verify(groupServiceMock, times(1)).initGroups();
        verify(studentServiceMock, times(1)).initStudents(new ArrayList<Group>());
        verify(courseServiceMock, times(1)).initCourses();
    }

    @Test
    void initSchema_shouldNotInitializedGroupsStudentsCoursesTables_whenGroupsTableNotEmpty() {
        when(groupServiceMock.getAllGroups()).thenReturn(Collections.singletonList(new Group()));
        when(studentServiceMock.getAllStudents()).thenReturn(new ArrayList<Student>());
        when(courseServiceMock.getAllCourses()).thenReturn(new ArrayList<Course>());

        serviceFacade.initSchema();

        verify(groupServiceMock, never()).initGroups();
        verify(studentServiceMock, never()).initStudents(new ArrayList<Group>());
        verify(courseServiceMock, never()).initCourses();
    }

    @Test
    void initSchema_shouldNotInitializedGroupsStudentsCoursesTables_whenStudentsTableNotEmpty() {
        when(groupServiceMock.getAllGroups()).thenReturn(new ArrayList<Group>());
        when(studentServiceMock.getAllStudents()).thenReturn(Collections.singletonList(new Student()));
        when(courseServiceMock.getAllCourses()).thenReturn(new ArrayList<Course>());

        serviceFacade.initSchema();

        verify(groupServiceMock, never()).initGroups();
        verify(studentServiceMock, never()).initStudents(new ArrayList<Group>());
        verify(courseServiceMock, never()).initCourses();
    }

    @Test
    void initSchema_shouldNotInitializedGroupsStudentsCoursesTables_whenCoursesTableNotEmpty() {
        when(groupServiceMock.getAllGroups()).thenReturn(new ArrayList<Group>());
        when(studentServiceMock.getAllStudents()).thenReturn(new ArrayList<Student>());
        when(courseServiceMock.getAllCourses()).thenReturn(Collections.singletonList(new Course()));

        serviceFacade.initSchema();

        verify(groupServiceMock, never()).initGroups();
        verify(studentServiceMock, never()).initStudents(new ArrayList<Group>());
        verify(courseServiceMock, never()).initCourses();
    }

    @Test
    void addNewGroup_shouldAddedNewGroup_whenNoGroupWithGivenNewNameAndGroupNameMatchesPattern() {
        String groupName = "FL-23";
        GroupDto newGroup = new GroupDto(groupName);
        when(validatorMock.validateGroupNameExistence(groupName)).thenReturn(false);
        when(validatorMock.validateGroupNamePattern(groupName)).thenReturn(true);

        boolean expectedResult = serviceFacade.addNewGroup(groupName);

        verify(groupServiceMock, times(1)).addGroup(newGroup);
        assertTrue(expectedResult);
    }

    @Test
    void addNewGroup_shouldNotAddedNewGroup_whenGroupWithGivenNewNameAlreadyExistAndGroupNameMatchesPattern() {
        String groupName = "FL-23";
        GroupDto newGroup = new GroupDto(groupName);
        when(validatorMock.validateGroupNameExistence(groupName)).thenReturn(true);
        when(validatorMock.validateGroupNamePattern(groupName)).thenReturn(true);

        boolean expectedResult = serviceFacade.addNewGroup(groupName);

        verify(groupServiceMock, never()).addGroup(newGroup);
        assertFalse(expectedResult);
    }

    @Test
    void addNewGroup_shouldNotAddedNewGroup_whenNoGroupWithGivenNewNameAndGroupNameNotMatchesPattern() {
        String groupName = "NotMatchesPattern";
        GroupDto newGroup = new GroupDto(groupName);
        when(validatorMock.validateGroupNameExistence(groupName)).thenReturn(false);
        when(validatorMock.validateGroupNamePattern(groupName)).thenReturn(false);

        boolean expectedResult = serviceFacade.addNewGroup(groupName);

        verify(groupServiceMock, never()).addGroup(newGroup);
        assertFalse(expectedResult);
    }

    @Test
    void getGroupByName_shouldReturnedGroup_whenGroupWithGivenNameExist() {
        String groupName = "FL-23";
        Group group = new Group(groupName);
        GroupDto expectedGroup = GroupMapper.mapGroupToDto(group);
        when(validatorMock.validateGroupNameExistence(groupName)).thenReturn(true);
        when(groupServiceMock.getGroupByName(groupName)).thenReturn(Optional.of(group));

        GroupDto actualdGroup = serviceFacade.getGroupByName(groupName);

        verify(groupServiceMock, times(1)).getGroupByName(groupName);
        assertEquals(expectedGroup, actualdGroup);
    }

    @Test
    void getGroupByName_shouldReturnedNull_whenNoGroupWithGivenName() {
        String groupName = "FL-23";
        when(validatorMock.validateGroupNameExistence(groupName)).thenReturn(false);

        GroupDto actualdGroup = serviceFacade.getGroupByName(groupName);

        verify(groupServiceMock, never()).getGroupByName(groupName);
        assertNull(actualdGroup);
    }

    @Test
    void getAllGroups_shouldReturnedGroupsDtoList_whenGroupServiceReturnSeveralGroups() {
        List<Group> allGroups = new ArrayList<Group>();
        Group firstGroup = new Group();
        Group secondGroup = new Group();
        Group thirdtGroup = new Group();
        allGroups.add(firstGroup);
        allGroups.add(secondGroup);
        allGroups.add(thirdtGroup);
        List<GroupDto> expectedAllGroups = allGroups.stream().map(GroupMapper::mapGroupToDto).toList();
        when(groupServiceMock.getAllGroups()).thenReturn(allGroups);

        List<GroupDto> actualdAllGroups = serviceFacade.getAllGroups();

        verify(groupServiceMock, times(1)).getAllGroups();
        assertEquals(expectedAllGroups, actualdAllGroups);
    }

    @Test
    void getAllGroups_shouldReturnedEmptyGroupsDtoList_whenGroupServiceReturnEmptyGroupsList() {
        List<Group> allGroups = new ArrayList<Group>();
        List<GroupDto> expectedAllGroups = new ArrayList<GroupDto>();
        when(groupServiceMock.getAllGroups()).thenReturn(allGroups);

        List<GroupDto> actualdAllGroups = serviceFacade.getAllGroups();

        verify(groupServiceMock, times(1)).getAllGroups();
        assertEquals(expectedAllGroups, actualdAllGroups);
    }

    @Test
    void updateGroup_shouldUpdatedGroup_whenGroupToUpdateExistAndNoGroupWithGivenNewNameAndNewGroupNameMatchesPattern() {
        String groupNameToUpdate = "FL-23";
        String newGroupName = "KS-32";
        Group groupToUpdate = new Group(groupNameToUpdate);
        Group updatedgroup = new Group(newGroupName);
        when(validatorMock.validateGroupNameExistence(newGroupName)).thenReturn(false);
        when(validatorMock.validateGroupNamePattern(newGroupName)).thenReturn(true);
        when(groupServiceMock.getGroupByName(groupNameToUpdate)).thenReturn(Optional.of(groupToUpdate));

        boolean expectedResult = serviceFacade.updateGroup(groupNameToUpdate, newGroupName);

        verify(groupServiceMock, times(1)).updateGroup(updatedgroup);
        assertTrue(expectedResult);
    }

    @Test
    void updateGroup_shouldNothingUpdated_whenNoGroupToUpdateAndNoGroupWithGivenNewNameAndNewGroupNameMatchesPattern() {
        String groupNameToUpdate = "FL-23";
        String newGroupName = "KS-32";
        Group updatedgroup = new Group(newGroupName);
        when(validatorMock.validateGroupNameExistence(newGroupName)).thenReturn(false);
        when(validatorMock.validateGroupNamePattern(newGroupName)).thenReturn(true);
        when(groupServiceMock.getGroupByName(groupNameToUpdate)).thenReturn(Optional.empty());

        boolean expectedResult = serviceFacade.updateGroup(groupNameToUpdate, newGroupName);

        verify(groupServiceMock, never()).updateGroup(updatedgroup);
        assertFalse(expectedResult);
    }

    @Test
    void updateGroup_shouldNothingUpdated_whenGroupToUpdateExistAndGroupWithGivenNewNameAlreadyExistAndNewGroupNameMatchesPattern() {
        String groupNameToUpdate = "FL-23";
        String newGroupName = "KS-32";
        Group groupToUpdate = new Group(groupNameToUpdate);
        Group updatedgroup = new Group(newGroupName);
        when(validatorMock.validateGroupNameExistence(newGroupName)).thenReturn(true);
        when(validatorMock.validateGroupNamePattern(newGroupName)).thenReturn(true);
        when(groupServiceMock.getGroupByName(groupNameToUpdate)).thenReturn(Optional.of(groupToUpdate));

        boolean expectedResult = serviceFacade.updateGroup(groupNameToUpdate, newGroupName);

        verify(groupServiceMock, never()).updateGroup(updatedgroup);
        assertFalse(expectedResult);
    }

    @Test
    void updateGroup_shouldNothingUpdated_whenGroupToUpdateExistAndNoGroupWithGivenNewNameAndNewGroupNameNotMatchesPattern() {
        String groupNameToUpdate = "FL-23";
        String newGroupName = "KS-32";
        Group groupToUpdate = new Group(groupNameToUpdate);
        Group updatedgroup = new Group(newGroupName);
        when(validatorMock.validateGroupNameExistence(newGroupName)).thenReturn(false);
        when(validatorMock.validateGroupNamePattern(newGroupName)).thenReturn(false);
        when(groupServiceMock.getGroupByName(groupNameToUpdate)).thenReturn(Optional.of(groupToUpdate));

        boolean expectedResult = serviceFacade.updateGroup(groupNameToUpdate, newGroupName);

        verify(groupServiceMock, never()).updateGroup(updatedgroup);
        assertFalse(expectedResult);
    }

    @Test
    void deleteGroupByName_shouldDeletedGroup_whenGroupWithGivenNameExist() {
        String groupNameToDelete = "FL-23";
        when(validatorMock.validateGroupNameExistence(groupNameToDelete)).thenReturn(true);

        boolean expectedResult = serviceFacade.deleteGroupByName(groupNameToDelete);

        verify(groupServiceMock, times(1)).deleteGroupByName(groupNameToDelete);
        assertTrue(expectedResult);
    }

    @Test
    void deleteGroupByName_shouldNothingDeleted_whenNoGroupWithGivenName() {
        String groupNameToDelete = "FL-23";
        when(validatorMock.validateGroupNameExistence(groupNameToDelete)).thenReturn(false);

        boolean expectedResult = serviceFacade.deleteGroupByName(groupNameToDelete);

        verify(groupServiceMock, never()).deleteGroupByName(groupNameToDelete);
        assertFalse(expectedResult);
    }

    @Test
    void addNewStudent_shouldAddedNewStudent_whenNoStudnentWithGivenNewFullNameAndFirstNameIsValidAndLastNameIsValidAndGroupWithGivenNameExist() {
        String firstName = "FirstName";
        String lastName = "LastName";
        String groupName = "FL-23";
        Group groupForStudent = new Group(groupName);
        StudentDto student = new StudentDto(firstName, lastName, groupForStudent);
        when(validatorMock.validateStudentFullName(firstName, lastName)).thenReturn(false);
        when(validatorMock.validateNameLength(firstName)).thenReturn(true);
        when(validatorMock.validateNameLength(lastName)).thenReturn(true);
        when(validatorMock.validateGroupNameExistence(groupName)).thenReturn(true);
        when(groupServiceMock.getGroupByName(groupName)).thenReturn(Optional.of(groupForStudent));

        boolean expectedResult = serviceFacade.addNewStudent(firstName, lastName, groupName);

        verify(studentServiceMock, times(1)).addStudent(student);
        assertTrue(expectedResult);
    }

    @Test
    void addNewStudent_shouldNotAddenNewStudent_whenStudnentWithGivenNewFullNameAlreadyExistAndFirstNameIsValidAndLastNameIsValidAndGroupWithGivenNameExist() {
        String firstName = "FirstName";
        String lastName = "LastName";
        String groupName = "FL-23";
        Group groupForStudent = new Group(groupName);
        StudentDto student = new StudentDto(firstName, lastName, groupForStudent);
        when(validatorMock.validateStudentFullName(firstName, lastName)).thenReturn(true);
        when(validatorMock.validateNameLength(firstName)).thenReturn(true);
        when(validatorMock.validateNameLength(lastName)).thenReturn(true);
        when(validatorMock.validateGroupNameExistence(groupName)).thenReturn(true);

        boolean expectedResult = serviceFacade.addNewStudent(firstName, lastName, groupName);

        verify(studentServiceMock, never()).addStudent(student);
        assertFalse(expectedResult);
    }

    @Test
    void addNewStudent_shouldNotAddenNewStudent_whenNoStudnentWithGivenNewFullNameAndFirstNameIsNotValidAndLastNameIsValidAndGroupWithGivenNameExist() {
        String firstName = "FffiiirrrssstttNnnaaammmeeeTttoooLong";
        String lastName = "LastName";
        String groupName = "FL-23";
        Group groupForStudent = new Group(groupName);
        StudentDto student = new StudentDto(firstName, lastName, groupForStudent);
        when(validatorMock.validateStudentFullName(firstName, lastName)).thenReturn(false);
        when(validatorMock.validateNameLength(firstName)).thenReturn(false);
        when(validatorMock.validateNameLength(lastName)).thenReturn(true);
        when(validatorMock.validateGroupNameExistence(groupName)).thenReturn(true);

        boolean expectedResult = serviceFacade.addNewStudent(firstName, lastName, groupName);

        verify(studentServiceMock, never()).addStudent(student);
        assertFalse(expectedResult);
    }

    @Test
    void addNewStudent_shouldNotAddenNewStudent_whenNoStudnentWithGivenNewFullNameAndFirstNameIsValidAndLastNameIsNotValidAndGroupWithGivenNameExist() {
        String firstName = "FirstName";
        String lastName = "LlllaaaasssstttNnnaaammmeeeTttooLong";
        String groupName = "FL-23";
        Group groupForStudent = new Group(groupName);
        StudentDto student = new StudentDto(firstName, lastName, groupForStudent);
        when(validatorMock.validateStudentFullName(firstName, lastName)).thenReturn(false);
        when(validatorMock.validateNameLength(firstName)).thenReturn(true);
        when(validatorMock.validateNameLength(lastName)).thenReturn(false);
        when(validatorMock.validateGroupNameExistence(groupName)).thenReturn(true);

        boolean expectedResult = serviceFacade.addNewStudent(firstName, lastName, groupName);

        verify(studentServiceMock, never()).addStudent(student);
        assertFalse(expectedResult);
    }

    @Test
    void addNewStudent_shouldNotAddenNewStudent_whenNoStudnentWithGivenNewFullNameAndFirstNameIsValidAndLastNameIsValidAndNoGroupWithGivenName() {
        String firstName = "FirstName";
        String lastName = "LasrName";
        String groupName = "FL-23";
        Group groupForStudent = new Group(groupName);
        StudentDto student = new StudentDto(firstName, lastName, groupForStudent);
        when(validatorMock.validateStudentFullName(firstName, lastName)).thenReturn(false);
        when(validatorMock.validateNameLength(firstName)).thenReturn(true);
        when(validatorMock.validateNameLength(lastName)).thenReturn(true);
        when(validatorMock.validateGroupNameExistence(groupName)).thenReturn(false);

        boolean expectedResult = serviceFacade.addNewStudent(firstName, lastName, groupName);

        verify(studentServiceMock, never()).addStudent(student);
        assertFalse(expectedResult);
    }

    @Test
    void getStudentById_shouldReturnedStudentDto_whenStudnentWithGivenIdExist() {
        Integer studentId = 1;
        Student student = new Student("FirstName", "LasrName", new Group());
        StudentDto expectedStudent = StudentMapper.mapStudentToDto(student);
        when(validatorMock.validateStudentId(studentId)).thenReturn(true);
        when(studentServiceMock.getStudentById(studentId)).thenReturn(Optional.of(student));

        StudentDto actualStudent = serviceFacade.getStudentById(studentId);

        verify(studentServiceMock, times(1)).getStudentById(studentId);
        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void getStudentById_shouldReturnedNull_whenNoStudnentWithGivenId() {
        Integer studentId = 0;
        when(validatorMock.validateStudentId(studentId)).thenReturn(false);

        StudentDto actualStudent = serviceFacade.getStudentById(studentId);

        verify(studentServiceMock, never()).getStudentById(studentId);
        assertNull(actualStudent);
    }

    @Test
    void updateStudent_shouldUpdatedStudent_whenStudnentToUpdateExistAndNewStudentFullNameNotAlreadyExistAndNewFirstNameIsValidAndNewLastNameIsValidAndGroupWithGivenNameExist() {
        String studentFirstNameToUpdate = "OldFirstName";
        String studentLastNameToUpdate = "OldLastName";
        Student oldStudent = new Student(studentFirstNameToUpdate, studentLastNameToUpdate, new Group());
        String newFirstName = "NewFirstName";
        String newLastName = "NewLastName";
        String newGroupName = "JS-59";
        Group group = new Group(newGroupName);
        Student updatedStudent = new Student(newFirstName, newLastName, group);
        when(validatorMock.validateStudentFullName(newFirstName, newLastName)).thenReturn(false);
        when(validatorMock.validateNameLength(newFirstName)).thenReturn(true);
        when(validatorMock.validateNameLength(newLastName)).thenReturn(true);
        when(studentServiceMock.getStudentByFullName(studentFirstNameToUpdate, studentLastNameToUpdate))
                .thenReturn(Optional.of(oldStudent));
        when(groupServiceMock.getGroupByName(newGroupName)).thenReturn(Optional.of(group));

        boolean actualResult = serviceFacade.updateStudent(studentFirstNameToUpdate, studentLastNameToUpdate,
                newFirstName, newLastName, newGroupName);

        verify(studentServiceMock, times(1)).updateStudent(updatedStudent);
        assertTrue(actualResult);
    }

    @Test
    void updateStudent_shouldUpdatedStudent_whenStudnentToUpdateExistAndNewFirstNameEqualsOldFirstNameAndNewLastNameEqualsOldLastNameAndGroupWithGivenNameExist() {
        String studentFirstNameToUpdate = "OldFirstName";
        String studentLastNameToUpdate = "OldLastName";
        Student oldStudent = new Student(studentFirstNameToUpdate, studentLastNameToUpdate, new Group());
        String newFirstName = "OldFirstName";
        String newLastName = "OldLastName";
        String newGroupName = "JS-59";
        Group group = new Group(newGroupName);
        Student updatedStudent = new Student(newFirstName, newLastName, group);
        when(validatorMock.validateStudentFullName(newFirstName, newLastName)).thenReturn(true);
        when(validatorMock.validateNameLength(newFirstName)).thenReturn(true);
        when(validatorMock.validateNameLength(newLastName)).thenReturn(true);
        when(studentServiceMock.getStudentByFullName(studentFirstNameToUpdate, studentLastNameToUpdate))
                .thenReturn(Optional.of(oldStudent));
        when(groupServiceMock.getGroupByName(newGroupName)).thenReturn(Optional.of(group));

        boolean actualResult = serviceFacade.updateStudent(studentFirstNameToUpdate, studentLastNameToUpdate,
                newFirstName, newLastName, newGroupName);

        verify(studentServiceMock, times(1)).updateStudent(updatedStudent);
        assertTrue(actualResult);
    }

    @Test
    void updateStudent_shouldUpdatedStudent_whenStudnentToUpdateExistAndNewFirstNameEqualsOldFirstNameAndNewLastNameNotEqualsOldLastNameAndGroupWithGivenNameExist() {
        String studentFirstNameToUpdate = "OldFirstName";
        String studentLastNameToUpdate = "OldLastName";
        Student oldStudent = new Student(studentFirstNameToUpdate, studentLastNameToUpdate, new Group());
        String newFirstName = "OldFirstName";
        String newLastName = "NewLastName";
        String newGroupName = "JS-59";
        Group group = new Group(newGroupName);
        Student updatedStudent = new Student(newFirstName, newLastName, group);
        when(validatorMock.validateStudentFullName(newFirstName, newLastName)).thenReturn(false);
        when(validatorMock.validateNameLength(newFirstName)).thenReturn(true);
        when(validatorMock.validateNameLength(newLastName)).thenReturn(true);
        when(studentServiceMock.getStudentByFullName(studentFirstNameToUpdate, studentLastNameToUpdate))
                .thenReturn(Optional.of(oldStudent));
        when(groupServiceMock.getGroupByName(newGroupName)).thenReturn(Optional.of(group));

        boolean actualResult = serviceFacade.updateStudent(studentFirstNameToUpdate, studentLastNameToUpdate,
                newFirstName, newLastName, newGroupName);

        verify(studentServiceMock, times(1)).updateStudent(updatedStudent);
        assertTrue(actualResult);
    }

    @Test
    void updateStudent_shouldNotUpdatedStudent_whenNoStudnentToUpdateAndNewStudentFullNameNotAlreadyExistAndNewFirstNameIsValidAndNewLastNameIsValidAndGroupWithGivenNameExist() {
        String studentFirstNameToUpdate = "NotExistent";
        String studentLastNameToUpdate = "NotExistent";
        String newFirstName = "NewFirstName";
        String newLastName = "NewLastName";
        String newGroupName = "JS-59";
        Group group = new Group(newGroupName);
        Student updatedStudent = new Student(newFirstName, newLastName, group);
        when(validatorMock.validateStudentFullName(newFirstName, newLastName)).thenReturn(false);
        when(validatorMock.validateNameLength(newFirstName)).thenReturn(true);
        when(validatorMock.validateNameLength(newLastName)).thenReturn(true);
        when(studentServiceMock.getStudentByFullName(studentFirstNameToUpdate, studentLastNameToUpdate))
                .thenReturn(Optional.empty());
        when(groupServiceMock.getGroupByName(newGroupName)).thenReturn(Optional.of(group));

        boolean actualResult = serviceFacade.updateStudent(studentFirstNameToUpdate, studentLastNameToUpdate,
                newFirstName, newLastName, newGroupName);

        verify(studentServiceMock, never()).updateStudent(updatedStudent);
        assertFalse(actualResult);
    }

    @Test
    void updateStudent_shouldNotUpdatedStudent_whenStudnentToUpdateExistAndNewStudentFullNameAlreadyExistAndNewFirstNameIsValidAndNewLastNameIsValidAndGroupWithGivenNameExist() {
        String studentFirstNameToUpdate = "OldFirstName";
        String studentLastNameToUpdate = "OldLastName";
        Student oldStudent = new Student(studentFirstNameToUpdate, studentLastNameToUpdate, new Group());
        String newFirstName = "NewFirstName";
        String newLastName = "NewLastName";
        String newGroupName = "JS-59";
        Group group = new Group(newGroupName);
        Student updatedStudent = new Student(newFirstName, newLastName, group);
        when(validatorMock.validateStudentFullName(newFirstName, newLastName)).thenReturn(true);
        when(validatorMock.validateNameLength(newFirstName)).thenReturn(true);
        when(validatorMock.validateNameLength(newLastName)).thenReturn(true);
        when(studentServiceMock.getStudentByFullName(studentFirstNameToUpdate, studentLastNameToUpdate))
                .thenReturn(Optional.of(oldStudent));
        when(groupServiceMock.getGroupByName(newGroupName)).thenReturn(Optional.of(group));

        boolean actualResult = serviceFacade.updateStudent(studentFirstNameToUpdate, studentLastNameToUpdate,
                newFirstName, newLastName, newGroupName);

        verify(studentServiceMock, never()).updateStudent(updatedStudent);
        assertFalse(actualResult);
    }

    @Test
    void updateStudent_shouldNotUpdatedStudent_whenStudnentToUpdateExistAndNewStudentFullNameNotAlreadyExistAndNewFirstNameIsNotValidAndNewLastNameIsValidAndGroupWithGivenNameExist() {
        String studentFirstNameToUpdate = "OldFirstName";
        String studentLastNameToUpdate = "OldLastName";
        Student oldStudent = new Student(studentFirstNameToUpdate, studentLastNameToUpdate, new Group());
        String newFirstName = "NnnneeewwwwFffiiirrrsssttNameToLong";
        String newLastName = "NewLastName";
        String newGroupName = "JS-59";
        Group group = new Group(newGroupName);
        Student updatedStudent = new Student(newFirstName, newLastName, group);
        when(validatorMock.validateStudentFullName(newFirstName, newLastName)).thenReturn(false);
        when(validatorMock.validateNameLength(newFirstName)).thenReturn(false);
        when(validatorMock.validateNameLength(newLastName)).thenReturn(true);
        when(studentServiceMock.getStudentByFullName(studentFirstNameToUpdate, studentLastNameToUpdate))
                .thenReturn(Optional.of(oldStudent));
        when(groupServiceMock.getGroupByName(newGroupName)).thenReturn(Optional.of(group));

        boolean actualResult = serviceFacade.updateStudent(studentFirstNameToUpdate, studentLastNameToUpdate,
                newFirstName, newLastName, newGroupName);

        verify(studentServiceMock, never()).updateStudent(updatedStudent);
        assertFalse(actualResult);
    }

    @Test
    void updateStudent_shouldNotUpdatedStudent_whenStudnentToUpdateExistAndNewStudentFullNameNotAlreadyExistAndNewFirstNameIsValidAndNewLastNameIsNotValidAndGroupWithGivenNameExist() {
        String studentFirstNameToUpdate = "OldFirstName";
        String studentLastNameToUpdate = "OldLastName";
        Student oldStudent = new Student(studentFirstNameToUpdate, studentLastNameToUpdate, new Group());
        String newFirstName = "NewFirstName";
        String newLastName = "NnneeewwwLllaaassstttNnnaaammmeeeToLong";
        String newGroupName = "JS-59";
        Group group = new Group(newGroupName);
        Student updatedStudent = new Student(newFirstName, newLastName, group);
        when(validatorMock.validateStudentFullName(newFirstName, newLastName)).thenReturn(false);
        when(validatorMock.validateNameLength(newFirstName)).thenReturn(true);
        when(validatorMock.validateNameLength(newLastName)).thenReturn(false);
        when(studentServiceMock.getStudentByFullName(studentFirstNameToUpdate, studentLastNameToUpdate))
                .thenReturn(Optional.of(oldStudent));
        when(groupServiceMock.getGroupByName(newGroupName)).thenReturn(Optional.of(group));

        boolean actualResult = serviceFacade.updateStudent(studentFirstNameToUpdate, studentLastNameToUpdate,
                newFirstName, newLastName, newGroupName);

        verify(studentServiceMock, never()).updateStudent(updatedStudent);
        assertFalse(actualResult);
    }

    @Test
    void updateStudent_shouldUpdatedStudent_whenStudnentToUpdateExistAndNewStudentFullNameNotAlreadyExistAndNewFirstNameIsValidAndNewLastNameIsValidAndNoGroupWithGivenName() {
        String studentFirstNameToUpdate = "OldFirstName";
        String studentLastNameToUpdate = "OldLastName";
        Student oldStudent = new Student(studentFirstNameToUpdate, studentLastNameToUpdate, new Group());
        String newFirstName = "NewFirstName";
        String newLastName = "NewLastName";
        String newGroupName = "JS-59";
        Group group = new Group(newGroupName);
        Student updatedStudent = new Student(newFirstName, newLastName, group);
        when(validatorMock.validateStudentFullName(newFirstName, newLastName)).thenReturn(false);
        when(validatorMock.validateNameLength(newFirstName)).thenReturn(true);
        when(validatorMock.validateNameLength(newLastName)).thenReturn(true);
        when(studentServiceMock.getStudentByFullName(studentFirstNameToUpdate, studentLastNameToUpdate))
                .thenReturn(Optional.of(oldStudent));
        when(groupServiceMock.getGroupByName(newGroupName)).thenReturn(Optional.empty());

        boolean actualResult = serviceFacade.updateStudent(studentFirstNameToUpdate, studentLastNameToUpdate,
                newFirstName, newLastName, newGroupName);

        verify(studentServiceMock, never()).updateStudent(updatedStudent);
        assertFalse(actualResult);
    }

    @Test
    void deleteStudentById_shouldDeletedStudent_whenGivenStudentIdCorrect() {
        int studentId = 1;
        when(validatorMock.validateStudentId(studentId)).thenReturn(true);

        boolean studentDeleted = serviceFacade.deleteStudentById(studentId);

        verify(studentServiceMock, times(1)).deleteStudentById(studentId);
        assertTrue(studentDeleted);
    }

    @Test
    void deleteStudentById_shouldNotDeletedStudent_whenGivenStudentIdIsNotCorrect() {
        int studentId = 1;
        when(validatorMock.validateStudentId(studentId)).thenReturn(false);

        boolean studentDeleted = serviceFacade.deleteStudentById(studentId);

        verify(studentServiceMock, never()).deleteStudentById(studentId);
        assertFalse(studentDeleted);
    }

    @Test
    void addNewCourse_shouldAddedNewCourse_whenNoCourseWithGivenNewCourseNameAndDescriptionAndCouseNameLengthIsValid() {
        String courseName = "CourseName";
        String description = "Description";
        CourseDto newCourse = new CourseDto(courseName, description);
        when(validatorMock.validateCourseName(courseName)).thenReturn(false);
        when(validatorMock.validateNameLength(courseName)).thenReturn(true);
        when(validatorMock.validateDescription(description)).thenReturn(false);

        boolean actualResult = serviceFacade.addNewCourse(courseName, description);

        verify(courseServiceMock, times(1)).addCourse(newCourse);
        assertTrue(actualResult);
    }

    @Test
    void addNewCourse_shouldNotAddedNewCourse_whenCourseWithGivenNewCourseNameAlreadyExist() {
        String courseName = "AlreadyExist";
        String description = "Description";
        CourseDto newCourse = new CourseDto(courseName, description);
        when(validatorMock.validateCourseName(courseName)).thenReturn(true);
        when(validatorMock.validateNameLength(courseName)).thenReturn(true);
        when(validatorMock.validateDescription(description)).thenReturn(false);

        boolean actualResult = serviceFacade.addNewCourse(courseName, description);

        verify(courseServiceMock, never()).addCourse(newCourse);
        assertFalse(actualResult);
    }

    @Test
    void addNewCourse_shouldNotAddedNewCourse_whenNoCourseWithGivenNewCourseNameAndDescriptionAndCouseNameLengthIsNotValid() {
        String courseName = "CccooouuurrrrssseeeNnnaaammmeTttooLong";
        String description = "Description";
        CourseDto newCourse = new CourseDto(courseName, description);
        when(validatorMock.validateCourseName(courseName)).thenReturn(false);
        when(validatorMock.validateNameLength(courseName)).thenReturn(false);
        when(validatorMock.validateDescription(description)).thenReturn(false);

        boolean actualResult = serviceFacade.addNewCourse(courseName, description);

        verify(courseServiceMock, never()).addCourse(newCourse);
        assertFalse(actualResult);
    }

    @Test
    void addNewCourse_shouldNotAddedNewCourse_whenCourseWithGivenDescriptionAlreadyExistAndCouseNameLengthIsValid() {
        String courseName = "CourseName";
        String description = "AlreadyExist";
        CourseDto newCourse = new CourseDto(courseName, description);
        when(validatorMock.validateCourseName(courseName)).thenReturn(false);
        when(validatorMock.validateNameLength(courseName)).thenReturn(true);
        when(validatorMock.validateDescription(description)).thenReturn(true);

        boolean actualResult = serviceFacade.addNewCourse(courseName, description);

        verify(courseServiceMock, never()).addCourse(newCourse);
        assertFalse(actualResult);
    }

    @Test
    void addNewCourse_shouldNotAddedNewCourse_whenCourseWithGivenNewCourseNameAndDescriptionAlreadyExistAndCouseNameLengthIsValid() {
        String courseName = "AlreadyExist";
        String description = "AlreadyExist";
        CourseDto newCourse = new CourseDto(courseName, description);
        when(validatorMock.validateCourseName(courseName)).thenReturn(true);
        when(validatorMock.validateNameLength(courseName)).thenReturn(true);
        when(validatorMock.validateDescription(description)).thenReturn(true);

        boolean actualResult = serviceFacade.addNewCourse(courseName, description);

        verify(courseServiceMock, never()).addCourse(newCourse);
        assertFalse(actualResult);
    }

    @Test
    void getCourseByName_shouldReturnedCourseDto_whenCourseWithGivenNameExist() {
        String courseName = "CourseName";
        String description = "Description";
        Course course = new Course(courseName, description);
        CourseDto expectedCourse = CourseMapper.mapCourseToDto(course);
        when(validatorMock.validateCourseName(courseName)).thenReturn(true);
        when(courseServiceMock.getCourseByName(courseName)).thenReturn(Optional.of(course));

        CourseDto actualCourse = serviceFacade.getCourseByName(courseName);

        verify(courseServiceMock, times(1)).getCourseByName(courseName);
        assertEquals(expectedCourse, actualCourse);
    }

    @Test
    void getCourseByName_shouldReturnedNull_whenNoCourseWithGivenName() {
        String courseName = "CourseName";
        when(validatorMock.validateCourseName(courseName)).thenReturn(false);

        CourseDto actualCourse = serviceFacade.getCourseByName(courseName);

        verify(courseServiceMock, never()).getCourseByName(courseName);
        assertNull(actualCourse);
    }

    @Test
    void getAllCourses_shouldCoursesDtosList_whenCourseServiceReturnCoursesList() {
        Course firstCourse = new Course();
        Course secondCourse = new Course();
        Course thirdCourse = new Course();
        List<Course> allCourses = new ArrayList<Course>();
        allCourses.add(firstCourse);
        allCourses.add(secondCourse);
        allCourses.add(thirdCourse);
        when(courseServiceMock.getAllCourses()).thenReturn(allCourses);

        List<CourseDto> allCoursesDtos = serviceFacade.getAllCourses();

        verify(courseServiceMock, times(1)).getAllCourses();
        assertEquals(3, allCoursesDtos.size());
    }

    @Test
    void getAllCourses_shouldEmptyList_whenCourseServiceReturnEmptyCoursesList() {
        List<Course> allCourses = new ArrayList<Course>();
        when(courseServiceMock.getAllCourses()).thenReturn(allCourses);

        List<CourseDto> allCoursesDtos = serviceFacade.getAllCourses();

        verify(courseServiceMock, times(1)).getAllCourses();
        assertTrue(allCoursesDtos.isEmpty());
    }

    @Test
    void updateCourse_shouldUpdatedCourse_whenCourseToUpdateExistAndNewCourseNameNotAlreadyExistAndNewCourseNameLengthIsValidAndNewDescriptionNotAlreadyExist() {
        String courseNameToUpdate = "OldCourseName";
        Course oldCourse = new Course(courseNameToUpdate, "OldDescription");
        String newCourseName = "NewCourseName";
        String newDescription = "NewDescription";
        Course updatedCourse = new Course(newCourseName, newDescription);
        when(courseServiceMock.getCourseByName(courseNameToUpdate)).thenReturn(Optional.of(oldCourse));
        when(validatorMock.validateCourseName(newCourseName)).thenReturn(false);
        when(validatorMock.validateNameLength(newCourseName)).thenReturn(true);
        when(validatorMock.validateDescription(newDescription)).thenReturn(false);

        boolean expectedResut = serviceFacade.updateCourse(courseNameToUpdate, newCourseName, newDescription);

        verify(courseServiceMock, times(1)).updateCourse(updatedCourse);
        assertTrue(expectedResut);
    }

    @Test
    void updateCourse_shouldUpdatedCourse_whenCourseToUpdateExistAndNewCourseNameEqualsOldCouseNameAndNewCourseNameLengthIsValidAndNewDescriptionNotAlreadyExist() {
        String courseNameToUpdate = "OldCourseName";
        Course oldCourse = new Course(courseNameToUpdate, "OldDescription");
        String newCourseName = "OldCourseName";
        String newDescription = "NewDescription";
        Course updatedCourse = new Course(newCourseName, newDescription);
        when(courseServiceMock.getCourseByName(courseNameToUpdate)).thenReturn(Optional.of(oldCourse));
        when(validatorMock.validateCourseName(newCourseName)).thenReturn(true);
        when(validatorMock.validateNameLength(newCourseName)).thenReturn(true);
        when(validatorMock.validateDescription(newDescription)).thenReturn(false);

        boolean expectedResut = serviceFacade.updateCourse(courseNameToUpdate, newCourseName, newDescription);

        verify(courseServiceMock, times(1)).updateCourse(updatedCourse);
        assertTrue(expectedResut);
    }

    @Test
    void updateCourse_shouldUpdatedCourse_whenCourseToUpdateExistAndNewCourseNameNotAlreadyExistAndNewCourseNameLengthIsValidAndNewDescriptionEqualsOldDescription() {
        String courseNameToUpdate = "OldCourseName";
        Course oldCourse = new Course(courseNameToUpdate, "OldDescription");
        String newCourseName = "NewCourseName";
        String newDescription = "OldDescription";
        Course updatedCourse = new Course(newCourseName, newDescription);
        when(courseServiceMock.getCourseByName(courseNameToUpdate)).thenReturn(Optional.of(oldCourse));
        when(validatorMock.validateCourseName(newCourseName)).thenReturn(false);
        when(validatorMock.validateNameLength(newCourseName)).thenReturn(true);
        when(validatorMock.validateDescription(newDescription)).thenReturn(true);

        boolean expectedResut = serviceFacade.updateCourse(courseNameToUpdate, newCourseName, newDescription);

        verify(courseServiceMock, times(1)).updateCourse(updatedCourse);
        assertTrue(expectedResut);
    }

    @Test
    void updateCourse_shouldUpdatedCourse_whenCourseToUpdateExistAndNewCourseNameEqualsOldCouseNameAndNewCourseNameLengthIsValidAndNewDescriptionEqualsOldDescription() {
        String courseNameToUpdate = "OldCourseName";
        Course oldCourse = new Course(courseNameToUpdate, "OldDescription");
        String newCourseName = "OldCourseName";
        String newDescription = "OldDescription";
        Course updatedCourse = new Course(newCourseName, newDescription);
        when(courseServiceMock.getCourseByName(courseNameToUpdate)).thenReturn(Optional.of(oldCourse));
        when(validatorMock.validateCourseName(newCourseName)).thenReturn(true);
        when(validatorMock.validateNameLength(newCourseName)).thenReturn(true);
        when(validatorMock.validateDescription(newDescription)).thenReturn(true);

        boolean expectedResut = serviceFacade.updateCourse(courseNameToUpdate, newCourseName, newDescription);

        verify(courseServiceMock, times(1)).updateCourse(updatedCourse);
        assertTrue(expectedResut);
    }

    @Test
    void updateCourse_shouldNotUpdatedCourse_whenNoCourseToUpdateAndNewCourseNameNotAlreadyExistAndNewCourseNameLengthIsValidAndNewDescriptionNotAlreadyExist() {
        String courseNameToUpdate = "OldCourseName";
        String newCourseName = "NewCourseName";
        String newDescription = "NewDescription";
        Course updatedCourse = new Course(newCourseName, newDescription);
        when(courseServiceMock.getCourseByName(courseNameToUpdate)).thenReturn(Optional.empty());
        when(validatorMock.validateCourseName(newCourseName)).thenReturn(false);
        when(validatorMock.validateNameLength(newCourseName)).thenReturn(true);
        when(validatorMock.validateDescription(newDescription)).thenReturn(false);

        boolean expectedResut = serviceFacade.updateCourse(courseNameToUpdate, newCourseName, newDescription);

        verify(courseServiceMock, never()).updateCourse(updatedCourse);
        assertFalse(expectedResut);
    }

    @Test
    void updateCourse_shouldNotUpdatedCourse_whenCourseToUpdateExistAndNewCourseNameAlreadyExistAndNewCourseNameLengthIsValidAndNewDescriptionNotAlreadyExist() {
        String courseNameToUpdate = "OldCourseName";
        Course oldCourse = new Course(courseNameToUpdate, "OldDescription");
        String newCourseName = "NewCourseName";
        String newDescription = "NewDescription";
        Course updatedCourse = new Course(newCourseName, newDescription);
        when(courseServiceMock.getCourseByName(courseNameToUpdate)).thenReturn(Optional.of(oldCourse));
        when(validatorMock.validateCourseName(newCourseName)).thenReturn(true);
        when(validatorMock.validateNameLength(newCourseName)).thenReturn(true);
        when(validatorMock.validateDescription(newDescription)).thenReturn(false);

        boolean expectedResut = serviceFacade.updateCourse(courseNameToUpdate, newCourseName, newDescription);

        verify(courseServiceMock, never()).updateCourse(updatedCourse);
        assertFalse(expectedResut);
    }

    @Test
    void updateCourse_shouldNotUpdatedCourse_whenCourseToUpdateExistAndNewCourseNameNotAlreadyExistAndNewCourseNameLengthIsNotValidAndNewDescriptionNotAlreadyExist() {
        String courseNameToUpdate = "OldCourseName";
        Course oldCourse = new Course(courseNameToUpdate, "OldDescription");
        String newCourseName = "CccooouuurrrrssseeeNnnaaammmeTttooLong";
        String newDescription = "NewDescription";
        Course updatedCourse = new Course(newCourseName, newDescription);
        when(courseServiceMock.getCourseByName(courseNameToUpdate)).thenReturn(Optional.of(oldCourse));
        when(validatorMock.validateCourseName(newCourseName)).thenReturn(false);
        when(validatorMock.validateNameLength(newCourseName)).thenReturn(false);
        when(validatorMock.validateDescription(newDescription)).thenReturn(false);

        boolean expectedResut = serviceFacade.updateCourse(courseNameToUpdate, newCourseName, newDescription);

        verify(courseServiceMock, never()).updateCourse(updatedCourse);
        assertFalse(expectedResut);
    }

    @Test
    void updateCourse_shouldNotUpdatedCourse_whenCourseToUpdateExistAndNewCourseNameNotAlreadyExistAndNewCourseNameLengthIsValidAndNewDescriptionAlreadyExist() {
        String courseNameToUpdate = "OldCourseName";
        Course oldCourse = new Course(courseNameToUpdate, "OldDescription");
        String newCourseName = "NewCourseName";
        String newDescription = "NewDescription";
        Course updatedCourse = new Course(newCourseName, newDescription);
        when(courseServiceMock.getCourseByName(courseNameToUpdate)).thenReturn(Optional.of(oldCourse));
        when(validatorMock.validateCourseName(newCourseName)).thenReturn(false);
        when(validatorMock.validateNameLength(newCourseName)).thenReturn(true);
        when(validatorMock.validateDescription(newDescription)).thenReturn(true);

        boolean expectedResut = serviceFacade.updateCourse(courseNameToUpdate, newCourseName, newDescription);

        verify(courseServiceMock, never()).updateCourse(updatedCourse);
        assertFalse(expectedResut);
    }

    @Test
    void deleteCourseByName_shouldDeletedCourse_whenCourseWithGivenNameExist() {
        String courseNameToDelete = "CourseName";
        when(validatorMock.validateCourseName(courseNameToDelete)).thenReturn(true);

        boolean expectedResut = serviceFacade.deleteCourseByName(courseNameToDelete);

        verify(courseServiceMock, times(1)).deleteCourseByName(courseNameToDelete);
        assertTrue(expectedResut);
    }

    @Test
    void deleteCourseByName_shouldNotDeletedCourse_whenCourseWithGivenNameNotExist() {
        String courseNameToDelete = "CourseName";
        when(validatorMock.validateCourseName(courseNameToDelete)).thenReturn(false);

        boolean expectedResut = serviceFacade.deleteCourseByName(courseNameToDelete);

        verify(courseServiceMock, never()).deleteCourseByName(courseNameToDelete);
        assertFalse(expectedResut);
    }

    @Test
    void getGroupsWithGivenNumberOfStudents_shouldMapGroupsWithTheirNumberOfStudents_whenGroupServiceReturnListOfTwoGroups() {
        Integer amountOfStudents = 3;
        Group firstGroup = new Group("GD-43");
        firstGroup.addStudent(new Student("FirstName_1", "LastName_1", firstGroup));
        firstGroup.addStudent(new Student("FirstName_2", "LastName_2", firstGroup));
        Group secondGroup = new Group("JF-96");
        secondGroup.addStudent(new Student("FirstName_3", "LastName_3", secondGroup));
        secondGroup.addStudent(new Student("FirstName_4", "LastName_4", secondGroup));
        secondGroup.addStudent(new Student("FirstName_5", "LastName_5", secondGroup));
        Group thirdGroup = new Group("BV-83");
        List<Group> groups = new ArrayList<Group>();
        groups.add(firstGroup);
        groups.add(secondGroup);
        groups.add(thirdGroup);
        when(validatorMock.validateAmountOfStudents(amountOfStudents)).thenReturn(true);
        when(groupServiceMock.getGroupsWithGivenNumberOfStudents(amountOfStudents)).thenReturn(groups);

        Map<GroupDto, Integer> groupsWithTheirNumberOfStudents = serviceFacade
                .getGroupsWithGivenNumberOfStudents(amountOfStudents);

        verify(groupServiceMock, times(1)).getGroupsWithGivenNumberOfStudents(amountOfStudents);
        assertEquals(3, groupsWithTheirNumberOfStudents.size());
        assertEquals(2, groupsWithTheirNumberOfStudents.get(GroupMapper.mapGroupToDto(firstGroup)));
        assertEquals(3, groupsWithTheirNumberOfStudents.get(GroupMapper.mapGroupToDto(secondGroup)));
        assertEquals(0, groupsWithTheirNumberOfStudents.get(GroupMapper.mapGroupToDto(thirdGroup)));
    }

    @Test
    void getGroupsWithGivenNumberOfStudents_shouldEmptyMap_whenGroupServiceReturnEmptyGroupList() {
        Integer amountOfStudents = 3;
        List<Group> groups = new ArrayList<Group>();
        when(validatorMock.validateAmountOfStudents(amountOfStudents)).thenReturn(true);
        when(groupServiceMock.getGroupsWithGivenNumberOfStudents(amountOfStudents)).thenReturn(groups);

        Map<GroupDto, Integer> groupsWithTheirNumberOfStudents = serviceFacade
                .getGroupsWithGivenNumberOfStudents(amountOfStudents);

        verify(groupServiceMock, times(1)).getGroupsWithGivenNumberOfStudents(amountOfStudents);
        assertTrue(groupsWithTheirNumberOfStudents.isEmpty());
    }

    @Test
    void getGroupsWithGivenNumberOfStudents_shouldReturnedNull_whenAmountOfStudentsIsNotValid() {
        Integer amountOfStudents = -1;
        when(validatorMock.validateAmountOfStudents(amountOfStudents)).thenReturn(false);

        Map<GroupDto, Integer> groupsWithTheirNumberOfStudents = serviceFacade
                .getGroupsWithGivenNumberOfStudents(amountOfStudents);

        verify(groupServiceMock, never()).getGroupsWithGivenNumberOfStudents(amountOfStudents);
        assertNull(groupsWithTheirNumberOfStudents);
    }

    @Test
    void getStudentsWithCoursesByCourseName_shouldMapStudentsWithTheirCourses_whenCourseServiceReturnCourseWithGivenName() {
        String courseName = "CourseName_1";
        Course course = new Course(courseName, "Description_1");
        Student firstStudent = new Student("FirstName_1", "LastName_1", new Group());
        firstStudent.addCourse(course);
        firstStudent.addCourse(new Course("CourseName_2", "Description_2"));
        Student secondStudent = new Student("FirstName_2", "LastName_2", new Group());
        secondStudent.addCourse(course);
        when(validatorMock.validateCourseName(courseName)).thenReturn(true);
        when(courseServiceMock.getCourseByName(courseName)).thenReturn(Optional.of(course));

        Map<StudentDto, Set<CourseDto>> studentsWithTheirCourses = serviceFacade
                .getStudentsWithCoursesByCourseName(courseName);

        verify(courseServiceMock, times(1)).getCourseByName(courseName);
        assertEquals(2, studentsWithTheirCourses.size());
        assertEquals(2, studentsWithTheirCourses.get(StudentMapper.mapStudentToDto(firstStudent)).size());
        assertEquals(1, studentsWithTheirCourses.get(StudentMapper.mapStudentToDto(secondStudent)).size());
    }

    @Test
    void getStudentsWithCoursesByCourseName_shouldEmptyMap_whenCourseServiceReturnCourseWithGivenNameAndThisCourseNotNaveAnyStudent() {
        String courseName = "CourseName_1";
        Course course = new Course(courseName, "Description_1");
        when(validatorMock.validateCourseName(courseName)).thenReturn(true);
        when(courseServiceMock.getCourseByName(courseName)).thenReturn(Optional.of(course));

        Map<StudentDto, Set<CourseDto>> studentsWithTheirCourses = serviceFacade
                .getStudentsWithCoursesByCourseName(courseName);

        verify(courseServiceMock, times(1)).getCourseByName(courseName);
        assertTrue(studentsWithTheirCourses.isEmpty());
    }

    @Test
    void getStudentsWithCoursesByCourseName_shouldReturnedNull_whenNoCourseWithGivenName() {
        String courseName = "CourseName_1";
        when(validatorMock.validateCourseName(courseName)).thenReturn(false);

        Map<StudentDto, Set<CourseDto>> studentsWithTheirCourses = serviceFacade
                .getStudentsWithCoursesByCourseName(courseName);

        verify(courseServiceMock, never()).getCourseByName(courseName);
        assertNull(studentsWithTheirCourses);
    }

    @Test
    void getAllStudentsWithTheirGroups_shouldStudentsWithTheirGroupsMap_whenStudentServiceReturnStudentsList() {
        String firstGroupName = "HD-63";
        String secondGroupName = "SG-62";
        String thirdGroupName = "KS-63";
        Student firstStudent = new Student("FirstName_1", "LastName_1", new Group(firstGroupName));
        Student secondStudent = new Student("FirstName_2", "LastName_2", new Group(secondGroupName));
        Student thirdStudent = new Student("FirstName_3", "LastName_3", new Group(thirdGroupName));
        List<Student> allStudents = new ArrayList<Student>();
        allStudents.add(firstStudent);
        allStudents.add(secondStudent);
        allStudents.add(thirdStudent);
        when(studentServiceMock.getAllStudents()).thenReturn(allStudents);

        Map<StudentDto, GroupDto> studentsWithTheirCourses = serviceFacade.getAllStudentsWithTheirGroups();

        verify(studentServiceMock, times(1)).getAllStudents();
        assertEquals(3, studentsWithTheirCourses.size());
        assertEquals(firstGroupName,
                studentsWithTheirCourses.get(StudentMapper.mapStudentToDto(firstStudent)).getGroupName());
        assertEquals(secondGroupName,
                studentsWithTheirCourses.get(StudentMapper.mapStudentToDto(secondStudent)).getGroupName());
        assertEquals(thirdGroupName,
                studentsWithTheirCourses.get(StudentMapper.mapStudentToDto(thirdStudent)).getGroupName());
    }

    @Test
    void getAllStudentsWithTheirGroups_shouldEmptyMap_whenStudentServiceReturnEmptyStudentsList() {
        List<Student> allStudents = new ArrayList<Student>();
        when(studentServiceMock.getAllStudents()).thenReturn(allStudents);

        Map<StudentDto, GroupDto> studentsWithTheirCourses = serviceFacade.getAllStudentsWithTheirGroups();

        verify(studentServiceMock, times(1)).getAllStudents();
        assertTrue(studentsWithTheirCourses.isEmpty());
    }

    @Test
    void addStudentToCourse_shouldAddedStudentToCourse_whenStudentWithGivenNameExistAndCourseWithGivenNameExistAndStudentNotAlreadyRegisteredOnCourse() {
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        Student student = new Student(studentFirstName, studentLastName, new Group());
        String courseName = "CourseName";
        Course course = new Course(courseName, "Description");
        when(validatorMock.validateStudentFullName(studentFirstName, studentLastName)).thenReturn(true);
        when(validatorMock.validateCourseName(courseName)).thenReturn(true);
        when(validatorMock.isStudentOnCourse(studentFirstName, studentLastName, courseName)).thenReturn(false);
        when(studentServiceMock.getStudentByFullName(studentFirstName, studentLastName))
                .thenReturn(Optional.of(student));
        when(courseServiceMock.getCourseByName(courseName)).thenReturn(Optional.of(course));

        boolean studentAddedToCourse = serviceFacade.addStudentToCourse(studentFirstName, studentLastName, courseName);

        verify(studentServiceMock, times(1)).getStudentByFullName(studentFirstName, studentLastName);
        verify(courseServiceMock, times(1)).getCourseByName(courseName);
        assertTrue(studentAddedToCourse);
    }

    @Test
    void addStudentToCourse_shouldNotAddedStudentToCourse_whenNoStudentWithGivenName() {
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        when(validatorMock.validateStudentFullName(studentFirstName, studentLastName)).thenReturn(false);
        when(validatorMock.validateCourseName(courseName)).thenReturn(true);
        when(validatorMock.isStudentOnCourse(studentFirstName, studentLastName, courseName)).thenReturn(false);

        boolean studentAddedToCourse = serviceFacade.addStudentToCourse(studentFirstName, studentLastName, courseName);

        verify(studentServiceMock, never()).getStudentByFullName(studentFirstName, studentLastName);
        verify(courseServiceMock, never()).getCourseByName(courseName);
        assertFalse(studentAddedToCourse);
    }

    @Test
    void addStudentToCourse_shouldNotAddedStudentToCourse_whenNoCourseWithGivenName() {
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        when(validatorMock.validateStudentFullName(studentFirstName, studentLastName)).thenReturn(true);
        when(validatorMock.validateCourseName(courseName)).thenReturn(false);
        when(validatorMock.isStudentOnCourse(studentFirstName, studentLastName, courseName)).thenReturn(false);

        boolean studentAddedToCourse = serviceFacade.addStudentToCourse(studentFirstName, studentLastName, courseName);

        verify(studentServiceMock, never()).getStudentByFullName(studentFirstName, studentLastName);
        verify(courseServiceMock, never()).getCourseByName(courseName);
        assertFalse(studentAddedToCourse);
    }

    @Test
    void addStudentToCourse_shouldNotAddedStudentToCourse_whenStudentAlreadyRegisteredOnCourse() {
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        when(validatorMock.validateStudentFullName(studentFirstName, studentLastName)).thenReturn(true);
        when(validatorMock.validateCourseName(courseName)).thenReturn(true);
        when(validatorMock.isStudentOnCourse(studentFirstName, studentLastName, courseName)).thenReturn(true);

        boolean studentAddedToCourse = serviceFacade.addStudentToCourse(studentFirstName, studentLastName, courseName);

        verify(studentServiceMock, never()).getStudentByFullName(studentFirstName, studentLastName);
        verify(courseServiceMock, never()).getCourseByName(courseName);
        assertFalse(studentAddedToCourse);
    }

    @Test
    void deleteStudentFromCourse_shouldDeletedStudentFromCourse_whenStudentWithGivenNameExistAndCourseWithGivenNameExistAndStudentAlreadyRegisteredOnCourse() {
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        Student student = new Student(studentFirstName, studentLastName, new Group());
        String courseName = "CourseName";
        Course course = new Course(courseName, "Description");
        when(validatorMock.validateStudentFullName(studentFirstName, studentLastName)).thenReturn(true);
        when(validatorMock.validateCourseName(courseName)).thenReturn(true);
        when(validatorMock.isStudentOnCourse(studentFirstName, studentLastName, courseName)).thenReturn(true);
        when(studentServiceMock.getStudentByFullName(studentFirstName, studentLastName))
                .thenReturn(Optional.of(student));
        when(courseServiceMock.getCourseByName(courseName)).thenReturn(Optional.of(course));

        boolean studentDeletedFromCourse = serviceFacade.deleteStudentFromCourse(studentFirstName, studentLastName,
                courseName);

        verify(studentServiceMock, times(1)).getStudentByFullName(studentFirstName, studentLastName);
        verify(courseServiceMock, times(1)).getCourseByName(courseName);
        assertTrue(studentDeletedFromCourse);
    }

    @Test
    void deleteStudentFromCourse_shouldNotDeletedStudentFromCourse_whenNoStudentWithGivenName() {
        serviceFacade = new ServiceFacadeImpl(groupServiceMock, studentServiceMock, courseServiceMock, validatorMock);
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        when(validatorMock.validateStudentFullName(studentFirstName, studentLastName)).thenReturn(false);
        when(validatorMock.validateCourseName(courseName)).thenReturn(true);
        when(validatorMock.isStudentOnCourse(studentFirstName, studentLastName, courseName)).thenReturn(false);

        boolean studentDeletedFromCourse = serviceFacade.deleteStudentFromCourse(studentFirstName, studentLastName,
                courseName);

        verify(studentServiceMock, never()).getStudentByFullName(studentFirstName, studentLastName);
        verify(courseServiceMock, never()).getCourseByName(courseName);
        assertFalse(studentDeletedFromCourse);
    }

    @Test
    void deleteStudentFromCourse_shouldNotDeletedStudentFromCourse_whenNoCourseWithGivenName() {
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        when(validatorMock.validateStudentFullName(studentFirstName, studentLastName)).thenReturn(true);
        when(validatorMock.validateCourseName(courseName)).thenReturn(false);
        when(validatorMock.isStudentOnCourse(studentFirstName, studentLastName, courseName)).thenReturn(false);

        boolean studentDeletedFromCourse = serviceFacade.deleteStudentFromCourse(studentFirstName, studentLastName,
                courseName);

        verify(studentServiceMock, never()).getStudentByFullName(studentFirstName, studentLastName);
        verify(courseServiceMock, never()).getCourseByName(courseName);
        assertFalse(studentDeletedFromCourse);
    }

    @Test
    void deleteStudentFromCourse_shouldNotDeletedStudentFromCourse_whenStudentNotAlreadyRegisteredOnCourse() {
        String studentFirstName = "FirstName";
        String studentLastName = "LastName";
        String courseName = "CourseName";
        when(validatorMock.validateStudentFullName(studentFirstName, studentLastName)).thenReturn(true);
        when(validatorMock.validateCourseName(courseName)).thenReturn(true);
        when(validatorMock.isStudentOnCourse(studentFirstName, studentLastName, courseName)).thenReturn(false);

        boolean studentDeletedFromCourse = serviceFacade.deleteStudentFromCourse(studentFirstName, studentLastName,
                courseName);

        verify(studentServiceMock, never()).getStudentByFullName(studentFirstName, studentLastName);
        verify(courseServiceMock, never()).getCourseByName(courseName);
        assertFalse(studentDeletedFromCourse);
    }

    @Test
    void getAllStudentsWithTheirCourses_shouldMapStudentsWithTheirCourses_whenStudentServiceReturnStudentsList() {
        Student firstStudent = new Student("FirstName_1", "LastName_1", new Group());
        firstStudent.addCourse(new Course("CourseName_1", "Description_1"));
        firstStudent.addCourse(new Course("CourseName_2", "Description_2"));
        Student secondStudent = new Student("FirstName_2", "LastName_2", new Group());
        secondStudent.addCourse(new Course("CourseName_3", "Description_3"));
        List<Student> allStudents = new ArrayList<Student>();
        allStudents.add(firstStudent);
        allStudents.add(secondStudent);
        when(studentServiceMock.getAllStudents()).thenReturn(allStudents);

        Map<StudentDto, Set<CourseDto>> studentsWithTheirCourses = serviceFacade.getAllStudentsWithTheirCourses();

        verify(studentServiceMock, times(1)).getAllStudents();
        assertEquals(2, studentsWithTheirCourses.size());
        assertEquals(2, studentsWithTheirCourses.get(StudentMapper.mapStudentToDto(firstStudent)).size());
        assertEquals(1, studentsWithTheirCourses.get(StudentMapper.mapStudentToDto(secondStudent)).size());
    }

    @Test
    void getAllStudentsWithTheirCourses_shouldEmptyMap_whenStudentServiceReturnEmptyStudentsList() {
        List<Student> allStudents = new ArrayList<Student>();
        when(studentServiceMock.getAllStudents()).thenReturn(allStudents);

        Map<StudentDto, Set<CourseDto>> studentsWithTheirCourses = serviceFacade.getAllStudentsWithTheirCourses();

        verify(studentServiceMock, times(1)).getAllStudents();
        assertTrue(studentsWithTheirCourses.isEmpty());
    }

    @Test
    void addStudentsToCourses_shouldStudentAddedToCourses_whenStudentServiceReturnStudentsList() throws Exception {
        Method method = ServiceFacadeImpl.class.getDeclaredMethod("addStudentsToCourses");
        method.setAccessible(true);
        when(studentServiceMock.getAllStudents()).thenReturn(Arrays.asList(new Student()));
        when(courseServiceMock.getCourseById(anyInt())).thenReturn(Optional.of(new Course()));

        method.invoke(serviceFacade);

        verify(studentServiceMock, times(1)).getAllStudents();
    }

    @Test
    void addStudentsToCourses_shouldNoOneAddedToCourse_whenStudentServiceReturnEmptyStudentsList() throws Exception {
        Method method = ServiceFacadeImpl.class.getDeclaredMethod("addStudentsToCourses");
        method.setAccessible(true);
        when(studentServiceMock.getAllStudents()).thenReturn(new ArrayList<Student>());

        method.invoke(serviceFacade);

        verify(studentServiceMock, times(1)).getAllStudents();
        verify(courseServiceMock, never()).getCourseById(anyInt());
    }

}
