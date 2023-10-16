package ua.foxminded.schoolapp.dao;

import java.util.Optional;
import ua.foxminded.schoolapp.model.Group;

/**
 * The GroupDao interface provides operations for accessing and manipulating
 * Group entities. It extends the {@link BaseDao} interface.
 *
 * @author Serhii Bohdan
 */
public interface GroupDao extends BaseDao<Group> {

    /**
     * Finds a group in the database by its name.
     *
     * @param groupName the name of the group to find
     * @return an {@link Optional} containing the found group, or empty if not found
     */
    Optional<Group> findGroupByName(String groupName);

}