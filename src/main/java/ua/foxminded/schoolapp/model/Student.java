package ua.foxminded.schoolapp.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * The Student class represents a student in a school and implements
 * {@link Serializable} interface. It contains information about
 * the student's ID, first name, last name, and group ID.
 *
 * @author Serhii Bohdan
 */
public class Student implements Serializable {

    private static final long serialVersionUID = -4502594183161233658L;

    private int id;
    private String firstName;
    private String lastName;
    private int groupId;

    /**
     * Constructs a Student object with the specified first name, last name, and
     * group ID.
     *
     * @param firstName the first name of the student
     * @param lastName  the last name of the student
     * @param groupId   the ID of the group to which the student belongs
     */
    public Student(String firstName, String lastName, int groupId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupId = groupId;
    }

    public Student() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
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

    /**
     * Generates a hash code for the student.
     *
     * @return the hash code value for the student
     */
    @Override
    public int hashCode() {
        return Objects.hash(firstName, groupId, id, lastName);
    }

    /**
     * Checks if this student is equal to another object. Two students are
     * considered equal if they have the same ID, first name, last name, and group
     * ID.
     *
     * @param obj the object to compare to
     * @return true if the students are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        return Objects.equals(firstName, other.firstName) && groupId == other.groupId && id == other.id
                && Objects.equals(lastName, other.lastName);
    }

    /**
     * Returns a string representation of the student.
     *
     * @return a string representation of the student
     */
    @Override
    public String toString() {
        return "Student [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", groupId=" + groupId
                + "]";
    }

}
