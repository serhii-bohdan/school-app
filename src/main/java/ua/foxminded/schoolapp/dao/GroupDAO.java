package ua.foxminded.schoolapp.dao;

import java.util.List;

import ua.foxminded.schoolapp.model.Group;

public interface GroupDAO extends DAO<Group> {

    int findGroupId(Group group);

    List<Group> findGroupsWithGivenNumberStudents(int amountOfStudents);

}
