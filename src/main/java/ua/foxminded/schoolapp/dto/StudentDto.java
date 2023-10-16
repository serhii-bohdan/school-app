package ua.foxminded.schoolapp.dto;

import java.util.Objects;
import java.util.Set;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;

/**
 * The StudentDto class represents a student data transfer object. It contains
 * information about the student's ID, first name, last name, group, and
 * associated courses.
 * </p>
 *
 * @author Serhii Bohdan
 */
public class StudentDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private Group group;
    private Set<Course> courses;

    /**
     * Constructs a StudentDto object with the specified first name, last name, and
     * group.
     *
     * @param firstName the first name of the student
     * @param lastName  the last name of the student
     * @param group     the group the student belongs to
     */
    public StudentDto(String firstName, String lastName, Group group) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
    }

    /**
     * Constructs a StudentDto object with the specified first name and last name.
     *
     * @param firstName the first name of the student
     * @param lastName  the last name of the student
     */
    public StudentDto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public StudentDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, id, lastName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StudentDto other = (StudentDto) obj;
        return Objects.equals(firstName, other.firstName) && Objects.equals(id, other.id)
                && Objects.equals(lastName, other.lastName);
    }

    @Override
    public String toString() {
        return "StudentDto [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }

}
