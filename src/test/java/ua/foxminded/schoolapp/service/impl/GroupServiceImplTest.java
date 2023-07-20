package ua.foxminded.schoolapp.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolapp.dao.GroupDao;
import ua.foxminded.schoolapp.datasetup.Generatable;
import ua.foxminded.schoolapp.exception.DaoException;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.service.GroupService;

class GroupServiceImplTest {

    Generatable<Group> groupsGeneratorMock;
    GroupDao groupDaoMock;
    GroupService groupService;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        groupsGeneratorMock = mock(Generatable.class);
        groupDaoMock = mock(GroupDao.class);
    }

    @Test
    void GroupServiceImpl_shouldNullPointerException_whenGroupsGeneratorIsNull() {
        assertThrows(NullPointerException.class, () -> new GroupServiceImpl(null, groupDaoMock));
    }

    @Test
    void GroupServiceImpl_shouldNullPointerException_whenGroupDaoIsNull() {
        assertThrows(NullPointerException.class, () -> new GroupServiceImpl(groupsGeneratorMock, null));
    }

    @Test
    void initGroups_shouldDaoException_whenGroupDaoThrowDaoException() {
        groupService = new GroupServiceImpl(groupsGeneratorMock, groupDaoMock);
        List<Group> generatedGroups = new ArrayList<>();
        Group group = new Group("GH-37");
        group.setId(1);
        generatedGroups.add(group);
        when(groupsGeneratorMock.toGenerate()).thenReturn(generatedGroups);
        when(groupDaoMock.save(group)).thenThrow(DaoException.class);

        assertThrows(DaoException.class, () -> groupService.initGroups());
        verify(groupsGeneratorMock, times(1)).toGenerate();
        verify(groupDaoMock, times(1)).save(group);
    }

    @Test
    void initGroups_shouldSavedAllGroupsWhichReturnsGroupsGenerator_whenGroupsGeneratorReturnsSeveralGroups() {
        groupService = new GroupServiceImpl(groupsGeneratorMock, groupDaoMock);
        List<Group> generatedGroups = new ArrayList<>();
        Group firstGroup = new Group("GH-37");
        Group secondGroup = new Group("HS-42");
        Group thirdGroup = new Group("AG-13");
        firstGroup.setId(1);
        secondGroup.setId(2);
        thirdGroup.setId(3);
        generatedGroups.add(firstGroup);
        generatedGroups.add(secondGroup);
        generatedGroups.add(thirdGroup);
        when(groupsGeneratorMock.toGenerate()).thenReturn(generatedGroups);

        groupService.initGroups();

        verify(groupsGeneratorMock, times(1)).toGenerate();
        verify(groupDaoMock, times(1)).save(firstGroup);
        verify(groupDaoMock, times(1)).save(secondGroup);
        verify(groupDaoMock, times(1)).save(thirdGroup);
    }

    @Test
    void initGroups_shouldNotSaveAnyGroup_whenGroupsGeneratorReturnsEmptyGroupsList() {
        groupService = new GroupServiceImpl(groupsGeneratorMock, groupDaoMock);
        List<Group> generatedGroups = new ArrayList<>();
        when(groupsGeneratorMock.toGenerate()).thenReturn(generatedGroups);

        groupService.initGroups();

        verify(groupsGeneratorMock, times(1)).toGenerate();
        verify(groupDaoMock, never()).save(any(Group.class));
    }

    @Test
    void getGroupsWithGivenNumberOfStudents_shouldDaoException_whenGroupDaoThrowDaoException() {
        groupService = new GroupServiceImpl(groupsGeneratorMock, groupDaoMock);
        int studentsNumber = 11;
        when(groupDaoMock.findGroupsWithGivenNumberStudents(studentsNumber)).thenThrow(DaoException.class);

        assertThrows(DaoException.class, () -> groupService.getGroupsWithGivenNumberOfStudents(studentsNumber));
        verify(groupDaoMock, times(1)).findGroupsWithGivenNumberStudents(studentsNumber);
    }

    @Test
    void getGroupsWithGivenNumberOfStudents_shouldBeReturnedValueThatReturnGroupDao_whenWePassedCorrectGroupDaoInstance() {
        groupService = new GroupServiceImpl(groupsGeneratorMock, groupDaoMock);
        int studentsNumber = 11;
        Map<Group, Integer> expectedGroupWithTheirNumberOfStudents = new HashMap<>();
        expectedGroupWithTheirNumberOfStudents.put(new Group("TO-72"), studentsNumber);
        when(groupDaoMock.findGroupsWithGivenNumberStudents(studentsNumber))
                .thenReturn(expectedGroupWithTheirNumberOfStudents);

        Map<Group, Integer> actualGroupWithTheirNumberOfStudents = groupService
                .getGroupsWithGivenNumberOfStudents(studentsNumber);

        assertEquals(expectedGroupWithTheirNumberOfStudents, actualGroupWithTheirNumberOfStudents);
        verify(groupDaoMock, times(1)).findGroupsWithGivenNumberStudents(studentsNumber);
    }

    @Test
    void getGroupsWithGivenNumberOfStudents_shouldEmptyMap_whenGroupReturnEmptyMap() {
        groupService = new GroupServiceImpl(groupsGeneratorMock, groupDaoMock);
        int studentsNumber = 11;
        Map<Group, Integer> expectedGroupWithTheirNumberOfStudents = new HashMap<>();
        when(groupDaoMock.findGroupsWithGivenNumberStudents(studentsNumber))
                .thenReturn(expectedGroupWithTheirNumberOfStudents);

        Map<Group, Integer> actualGroupWithTheirNumberOfStudents = groupService
                .getGroupsWithGivenNumberOfStudents(studentsNumber);

        assertEquals(expectedGroupWithTheirNumberOfStudents, actualGroupWithTheirNumberOfStudents);
        verify(groupDaoMock, times(1)).findGroupsWithGivenNumberStudents(studentsNumber);
    }

}
