package ua.foxminded.schoolapp.dao;

import java.util.List;
import ua.foxminded.schoolapp.model.Group;

public interface GroupDao extends BaseDao<Group> {

    List<Group> findGroupsWithGivenNumberStudents(int amountOfStudents);

}
