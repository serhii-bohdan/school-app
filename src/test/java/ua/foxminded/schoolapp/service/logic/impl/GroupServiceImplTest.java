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
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.schoolapp.dao.GroupDao;
import ua.foxminded.schoolapp.dto.GroupDto;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.generate.Generatable;

@SpringBootTest(classes = { GroupServiceImpl.class })
class GroupServiceImplTest {

    @MockBean
    Generatable<GroupDto> groupsGeneratorMock;

    @MockBean
    GroupDao groupDaoMock;

    @Autowired
    GroupServiceImpl groupService;

    @Test
    void initGroups_shouldSavedAllGroupsWhichReturnsGroupsGenerator_whenGroupsGeneratorReturnsSeveralGroups() {
        List<GroupDto> generatedGroups = new ArrayList<>();
        generatedGroups.add(new GroupDto());
        generatedGroups.add(new GroupDto());
        generatedGroups.add(new GroupDto());
        when(groupsGeneratorMock.toGenerate()).thenReturn(generatedGroups);

        groupService.initGroups();

        verify(groupsGeneratorMock, times(1)).toGenerate();
        verify(groupDaoMock, times(3)).save(any(Group.class));
    }

    @Test
    void initGroups_shouldNotSaveAnyGroup_whenGroupsGeneratorReturnsEmptyGroupsList() {
        List<GroupDto> generatedGroups = new ArrayList<>();
        when(groupsGeneratorMock.toGenerate()).thenReturn(generatedGroups);

        groupService.initGroups();

        verify(groupsGeneratorMock, times(1)).toGenerate();
        verify(groupDaoMock, never()).save(any(Group.class));
    }

    @Test
    void addGroup_shouldAddedAndReturnedNewGroupOptional_whenGroupDaoSuccessfulSaveNewGroup() {
        String newGroupName = "LK-33";
        Group newGroup = new Group(newGroupName);
        when(groupDaoMock.save(newGroup)).thenReturn(newGroup);

        Optional<Group> newReturnedGroup = groupService.addGroup(newGroupName);

        verify(groupDaoMock, times(1)).save(newGroup);
        assertTrue(newReturnedGroup.isPresent());
        assertEquals(newGroup, newReturnedGroup.get());
    }

