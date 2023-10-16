package ua.foxminded.schoolapp.service.logic;

import java.util.List;
import java.util.Optional;
import ua.foxminded.schoolapp.model.Group;

/**
 * The GroupService interface provides operations for managing groups. It allows
 * for initializing groups, adding, retrieving, updating, and deleting groups,
 * as well as obtaining a list of groups and filtering groups by the number of
 * students.
 *
 * @author Serhii Bohdan
 */
public interface GroupService {

    /**
     * Initializes the groups by generating them and saving them.
     */
    void initGroups();

    /**
     * Adds a new group with the specified name.
     *
     * @param groupName the name of the group to add
     * @return an {@link Optional} containing the added group if successful, empty
     *         otherwise
     */
    Optional<Group> addGroup(String groupName);

    /**
     * Retrieves a group by its name.
     *
     * @param groupName the name of the group to retrieve
     * @return an {@link Optional} containing the retrieved group, empty if not
     *         found
     */
    Optional<Group> getGroupByName(String groupName);

    /**
     * Retrieves a list of all groups.
     *
     * @return a list of all groups
     */
    List<Group> getAllGroups();

    /**
     * Updates the information of an existing group.
     *
     * @param updatedGroup the updated group object
     * @return an {@link Optional} containing the updated group if successful, empty
     *         otherwise
     */
    Optional<Group> updateGroup(Group updatedGroup);

    /**
     * Deletes a group by its name.
     *
     * @param groupName the name of the group to delete
     */
    void deleteGroupByName(String groupName);

    /**
     * Retrieves a list of groups with a given number of students or fewer.
     *
     * @param amountOfStudents the number of students for which to retrieve the
     *                         groups
     * @return a list of groups with the given number of students or fewer
     */
    List<Group> getGroupsWithGivenNumberOfStudents(Integer amountOfStudents);

}