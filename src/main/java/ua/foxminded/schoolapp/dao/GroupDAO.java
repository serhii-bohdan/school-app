package ua.foxminded.schoolapp.dao;

import java.util.List;
import ua.foxminded.schoolapp.entity.Group;

public interface GroupDAO extends CRUD<Group> {

    int findGroupId(Group group);

    List<Group> findGroupsWithGivenNumberStudents(int amountOfStudents);

}