    @Test
    void addGroup_shouldConstraintViolationException_whenNewGroupNameIsNullAndGroupDaoThrowConstraintViolationException() {
        String newGroupName = null;
        Group newGroup = new Group(newGroupName);
        when(groupDaoMock.save(newGroup)).thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class, () -> groupService.addGroup(newGroupName));
        verify(groupDaoMock, times(1)).save(newGroup);
    }

    @Test
    void addGroup_shouldDataException_whenNewGroupNameContainsMoreThenFiveCharactersAndGroupDaoThrowDataException() {
        String newGroupName = "LP-130";
        Group newGroup = new Group(newGroupName);
        when(groupDaoMock.save(newGroup)).thenThrow(DataException.class);

        assertThrows(DataException.class, () -> groupService.addGroup(newGroupName));
        verify(groupDaoMock, times(1)).save(newGroup);
    }

    @Test
    void getGroupByName_shouldReturnedGroupOptional_whenGroupDaoFoundGroupByName() {
        String groupName = "LK-33";
        Optional<Group> expectedGroup = Optional.of(new Group(groupName));
        when(groupDaoMock.findGroupByName(groupName)).thenReturn(expectedGroup);

        Optional<Group> actualGroup = groupService.getGroupByName(groupName);

        verify(groupDaoMock, times(1)).findGroupByName(groupName);
        assertTrue(actualGroup.isPresent());
        assertEquals(expectedGroup.get(), actualGroup.get());
    }

    @Test
    void getGroupByName_shouldReturnedEmptyOptional_whenGroupDaoNotFindGroupWithGivenName() {
        String groupName = "NotExistent";
        Optional<Group> expectedGroup = Optional.empty();
        when(groupDaoMock.findGroupByName(groupName)).thenReturn(expectedGroup);

        Optional<Group> actualGroup = groupService.getGroupByName(groupName);

        verify(groupDaoMock, times(1)).findGroupByName(groupName);
        assertFalse(actualGroup.isPresent());
    }

    @Test
    void getGroupByName_shouldReturnedEmptyOptional_whenGroupDaoNotFindGroupWithGivenNameAndNameIsNull() {
        String groupName = null;
        Optional<Group> expectedGroup = Optional.empty();
        when(groupDaoMock.findGroupByName(groupName)).thenReturn(expectedGroup);

        Optional<Group> actualGroup = groupService.getGroupByName(groupName);

        verify(groupDaoMock, times(1)).findGroupByName(groupName);
        assertFalse(actualGroup.isPresent());
    }

    @Test
    void getAllGroups_shouldGroupsList_whenGroupDaoReturnsNotEmptyGroupsList() {
        List<Group> expectedGroups = new ArrayList<Group>();
        expectedGroups.add(new Group());
        expectedGroups.add(new Group());
        when(groupDaoMock.findAll()).thenReturn(expectedGroups);

        List<Group> actualGroups = groupService.getAllGroups();

        verify(groupDaoMock, times(1)).findAll();
        assertEquals(expectedGroups, actualGroups);
    }

    @Test
    void getAllGroups_shouldEmptyGroupsList_whenGroupDaoReturnsEmptyGroupsList() {
        List<Group> expectedGroups = new ArrayList<Group>();
        when(groupDaoMock.findAll()).thenReturn(expectedGroups);

        List<Group> actualGroups = groupService.getAllGroups();

        verify(groupDaoMock, times(1)).findAll();
        assertTrue(actualGroups.isEmpty());
        assertEquals(expectedGroups, actualGroups);
    }

    @Test
    void updateGroup_shouldReturnedUpdatedGroup_whenGroupDaoReturnUpdatedGroup() {
        Group uapdatedGroup = new Group("AS-84");
        uapdatedGroup.setId(1);
        when(groupDaoMock.update(uapdatedGroup)).thenReturn(uapdatedGroup);

        Optional<Group> actualUpdatedGroup = groupService.updateGroup(uapdatedGroup);

        verify(groupDaoMock, times(1)).update(uapdatedGroup);
        assertTrue(actualUpdatedGroup.isPresent());
        assertEquals(uapdatedGroup, actualUpdatedGroup.get());
    }

    @Test
    void updateGroup_shouldIllegalArgumentException_whenUpdatedGroupIsNullAndGroupDaoThrowIllegalArgumentException() {
        Group uapdatedGroup = null;
        when(groupDaoMock.update(uapdatedGroup)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> groupService.updateGroup(uapdatedGroup));
        verify(groupDaoMock, times(1)).update(uapdatedGroup);
    }

    @Test
    void deleteGroupByName_shouldDeletingGroup_whenGroupWasFoundSuccessfullyByThisName() {
        Integer groupId = 1;
        String groupName = "AS-84";
        Group groupToDelete = new Group(groupName);
        groupToDelete.setId(groupId);
        when(groupService.getGroupByName(groupName)).thenReturn(Optional.of(groupToDelete));

        groupService.deleteGroupByName(groupName);

        verify(groupDaoMock, times(1)).delete(groupId);
    }

    @Test
    void deleteGroupByName_shouldNothingDeleted_whenGroupNotFoundByThisName() {
        Integer groupId = 1;
        String groupName = null;
        when(groupService.getGroupByName(groupName)).thenReturn(Optional.empty());

        groupService.deleteGroupByName(groupName);

        verify(groupDaoMock, never()).delete(groupId);
    }

    @Test
    void getGroupsWithGivenNumberOfStudents_shouldGroupsListInWhichNumberOfStudentsLessThanAndEqualToGivenNumber_whenGroupsWithGivenNumberOfStudentsAreExist() {
        Integer numberOfStudents = 2;
        Group firstGroup = new Group("GD-43");
        firstGroup.addStudent(new Student("FirstName_1", "LastName_1", firstGroup));
        firstGroup.addStudent(new Student("FirstName_2", "LastName_2", firstGroup));
        Group secondGroup = new Group("JF-96");
        secondGroup.addStudent(new Student("FirstName_3", "LastName_3", secondGroup));
        secondGroup.addStudent(new Student("FirstName_4", "LastName_4", secondGroup));
        secondGroup.addStudent(new Student("FirstName_5", "LastName_5", secondGroup));
        Group thirdGroup = new Group("BV-83");
        List<Group> allGroups = new ArrayList<Group>();
        allGroups.add(firstGroup);
        allGroups.add(secondGroup);
        allGroups.add(thirdGroup);
        when(groupDaoMock.findAll()).thenReturn(allGroups);

        List<Group> groupsThatHaveGivenNumberOfStudents = groupService
                .getGroupsWithGivenNumberOfStudents(numberOfStudents);

        assertTrue(groupsThatHaveGivenNumberOfStudents.contains(firstGroup));
        assertTrue(groupsThatHaveGivenNumberOfStudents.contains(thirdGroup));
    }

    @Test
    void getGroupsWithGivenNumberOfStudents_shouldGroupsListInWhichNumberOfStudentsLessThanAndEqualToGivenNumber_whenGivenNumberMuchLargerThanNumberOfStudentsInGroups() {
        Integer numberOfStudents = 200;
        Group firstGroup = new Group("GD-43");
        firstGroup.addStudent(new Student("FirstName_1", "LastName_1", firstGroup));
        firstGroup.addStudent(new Student("FirstName_2", "LastName_2", firstGroup));
        Group secondGroup = new Group("JF-96");
        secondGroup.addStudent(new Student("FirstName_3", "LastName_3", secondGroup));
        secondGroup.addStudent(new Student("FirstName_4", "LastName_4", secondGroup));
        secondGroup.addStudent(new Student("FirstName_5", "LastName_5", secondGroup));
        Group thirdGroup = new Group("BV-83");
        List<Group> allGroups = new ArrayList<Group>();
        allGroups.add(firstGroup);
        allGroups.add(secondGroup);
        allGroups.add(thirdGroup);
        when(groupDaoMock.findAll()).thenReturn(allGroups);

        List<Group> groupsThatHaveGivenNumberOfStudents = groupService
                .getGroupsWithGivenNumberOfStudents(numberOfStudents);

        assertTrue(groupsThatHaveGivenNumberOfStudents.contains(firstGroup));
        assertTrue(groupsThatHaveGivenNumberOfStudents.contains(secondGroup));
        assertTrue(groupsThatHaveGivenNumberOfStudents.contains(thirdGroup));
    }

    @Test
    void getGroupsWithGivenNumberOfStudents_shouldGroupWhichNotHaveStudents_whenGivenNumberIsZero() {
        Integer numberOfStudents = 0;
        Group firstGroup = new Group("GD-43");
        firstGroup.addStudent(new Student("FirstName_1", "LastName_1", firstGroup));
        firstGroup.addStudent(new Student("FirstName_2", "LastName_2", firstGroup));
        Group secondGroup = new Group("JF-96");
        secondGroup.addStudent(new Student("FirstName_3", "LastName_3", secondGroup));
        secondGroup.addStudent(new Student("FirstName_4", "LastName_4", secondGroup));
        secondGroup.addStudent(new Student("FirstName_5", "LastName_5", secondGroup));
        Group thirdGroup = new Group("BV-83");
        List<Group> allGroups = new ArrayList<Group>();
        allGroups.add(firstGroup);
        allGroups.add(secondGroup);
        allGroups.add(thirdGroup);
        when(groupDaoMock.findAll()).thenReturn(allGroups);

        List<Group> groupsThatHaveGivenNumberOfStudents = groupService
                .getGroupsWithGivenNumberOfStudents(numberOfStudents);

        assertTrue(groupsThatHaveGivenNumberOfStudents.contains(thirdGroup));
    }

    @Test
    void getGroupsWithGivenNumberOfStudents_shouldEmptyGroupsList_whenGivenNumberLessThanZero() {
        Integer numberOfStudents = -2;
        Group firstGroup = new Group("GD-43");
        firstGroup.addStudent(new Student("FirstName_1", "LastName_1", firstGroup));
        firstGroup.addStudent(new Student("FirstName_2", "LastName_2", firstGroup));
        Group secondGroup = new Group("JF-96");
        secondGroup.addStudent(new Student("FirstName_3", "LastName_3", secondGroup));
        secondGroup.addStudent(new Student("FirstName_4", "LastName_4", secondGroup));
        secondGroup.addStudent(new Student("FirstName_5", "LastName_5", secondGroup));
        Group thirdGroup = new Group("BV-83");
        List<Group> allGroups = new ArrayList<Group>();
        allGroups.add(firstGroup);
        allGroups.add(secondGroup);
        allGroups.add(thirdGroup);
        when(groupDaoMock.findAll()).thenReturn(allGroups);

        List<Group> groupsThatHaveGivenNumberOfStudents = groupService
                .getGroupsWithGivenNumberOfStudents(numberOfStudents);

        assertTrue(groupsThatHaveGivenNumberOfStudents.isEmpty());
    }

    @Test
    void getGroupsWithGivenNumberOfStudents_shouldNullPointerException_whenGivenNumberIsNull() {
        Integer numberOfStudents = null;
        Group firstGroup = new Group("GD-43");
        firstGroup.addStudent(new Student("FirstName_1", "LastName_1", firstGroup));
        firstGroup.addStudent(new Student("FirstName_2", "LastName_2", firstGroup));
        Group secondGroup = new Group("JF-96");
        secondGroup.addStudent(new Student("FirstName_3", "LastName_3", secondGroup));
        secondGroup.addStudent(new Student("FirstName_4", "LastName_4", secondGroup));
        secondGroup.addStudent(new Student("FirstName_5", "LastName_5", secondGroup));
        Group thirdGroup = new Group("BV-83");
        List<Group> allGroups = new ArrayList<Group>();
        allGroups.add(firstGroup);
        allGroups.add(secondGroup);
        allGroups.add(thirdGroup);
        when(groupDaoMock.findAll()).thenReturn(allGroups);

        assertThrows(NullPointerException.class,
                () -> groupService.getGroupsWithGivenNumberOfStudents(numberOfStudents));
    }

}
