package ua.foxminded.schoolapp.service.logic.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import ua.foxminded.schoolapp.dao.GroupDao;
import ua.foxminded.schoolapp.dto.GroupDto;
import ua.foxminded.schoolapp.dto.mapper.GroupMapper;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.service.generate.Generatable;
import ua.foxminded.schoolapp.service.logic.GroupService;

/**
 * The GroupServiceImpl class is an implementation of the {@link GroupService}
 * interface. It provides operations for managing groups, including initializing
 * groups with a generator, and retrieving groups with a given number of
 * students along with the corresponding student count.
 * <p>
 * This class is annotated with {@code @Service} to indicate that it is a Spring
 * service, and it can be automatically discovered and registered as a bean in
 * the Spring context. The GroupServiceImpl requires a
 * {@link Generatable<GroupDto>} object to generate groups Dto and a
 * {@link GroupDao} object to access the group data.
 *
 * @author Serhii Bohdan
 */
@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    /**
     * The logger for logging events and messages in the {@link GroupServiceImpl}
     * class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final Generatable<GroupDto> groupsGenerator;
    private final GroupDao groupDao;

    /**
     * Constructs a new GroupServiceImpl with the specified groups generator and
     * group Dao.
     *
     * @param groupsGenerator the generator for creating groups
     * @param groupDao        the data access object for groups
     */
    public GroupServiceImpl(Generatable<GroupDto> groupsGenerator, GroupDao groupDao) {
        this.groupsGenerator = groupsGenerator;
        this.groupDao = groupDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initGroups() {
        LOGGER.info("Filling with generated groups");
        groupsGenerator.toGenerate().stream()
                .map(GroupMapper::mapDtoToGroup)
                .forEach(groupDao::save);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Group> addGroup(String groupName) {
        Group newGroup = new Group(groupName);
        LOGGER.debug("Adding a new group: {}", newGroup);

        return Optional.ofNullable(groupDao.save(newGroup));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Group> getGroupByName(String groupName) {
        Optional<Group> group = groupDao.findGroupByName(groupName);
        LOGGER.debug("Search group by name {}: {}", groupName, group);

        return group;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> getAllGroups() {
        List<Group> allGroups = groupDao.findAll();
        LOGGER.debug("Search for all groups. All received groups: {}", allGroups);

        return allGroups;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Group> updateGroup(Group updatedGroup) {
        Group group = groupDao.update(updatedGroup);
        LOGGER.debug("Updating group data. Updated group: {}", group);

        return Optional.ofNullable(group);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteGroupByName(String groupName) {
        Optional<Group> group = getGroupByName(groupName);

        if (group.isPresent()) {
            LOGGER.debug("Deleting group: {}", group);
            groupDao.delete(group.get().getId());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> getGroupsWithGivenNumberOfStudents(Integer amountOfStudents) {
        List<Group> groupsWithGivenNumberOfStudents = groupDao.findAll().stream()
                .filter(group -> group.getStudents().size() <= amountOfStudents)
                .toList();

        LOGGER.debug("Obtained groups with the number of students {}: {}", amountOfStudents,
                groupsWithGivenNumberOfStudents);
        return groupsWithGivenNumberOfStudents;
    }

}
