package ua.foxminded.schoolapp.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.schoolapp.TestAppConfig;
import ua.foxminded.schoolapp.dao.GroupDao;
import ua.foxminded.schoolapp.dao.mapper.GroupRowMapper;
import ua.foxminded.schoolapp.model.Group;

@JdbcTest
@ContextConfiguration(classes = TestAppConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql( 
        scripts = { "/sql/clear_tables.sql", "/sql/groups_test_init.sql" }, 
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class GroupDaoImplTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    GroupDao groupDao;
    final RowMapper<Group> groupRowMapper = new GroupRowMapper();

    @BeforeEach
    void setUp() {
        groupDao = new GroupDaoImpl(jdbcTemplate);
    }

    @Test
    void save_shouldNullPointerException_whenGroupIsNull() {
        assertThrows(NullPointerException.class, () -> groupDao.save(null));
    }

    @Test
    void save_shouldDataIntegrityViolationException_whenGroupNameNotInitialized() {
        Group group = new Group();

        assertThrows(DataIntegrityViolationException.class, () -> groupDao.save(group));
    }

    @Test
    void save_shouldDataIntegrityViolationException_whenGroupNameContainsMoreThanFiveCharacters() {
        String invalidGroupName = "LKJ-576";
        Group group = new Group(invalidGroupName);

        assertThrows(DataIntegrityViolationException.class, () -> groupDao.save(group));
    }

    @Test
    void save_shouldSavedGroupInTestTable_whenGroupNameContainsLessThanFiveCharacters() {
        Group expectedGroup = new Group();
        expectedGroup.setId(4);
        expectedGroup.setGroupName("AQ-");
        String selectTestDataScript = """
                SELECT * FROM groups
                WHERE group_name = 'AQ-';
                """;

        groupDao.save(expectedGroup);
        Group actualGroup = jdbcTemplate.queryForObject(selectTestDataScript, groupRowMapper);

        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    void save_shouldSavedGroupInTestTable_whenGroupNameEmpty() {
        Group expectedGroup = new Group();
        expectedGroup.setId(4);
        expectedGroup.setGroupName("");
        String selectTestDataScript = """
                SELECT * FROM groups
                WHERE group_name = '';
                """;

        groupDao.save(expectedGroup);
        Group actualGroup = jdbcTemplate.queryForObject(selectTestDataScript, groupRowMapper);

        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    void save_shouldSavedGroupInTestTable_whenGroupWithNameThatContainsFiveCharacters() {
        Group expectedGroup = new Group();
        expectedGroup.setId(4);
        expectedGroup.setGroupName("GC-34");
        String selectTestDataScript = """
                SELECT * FROM groups
                WHERE group_name = 'GC-34';
                """;

        groupDao.save(expectedGroup);
        Group actualGroup = jdbcTemplate.queryForObject(selectTestDataScript, groupRowMapper);

        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    void find_shouldGroupThatExistInGroupsTable_whenGroupWithEnteredIdExist() {
        Group expectedGroup = new Group();
        expectedGroup.setId(1);
        expectedGroup.setGroupName("FD-74");

        Group actualGroup = groupDao.find(1);

        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    void find_shouldNull_whenNoGroupWithEnteredId() {
        Group actualGroup = groupDao.find(4);

        assertNull(actualGroup);
    }

    @Test
    @Sql("/sql/clear_tables.sql")
    void findAll_shouldEmptyGroupsList_whenGroupsTableEmpty() {
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
        String selectTestDataScript = """
                SELECT * FROM groups
                WHERE group_id = 1;
                """;

        int numberOfChanges = groupDao.update(newExpectedGroup);
        Group actualUpdatedGroup = jdbcTemplate.queryForObject(selectTestDataScript, groupRowMapper);

        assertEquals(1, numberOfChanges);
        assertEquals(newExpectedGroup, actualUpdatedGroup);
    }

    @Test
    void update_shouldNoAnyChanges_whenNoGroupWithGivenId() {
        String newGroupName = "LF-64";
        Group newExpectedGroup = new Group();
        newExpectedGroup.setId(5);
        newExpectedGroup.setGroupName(newGroupName);

        int numberOfChanges = groupDao.update(newExpectedGroup);

        assertEquals(0, numberOfChanges);
    }

    @Test
    void delete_shouldDeletedGroupWithGivenIdAndStudentsFromThisGroup_whenGroupWithGivenIdExist() {
        int groupId = 1;
        String selectTestDataScript = """
                SELECT * FROM groups
                WHERE group_id = 1;
                """;

        int numberOfChanges = groupDao.delete(groupId);

        assertEquals(4, numberOfChanges);
        assertThrows(EmptyResultDataAccessException.class,
                () -> jdbcTemplate.queryForObject(selectTestDataScript, groupRowMapper));
    }

    @Test
    void delete_shouldNothingDeleted_whenNoGroupWithGivenId() {
        int groupIdThatNoExist = 4;

        int numberOfChanges = groupDao.delete(groupIdThatNoExist);

        assertEquals(0, numberOfChanges);
    }

    @Test
    void findGroupsWithGivenNumberStudents_shouldEmptyGroupsList_whenNoGroupsWithGivenNumberOfStudents() {
        int studentsNumber = 1;
        List<Group> groups = groupDao.findGroupsWithGivenNumberStudents(studentsNumber);

        assertTrue(groups.isEmpty());
    }

    @Test
    void findGroupsWithGivenNumberStudents_shouldOneGroup_whenOneGroupWithGivenAndSmallerNumberOfStudents() {
        int studentsNumber = 3;
        Group expectedGroup = new Group();
        expectedGroup.setId(2);
        expectedGroup.setGroupName("KL-91");

        List<Group> groups = groupDao.findGroupsWithGivenNumberStudents(studentsNumber);

        assertEquals(expectedGroup, groups.get(0));
    }

    @Test
    void findGroupsWithGivenNumberStudents_shouldAllExistingGroups_whenGivenNumberOfStudentsIsMuchLargerThanWhatInEachOfAvailableGroups() {
        int studentsNumber = 100;

        List<Group> groups = groupDao.findGroupsWithGivenNumberStudents(studentsNumber);

        assertEquals("CZ-03", groups.get(0).getGroupName());
        assertEquals("FD-74", groups.get(1).getGroupName());
        assertEquals("KL-91", groups.get(2).getGroupName());
    }

    @Test
    void findNumberOfStudentsForGroup_shouldNullPointerException_whenGroupIsNull() {
        assertThrows(NullPointerException.class, () -> groupDao.findNumberOfStudentsForGroup(null));
    }

    @Test
    void findNumberOfStudentsForGroup_shouldMinusOne_whenGroupUninitialized() {
        Group group = new Group();

        int actualStudentsNumberInGroup = groupDao.findNumberOfStudentsForGroup(group);

        assertEquals(-1, actualStudentsNumberInGroup);
    }

    @Test
    void findNumberOfStudentsForGroup_shouldStudentsNumberInGroup_whenGroupWithGivenParametersExists() {
        Group group = new Group();
        group.setId(3);
        group.setGroupName("CZ-03");

        int actualStudentsNumberInGroup = groupDao.findNumberOfStudentsForGroup(group);

        assertEquals(5, actualStudentsNumberInGroup);
    }

    @Test
    void findNumberOfStudentsForGroup_shouldMinusOne_whenNoGroupWithGivenId() {
        Group group = new Group();
        group.setId(4);
        group.setGroupName("CZ-03");

        int actualStudentsNumberInGroup = groupDao.findNumberOfStudentsForGroup(group);

        assertEquals(-1, actualStudentsNumberInGroup);
    }

}
