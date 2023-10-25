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
import ua.foxminded.schoolapp.dto.GroupDto;
import ua.foxminded.schoolapp.dto.mapper.GroupMapper;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.repository.GroupRepository;
import ua.foxminded.schoolapp.service.generate.Generatable;

@SpringBootTest(classes = { GroupServiceImpl.class })
class GroupServiceImplTest {

    @MockBean
    private Generatable<GroupDto> groupsGeneratorMock;

    @MockBean
    private GroupRepository groupRepositoryMock;

    @Autowired
    private GroupServiceImpl groupService;

    @Test
    void initGroups_shouldSavedAllGroupsWhichReturnsGroupsGenerator_whenGroupsGeneratorReturnsSeveralGroups() {
        List<GroupDto> generatedGroups = new ArrayList<>();
        generatedGroups.add(new GroupDto());
        generatedGroups.add(new GroupDto());
        generatedGroups.add(new GroupDto());
        when(groupsGeneratorMock.toGenerate()).thenReturn(generatedGroups);

        groupService.initGroups();

        verify(groupsGeneratorMock, times(1)).toGenerate();
        verify(groupRepositoryMock, times(3)).save(any(Group.class));
    }

    @Test
    void initGroups_shouldNotSaveAnyGroup_whenGroupsGeneratorReturnsEmptyGroupsList() {
        List<GroupDto> generatedGroups = new ArrayList<>();
        when(groupsGeneratorMock.toGenerate()).thenReturn(generatedGroups);

        groupService.initGroups();

        verify(groupsGeneratorMock, times(1)).toGenerate();
        verify(groupRepositoryMock, never()).save(any(Group.class));
    }

    @Test
    void addGroup_shouldIllegalArgumentException_whenNewGroupDtoIsNull() {
        GroupDto newGroupDto = null;

        assertThrows(IllegalArgumentException.class, () -> groupService.addGroup(newGroupDto));
    }

    @Test
    void addGroup_shouldAddedAndReturnedNewGroupOptional_whenGroupRepositorySuccessfulSaveNewGroup() {
        String newGroupName = "LK-33";
        GroupDto newGroupDto = new GroupDto(newGroupName);
        Group newGroup = GroupMapper.mapDtoToGroup(newGroupDto);
        when(groupRepositoryMock.save(newGroup)).thenReturn(newGroup);

        Optional<Group> newReturnedGroup = groupService.addGroup(newGroupDto);

        verify(groupRepositoryMock, times(1)).save(newGroup);
        assertTrue(newReturnedGroup.isPresent());
        assertEquals(newGroup, newReturnedGroup.get());
    }

    @Test
    void getGroupByName_shouldReturnedGroupOptional_whenGroupRepositoryFoundGroupByName() {
        String groupName = "LK-33";
        Group expectedGroup = new Group(groupName);
        when(groupRepositoryMock.findByGroupName(groupName)).thenReturn(Optional.of(expectedGroup));

        Optional<Group> actualGroup = groupService.getGroupByName(groupName);

        verify(groupRepositoryMock, times(1)).findByGroupName(groupName);
        assertTrue(actualGroup.isPresent());
        assertEquals(expectedGroup, actualGroup.get());
    }

    @Test
    void getGroupByName_shouldReturnedEmptyOptional_whenGroupRepositoryNotFindGroupWithGivenName() {
        String groupName = "NotExistent";
        when(groupRepositoryMock.findByGroupName(groupName)).thenReturn(Optional.empty());

        Optional<Group> actualGroup = groupService.getGroupByName(groupName);

        verify(groupRepositoryMock, times(1)).findByGroupName(groupName);
        assertFalse(actualGroup.isPresent());
    }

    @Test
    void getGroupByName_shouldReturnedEmptyOptional_whenGroupRepositoryNotFindGroupWithGivenNameAndNameIsNull() {
        String groupName = null;
        when(groupRepositoryMock.findByGroupName(groupName)).thenReturn(Optional.empty());

        Optional<Group> actualGroup = groupService.getGroupByName(groupName);

        verify(groupRepositoryMock, times(1)).findByGroupName(groupName);
        assertFalse(actualGroup.isPresent());
    }

    @Test
    void getAllGroups_shouldGroupsList_whenGroupRepositoryReturnsNotEmptyGroupsList() {
        List<Group> expectedGroups = new ArrayList<Group>();
        expectedGroups.add(new Group());
        expectedGroups.add(new Group());
        when(groupRepositoryMock.findAll()).thenReturn(expectedGroups);

        List<Group> actualGroups = groupService.getAllGroups();

        verify(groupRepositoryMock, times(1)).findAll();
        assertEquals(expectedGroups, actualGroups);
    }

    @Test
    void getAllGroups_shouldEmptyGroupsList_whenGroupRepositoryReturnsEmptyGroupsList() {
        when(groupRepositoryMock.findAll()).thenReturn(new ArrayList<Group>());

        List<Group> actualGroups = groupService.getAllGroups();

        verify(groupRepositoryMock, times(1)).findAll();
        assertTrue(actualGroups.isEmpty());
    }

    @Test
    void updateGroup_shouldReturnedUpdatedGroup_whenGroupRepositoryReturnUpdatedGroup() {
        Group uapdatedGroup = new Group("AS-84");
        uapdatedGroup.setId(1);
        when(groupRepositoryMock.save(uapdatedGroup)).thenReturn(uapdatedGroup);

        Optional<Group> actualUpdatedGroup = groupService.updateGroup(uapdatedGroup);

        verify(groupRepositoryMock, times(1)).save(uapdatedGroup);
        assertTrue(actualUpdatedGroup.isPresent());
        assertEquals(uapdatedGroup, actualUpdatedGroup.get());
    }

    @Test
    void deleteGroupByName_shouldDeletedGroup_whenGroupWithGivenNameIsExist() {
        Integer groupId = 1;
        String groupName = "AS-84";
        Group groupToDelete = new Group(groupName);
        groupToDelete.setId(groupId);
        when(groupService.getGroupByName(groupName)).thenReturn(Optional.of(groupToDelete));

        groupService.deleteGroupByName(groupName);

        verify(groupRepositoryMock, times(1)).delete(groupToDelete);
    }

    @Test
    void deleteGroupByName_shouldNothingDeleted_whenNoGroupWithGivenName() {
        String groupName = null;
        when(groupService.getGroupByName(groupName)).thenReturn(Optional.empty());

        groupService.deleteGroupByName(groupName);

        verify(groupRepositoryMock, never()).delete(any(Group.class));
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
        when(groupRepositoryMock.findAll()).thenReturn(allGroups);

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
        when(groupRepositoryMock.findAll()).thenReturn(allGroups);

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
        when(groupRepositoryMock.findAll()).thenReturn(allGroups);

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
        when(groupRepositoryMock.findAll()).thenReturn(allGroups);

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
        when(groupRepositoryMock.findAll()).thenReturn(allGroups);

        assertThrows(NullPointerException.class,
                () -> groupService.getGroupsWithGivenNumberOfStudents(numberOfStudents));
    }

}
