package ua.foxminded.schoolapp.service.logic.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        groupsGenerator.toGenerate().forEach(groupDao::save);
    }

    /**
     * {@inheritDoc}
     */
    public List<Group> getAllGroups() {
        return groupDao.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Group, Integer> getGroupsWithGivenNumberOfStudents(int amountOfStudents) {
        return groupDao.findGroupsWithGivenNumberStudents(amountOfStudents).stream()
                .collect(Collectors.toMap(group -> group, groupDao::findNumberOfStudentsForGroup));
    }

}
