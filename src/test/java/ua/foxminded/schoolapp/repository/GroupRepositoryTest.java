package ua.foxminded.schoolapp.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.schoolapp.TestApplicationConfig;
import ua.foxminded.schoolapp.model.Group;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = { GroupRepository.class }
))
@ContextConfiguration(classes = TestApplicationConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = { "/sql/clear_tables.sql", "/sql/groups_test_init.sql" }, 
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class GroupRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GroupRepository groupRepository;

    @Test
    void save_shouldInvalidDataAccessApiUsageException_whenGroupIsNull() {
        Group group = null;

        assertThrows(InvalidDataAccessApiUsageException.class, () -> groupRepository.save(group));
    }

    @Test
    void save_shouldDataIntegrityViolationException_whenGroupNameIsNull() {
        Group group = new Group(null);

        assertThrows(DataIntegrityViolationException.class, () -> groupRepository.save(group));
    }

    @Test
    void save_shouldDataIntegrityViolationException_whenGroupNameContainsMoreThanFiveCharacters() {
        String invalidGroupName = "LKJ-576";
        Group group = new Group(invalidGroupName);

        assertThrows(DataIntegrityViolationException.class, () -> groupRepository.save(group));
    }

    @Test
    void save_shouldSavedGroupInTestTable_whenGroupNameContainsLessThanFiveCharacters() {
        Group expectedGroup = new Group("AQ-");

        Group actualGroup = groupRepository.save(expectedGroup);

        assertEquals(expectedGroup.getGroupName(), actualGroup.getGroupName());
    }

    @Test
    void save_shouldSavedGroupInTestTable_whenGroupNameIsEmpty() {
        Group expectedGroup = new Group("");

        Group actualGroup = groupRepository.save(expectedGroup);

        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    void save_shouldSavedGroupInTestTable_whenGroupWithNameThatContainsFiveCharacters() {
        Group expectedGroup = new Group("GC-34");

        Group actualGroup = groupRepository.save(expectedGroup);

        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    void save_shouldUpdateGroupWithNewName_whenGroupAlreadyExistInTable() {
        Group groupToUpdate = entityManager.find(Group.class, 1);
        String newGroupName = "LF-64";
        groupToUpdate.setGroupName(newGroupName);

        Group actualUpdatedGroup = groupRepository.save(groupToUpdate);

        assertEquals(groupToUpdate, actualUpdatedGroup);
    }

    @Test
    void save_shouldUpdateGroupWithNewName_whenGroupAlreadyExistAndNewGroupNameIsNull() {
        Group groupToUpdate = entityManager.find(Group.class, 1);
        String newGroupName = null;
        groupToUpdate.setGroupName(newGroupName);

        Group actualUpdatedGroup = groupRepository.save(groupToUpdate);

        assertEquals(groupToUpdate, actualUpdatedGroup);
        assertNull(actualUpdatedGroup.getGroupName());
    }

    @Test
    void findByGroupName_shouldFindedGroupWithGivenName_whenGroupWithGivenNameExist() {
        String groupName = "FD-74";

        Group actualGroup = groupRepository.findByGroupName(groupName).get();

        assertEquals(groupName, actualGroup.getGroupName());
    }

    @Test
    void findByGroupName_shouldEmptyOptional_whenNoGroupWithGivenName() {
        String groupName = "OO-00";

        Optional<Group> actualGroup = groupRepository.findByGroupName(groupName);

        assertTrue(actualGroup.isEmpty());
    }

    @Test
    void findByGroupName_shouldEmptyOptional_whenGivenGroupNameIsNull() {
        String groupName = null;

        Optional<Group> actualGroup = groupRepository.findByGroupName(groupName);

        assertTrue(actualGroup.isEmpty());
    }

    @Test
    @Sql("/sql/clear_tables.sql")
    void findAll_shouldEmptyGroupsList_whenGroupsTableIsEmpty() {
        List<Group> allAvailableGroups = groupRepository.findAll();

        assertTrue(allAvailableGroups.isEmpty());
    }

    @Test
    void findAll_shouldAllAvailableGroups_whenGroupsTableContainGroups() {
        List<Group> allAvailableGroups = groupRepository.findAll();

        assertEquals(3, allAvailableGroups.size());
    }

    @Test
    void delete_shouldDeletedGroupWithGivenId_whenGroupWithGivenDataExist() {
        Group groupToDelete = new Group("LS-09");
        Integer groupId = 1;
        groupToDelete.setId(groupId);

        groupRepository.delete(groupToDelete);
        Group deletedGroup = entityManager.find(Group.class, groupId);

        assertNull(deletedGroup);
    }

    @Test
    void delete_shouldNothingDeleted_whenNoGroupWithGivenData() {
        Group groupToDelete = new Group("LS-09");
        Integer groupIdThatNoExist = 5;
        groupToDelete.setId(groupIdThatNoExist);

        groupRepository.delete(groupToDelete);

        assertNull(entityManager.find(Group.class, groupIdThatNoExist));
    }

    @Test
    void delete_shouldInvalidDataAccessApiUsageException_whenGivenGroupIsNull() {
        Group group = null;

        assertThrows(InvalidDataAccessApiUsageException.class, () -> groupRepository.delete(group));
    }

}
