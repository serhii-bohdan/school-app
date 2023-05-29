package ua.foxminded.schoolapp.model;

import java.io.Serializable;
import java.util.Objects;

public class Group implements Serializable {

    private static final long serialVersionUID = -533798781066983776L;

    private int id;
    private String groupName;

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public Group() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName, id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Group other = (Group) obj;
        return Objects.equals(groupName, other.groupName) && id == other.id;
    }

    @Override
    public String toString() {
        return "Group [id=" + id + ", groupName=" + groupName + "]";
    }

}
