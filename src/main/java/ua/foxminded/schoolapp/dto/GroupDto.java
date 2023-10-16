package ua.foxminded.schoolapp.dto;

import java.util.Objects;
import java.util.Set;
import ua.foxminded.schoolapp.model.Student;

/**
 * The GroupDto class represents a group data transfer object. It contains
 * information about the group's ID, name, and associated students.
 * </p>
 *
 * @author Serhii Bohdan
 */
public class GroupDto {

    private Integer id;
    private String groupName;
    private Set<Student> students;

    /**
     * Constructs a GroupDto object with the specified group name.
     *
     * @param groupName the name of the group
     */
    public GroupDto(String groupName) {
        this.groupName = groupName;
    }

    public GroupDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
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
        GroupDto other = (GroupDto) obj;
        return Objects.equals(groupName, other.groupName) && Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "GroupDto [id=" + id + ", groupName=" + groupName + ", students=" + students + "]";
    }

}
