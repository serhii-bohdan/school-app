package ua.foxminded.schoolapp.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.schoolapp.TestApplicationConfig;
import ua.foxminded.schoolapp.dao.GroupDao;
import ua.foxminded.schoolapp.model.Group;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = { JpaGroupDao.class }
))
@ContextConfiguration(classes = TestApplicationConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = { "/sql/clear_tables.sql", "/sql/groups_test_init.sql" }, 
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class JpaGroupDaoTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    GroupDao groupDao;

    @Test
    void save_shouldIllegalArgumentException_whenGroupIsNull() {
        Group group = null;

        assertThrows(IllegalArgumentException.class, () -> groupDao.save(group));
    }

    @Test
    void save_shouldConstraintViolationException_whenGroupNameNotInitialized() {
        Group group = new Group();

        assertThrows(ConstraintViolationException.class, () -> groupDao.save(group));
    }

    @Test
    void save_shouldDataException_whenGroupNameContainsMoreThanFiveCharacters() {
        String invalidGroupName = "LKJ-576";
        Group group = new Group(invalidGroupName);

        assertThrows(DataException.class, () -> groupDao.save(group));
    }

    @Test
    void save_shouldSavedGroupInTestTable_whenGroupNameContainsLessThanFiveCharacters() {
        Group expectedGroup = new Group("AQ-");

        Group actualGroup = groupDao.save(expectedGroup);

        assertEquals(expectedGroup.getGroupName(), actualGroup.getGroupName());
    }

    @Test
    void save_shouldSavedGroupInTestTable_whenGroupNameIsEmpty() {
        Group expectedGroup = new Group("");

        Group actualGroup = groupDao.save(expectedGroup);

        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    void save_shouldSavedGroupInTestTable_whenGroupWithNameThatContainsFiveCharacters() {
        Group expectedGroup = new Group("GC-34");

        Group actualGroup = groupDao.save(expectedGroup);

        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    void find_shouldGroupThatExistInGroupsTable_whenGroupWithGivenIdExist() {
        Group expectedGroup = new Group();
        Integer expectedId = 1;
        expectedGroup.setId(expectedId);
        expectedGroup.setGroupName("FD-74");

        Group actualGroup = groupDao.find(expectedId);

        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    void find_shouldNull_whenNoGroupWithGivenId() {
        Group actualGroup = groupDao.find(4);

        assertNull(actualGroup);
    }

    @Test
    void find_shouldIllegalArgumentException_whenGivenGroupIdIsNull() {
        Integer groupId = null;

        assertThrows(IllegalArgumentException.class, () -> groupDao.find(groupId));
    }

    @Test
    void findGroupByName_shouldFindedGroupWithGivenName_whenGroupWithGivenNameExist() {
        String groupName = "FD-74";

        Group actualGroup = groupDao.findGroupByName(groupName).get();

        assertEquals(groupName, actualGroup.getGroupName());
    }

    @Test
    void findGroupByName_shouldEmptyOptional_whenNoGroupWithGivenName() {
        String groupName = "OO-00";

        Optional<Group> actualGroup = groupDao.findGroupByName(groupName);

        assertTrue(actualGroup.isEmpty());
    }

    @Test
    void findGroupByName_shouldEmptyOptional_whenGivenGroupNameIsNull() {
        String groupName = null;

        Optional<Group> actualGroup = groupDao.findGroupByName(groupName);

        assertTrue(actualGroup.isEmpty());
    }

    @Test
    @Sql("/sql/clear_tables.sql")
    void findAll_shouldEmptyGroupsList_whenGroupsTableIsEmpty() {
        List<Group> allAvailableGroups = groupDao.findAll();

        assertTrue(allAvailableGroups.isEmpty());
    }

    @Test
    void findAll_shouldAllAvailableGroups_whenGroupsTableContainGroups() {
        List<Group> allAvailableGroups = groupDao.findAll();

        assertEquals(3, allAvailableGroups.size());
    }

    @Test
    void update_shouldUpdatedGroupName_whenGroupWithGivenIdExist() {
        String newGroupName = "LF-64";
        Group newExpectedGroup = new Group();
        newExpectedGroup.setId(1);
        newExpectedGroup.setGroupName(newGroupName);

        Group actualUpdatedGroup = groupDao.update(newExpectedGroup);

        assertEquals(newExpectedGroup, actualUpdatedGroup);
    }

    @Test
    void update_shouldUpdatedGroupName_whenGroupWithGivenIdExistAndNewGroupNameIsNull() {
        String newGroupName = null;
        Group newExpectedGroup = new Group();
        newExpectedGroup.setId(1);
        newExpectedGroup.setGroupName(newGroupName);

        Group actualUpdatedGroup = groupDao.update(newExpectedGroup);

        assertEquals(newExpectedGroup, actualUpdatedGroup);
    }

    @Test
    void update_shouldSavedNewGroupWithAnotherId_whenNoGroupWithGivenId() {
        String newGroupName = "LF-64";
        Group newExpectedGroup = new Group();
        newExpectedGroup.setId(5);
        newExpectedGroup.setGroupName(newGroupName);

        Group actualUpdatedGroup = groupDao.update(newExpectedGroup);

        assertEquals(4, actualUpdatedGroup.getId());
        assertEquals(newExpectedGroup, actualUpdatedGroup);
    }

    @Test
    void update_shouldIllegalArgumentException_whenGivenGroupIsNull() {
        Group newExpectedGroup = null;

        assertThrows(IllegalArgumentException.class, () -> groupDao.update(newExpectedGroup));
    }

    @Test
    void delete_shouldDeletedGroupWithGivenId_whenGroupWithGivenIdExist() {
        Integer groupIdToDelete = 1;

        groupDao.delete(groupIdToDelete);
        Group deletedGroup = entityManager.find(Group.class, groupIdToDelete);

        assertNull(deletedGroup);
    }

    @Test
    void delete_shouldNothingDeleted_whenNoGroupWithGivenId() {
        Integer groupIdThatNoExist = 4;

        groupDao.delete(groupIdThatNoExist);

        assertNull(entityManager.find(Group.class, groupIdThatNoExist));
    }

    @Test
    void delete_shouldIllegalArgumentException_whenGivenGroupIdIsNull() {
        Integer groupId = null;

        assertThrows(IllegalArgumentException.class, () -> groupDao.delete(groupId));
    }

}
