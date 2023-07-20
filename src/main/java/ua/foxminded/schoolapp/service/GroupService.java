package ua.foxminded.schoolapp.service;

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
     * Retrieves a map of groups with the given number of students.
     *
     * @param amountOfStudents the number of students for which to retrieve the
     *                         groups
     * @return a map of groups with the given number of students
     */
    Map<Group, Integer> getGroupsWithGivenNumberOfStudents(int amountOfStudents);

}
