package ua.foxminded.schoolapp.service.logic;

import java.util.List;
import java.util.Map;
import ua.foxminded.schoolapp.model.Group;

/**
 * The GroupService interface provides operations for managing groups.
 *
 * @author Serhii Bohdan
 */
public interface GroupService {

    /**
     * Initializes the groups by generating them and saving them.
     */
    void initGroups();

    /**
     * Retrieves a list of all groups.
     *
     * @return a list of all groups
     */
    List<Group> getAllGroups();

    /**
     * Retrieves a map of groups with the given number of students.
     *
     * @param amountOfStudents the number of students for which to retrieve the
     *                         groups
     * @return a map of groups with the given number of students
     */
    Map<Group, Integer> getGroupsWithGivenNumberOfStudents(int amountOfStudents);

}
