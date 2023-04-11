package ua.foxminded.schoolapp.entity;

public class Group {

    private int groupId;
    private String name;

    public Group(String name) {
        super();
        this.name = name;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GroupDTO [groupId=" + groupId + ", name=" + name + "]";
    }

}
