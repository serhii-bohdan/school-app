package ua.foxminded.schoolapp.service.logic.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.foxminded.schoolapp.dao.GroupDao;
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
 * {@link Generatable<Group>} object to generate groups and a {@link GroupDao}
 * object to access the group data.
 *
 * @author Serhii Bohdan
 */
@Service
public class GroupServiceImpl implements GroupService {

    /**
     * The logger for logging events and messages in the {@link GroupServiceImpl}
     * class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final Generatable<Group> groupsGenerator;
    private final GroupDao groupDao;

    /**
     * Constructs a new GroupServiceImpl with the specified groups generator and
     * group Dao.
     *
     * @param groupsGenerator the generator for creating groups
     * @param groupDao        the data access object for groups
     */
    public GroupServiceImpl(Generatable<Group> groupsGenerator, GroupDao groupDao) {
        this.groupsGenerator = groupsGenerator;
        this.groupDao = groupDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initGroups() {
        LOGGER.info("Filling with generated groups");
        groupsGenerator.toGenerate().forEach(groupDao::save);
    }

    /**
     * {@inheritDoc}
     */
    public List<Group> getAllGroups() {
        List<Group> allGroups = groupDao.findAll();
        LOGGER.debug("All received groups: {}", allGroups);

        return allGroups;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Group, Integer> getGroupsWithGivenNumberOfStudents(int amountOfStudents) {
        Map<Group, Integer> groupsWithTheirNumberOfStudents = groupDao
                .findGroupsWithGivenNumberOfStudents(amountOfStudents).stream()
                .collect(Collectors.toMap(group -> group, groupDao::findNumberOfStudentsForGroup));
        LOGGER.debug("Received groups with a given number of students: {}", groupsWithTheirNumberOfStudents);

        return groupsWithTheirNumberOfStudents;
    }

}
