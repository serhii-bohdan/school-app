package ua.foxminded.schoolapp.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * The Group class represents a group in a school and implements
 * {@link Serializable} interface. It contains information about 
 * the group's ID and name.
 *
 * @author Serhii Bohdan
 */
public class Group implements Serializable {

    private static final long serialVersionUID = -533798781066983776L;

    private int id;
    private String groupName;

    /**
     * Constructs a Group object with the specified group name.
     *
     * @param groupName the name of the group
     */
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

    /**
     * Generates a hash code for the group.
     *
     * @return the hash code value for the group
     */
    @Override
    public int hashCode() {
        return Objects.hash(groupName, id);
    }

    /**
     * Checks if this group is equal to another object. Two groups are considered
     * equal if they have the same ID and name.
     *
     * @param obj the object to compare to
     * @return true if the groups are equal, false otherwise
     */
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

    /**
     * Returns a string representation of the group.
     *
     * @return a string representation of the group
     */
    @Override
    public String toString() {
        return "Group [id=" + id + ", groupName=" + groupName + "]";
    }

}
