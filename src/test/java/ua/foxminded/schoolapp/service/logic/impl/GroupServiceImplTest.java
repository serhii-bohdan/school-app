package ua.foxminded.schoolapp.service.logic.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.schoolapp.dao.GroupDao;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.service.generate.Generatable;

@SpringBootTest(classes = { GroupServiceImpl.class })
class GroupServiceImplTest {

    @MockBean
    Generatable<Group> groupsGeneratorMock;

    @MockBean
    GroupDao groupDaoMock;

    @Autowired
    GroupServiceImpl groupService;

    @Test
    void initGroups_shouldSavedAllGroupsWhichReturnsGroupsGenerator_whenGroupsGeneratorReturnsSeveralGroups() {
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
        List<Group> generatedGroups = new ArrayList<>();
        when(groupsGeneratorMock.toGenerate()).thenReturn(generatedGroups);

        groupService.initGroups();

        verify(groupsGeneratorMock, times(1)).toGenerate();
        verify(groupDaoMock, never()).save(any(Group.class));
    }

    @Test
    void getGroupsWithGivenNumberOfStudents_shouldBeReturnedMapWithTwoGroupsAndTheirNumberOfStudents_whenGroupDaoReturnListWithTwoGroups() {
        Group firstGroup = new Group("TO-72");
        Group secondGroup = new Group("JD-20");
        List<Group> groupsWithGivenNumberOfStudents = new ArrayList<Group>();
        groupsWithGivenNumberOfStudents.add(firstGroup);
        groupsWithGivenNumberOfStudents.add(secondGroup);
        int studentsNumberForFirstGroup = 3;
        int studentsNumberForSecondGroup = 2;
        Map<Group, Integer> expectedGroupWithTheirNumberOfStudents = new HashMap<>();
        expectedGroupWithTheirNumberOfStudents.put(firstGroup, studentsNumberForFirstGroup);
        expectedGroupWithTheirNumberOfStudents.put(secondGroup, studentsNumberForSecondGroup);
        when(groupDaoMock.findGroupsWithGivenNumberStudents(studentsNumberForFirstGroup))
                .thenReturn(groupsWithGivenNumberOfStudents);
        when(groupDaoMock.findNumberOfStudentsForGroup(firstGroup)).thenReturn(studentsNumberForFirstGroup);
        when(groupDaoMock.findNumberOfStudentsForGroup(secondGroup)).thenReturn(studentsNumberForSecondGroup);

        Map<Group, Integer> actualGroupWithTheirNumberOfStudents = groupService
                .getGroupsWithGivenNumberOfStudents(studentsNumberForFirstGroup);

        assertEquals(expectedGroupWithTheirNumberOfStudents, actualGroupWithTheirNumberOfStudents);
        verify(groupDaoMock, times(1)).findGroupsWithGivenNumberStudents(studentsNumberForFirstGroup);
        verify(groupDaoMock, times(1)).findNumberOfStudentsForGroup(firstGroup);
        verify(groupDaoMock, times(1)).findNumberOfStudentsForGroup(secondGroup);
    }

    @Test
    void getGroupsWithGivenNumberOfStudents_shouldEmptyMap_whenGroupDaoReturnEmptyGroupsList() {
        int studentsNumber = 2;
        List<Group> emptyGroupsList = new ArrayList<>();
        Map<Group, Integer> expectedGroupWithTheirNumberOfStudents = new HashMap<>();
        when(groupDaoMock.findGroupsWithGivenNumberStudents(studentsNumber)).thenReturn(emptyGroupsList);

        Map<Group, Integer> actualGroupWithTheirNumberOfStudents = groupService
                .getGroupsWithGivenNumberOfStudents(studentsNumber);

        assertEquals(expectedGroupWithTheirNumberOfStudents, actualGroupWithTheirNumberOfStudents);
        verify(groupDaoMock, times(1)).findGroupsWithGivenNumberStudents(studentsNumber);
    }

}
