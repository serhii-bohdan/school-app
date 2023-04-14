package ua.foxminded.schoolapp.entity;

import ua.foxminded.schoolapp.dao.GroupDAO;
import ua.foxminded.schoolapp.dao.implement.GroupDAOImpl;

public class Group {

    private int groupId;
    private String name;
    private GroupDAO groupDao = new GroupDAOImpl();

    public Group(String name) {
        super();
        this.name = name;
    }

    public int getGroupId() {
        this.groupId = groupDao.findGroupIdByGroupName(name);
        return groupId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "GroupDTO [groupId=" + groupId + ", name=" + name + "]";
    }

}
