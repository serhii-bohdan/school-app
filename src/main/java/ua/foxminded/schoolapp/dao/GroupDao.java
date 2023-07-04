package ua.foxminded.schoolapp.dao;

import java.util.List;
import ua.foxminded.schoolapp.model.Group;

/**
 * The GroupDao interface provides operations for accessing and manipulating
 * Group entities. It extends the {@link BaseDao} BaseDao interface.
 *
 * @author Serhii Bohdan
 */
public interface GroupDao extends BaseDao<Group> {

    /**
     * Finds groups with the specified number of students.
     *
     * @param amountOfStudents the number of students
     * @return a list of Group objects that match the specified criteria
     */
    List<Group> findGroupsWithGivenNumberStudents(int amountOfStudents);

}
