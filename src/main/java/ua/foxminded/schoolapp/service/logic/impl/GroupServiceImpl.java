package ua.foxminded.schoolapp.service.logic.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import ua.foxminded.schoolapp.dto.GroupDto;
import ua.foxminded.schoolapp.dto.mapper.GroupMapper;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.repository.GroupRepository;
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
 * the Spring context. The GroupServiceImpl requires a {@link Generatable}
 * object to generate groups Dto and a {@link GroupRepository} object to access
 * the group data.
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
    private final GroupRepository groupRepository;

    /**
     * Constructs a new GroupServiceImpl with the specified groups generator and
     * group repository.
     *
     * @param groupsGenerator an instance of {@link Generatable} for generating
     *                        groups
     * @param groupRepository an instance of {@link GroupRepository} for accessing
     *                        and managing group data
     */
    public GroupServiceImpl(Generatable<GroupDto> groupsGenerator, GroupRepository groupRepository) {
        this.groupsGenerator = groupsGenerator;
        this.groupRepository = groupRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initGroups() {
        LOGGER.info("Filling with generated groups");
        groupsGenerator.toGenerate().stream()
                .map(GroupMapper::mapDtoToGroup)
                .forEach(groupRepository::save);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Group> addGroup(GroupDto newGroup) {
        Group group = GroupMapper.mapDtoToGroup(newGroup);
        LOGGER.debug("Adding a new group: {}", group);

        return Optional.ofNullable(groupRepository.save(group));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Group> getGroupByName(String groupName) {
        Optional<Group> group = groupRepository.findByGroupName(groupName);
        LOGGER.debug("Search group by name {}: {}", groupName, group);

        return group;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> getAllGroups() {
        List<Group> allGroups = groupRepository.findAll();
        LOGGER.debug("Search for all groups. All received groups: {}", allGroups);

        return allGroups;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Group> updateGroup(Group updatedGroup) {
        LOGGER.debug("Updating group data. Updated group: {}", updatedGroup);
        Group group = groupRepository.save(updatedGroup);

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
            groupRepository.delete(group.get());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> getGroupsWithGivenNumberOfStudents(Integer amountOfStudents) {
        List<Group> groupsWithGivenNumberOfStudents = groupRepository.findAll().stream()
                .filter(group -> group.getStudents().size() <= amountOfStudents)
                .toList();

        LOGGER.debug("Obtained groups with the number of students {}: {}", amountOfStudents,
                groupsWithGivenNumberOfStudents);
        return groupsWithGivenNumberOfStudents;
    }

}
