package ua.foxminded.schoolapp.dao;

import java.util.List;
import ua.foxminded.schoolapp.entity.Group;

public interface GroupDAO extends CrudDAO<Group> {

    int findGroupIdByGroupName(String groupName);
    List<Group> findGroupsWithGivenNumberStudents(int amountOfStudents);

}
