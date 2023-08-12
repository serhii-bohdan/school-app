package ua.foxminded.schoolapp.service.impl;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ua.foxminded.schoolapp.dao.GroupDao;
import ua.foxminded.schoolapp.datasetup.Generatable;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.service.GroupService;

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

    private Generatable<Group> groupsGenerator;
    private GroupDao groupDao;

    /**
     * Constructs a new GroupServiceImpl with the specified groups generator and
     * group Dao.
     *
     * @param groupsGenerator the generator for creating groups
     * @param groupDao        the data access object for groups
     * @throws NullPointerException if either groupsGenerator or groupDao is null
     */
    public GroupServiceImpl(Generatable<Group> groupsGenerator, GroupDao groupDao) {
        Objects.requireNonNull(groupsGenerator, "groupsGenerator must not be null");
        Objects.requireNonNull(groupDao, "groupDao must not be null");
        this.groupsGenerator = groupsGenerator;
        this.groupDao = groupDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initGroups() {
        groupsGenerator.toGenerate().stream()
                                    .forEach(groupDao::save);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Group, Integer> getGroupsWithGivenNumberOfStudents(int amountOfStudents) {
        return groupDao.findGroupsWithGivenNumberStudents(amountOfStudents).stream()
                .collect(Collectors.toMap(group -> group, group -> groupDao.findNumberOfStudentsForGroup(group)));
    }

}
