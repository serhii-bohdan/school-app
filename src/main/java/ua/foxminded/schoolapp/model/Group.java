package ua.foxminded.schoolapp.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The Group class represents a group in a school.
 * <p>
 * This class is annotated with {@link Entity} to mark it as a JPA entity, and
 * it is mapped to the "groups" table in the database. It implements the
 * {@link Serializable} interface to allow for serialization. It contains
 * information about the group's ID and name.
 * </p>
 *
 * @author Serhii Bohdan
 */
@Entity
@Table(name = "groups")
public class Group implements Serializable {

    private static final long serialVersionUID = -533798781066983776L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Integer id;

    @Column(name = "group_name")
    private String groupName;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Student> students = new HashSet<>();

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

    /**
     * Adds a student to the group.
     *
     * @param student the student to add to the group
     */
    public void addStudent(Student student) {
        this.students.add(student);
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
        return Collections.unmodifiableSet(students);
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
        return Objects.equals(groupName, other.groupName) && Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Group [id=" + id + ", groupName=" + groupName + "]";
    }

}
