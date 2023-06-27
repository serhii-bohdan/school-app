package ua.foxminded.schoolapp.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * The Course class represents a course in a school and implements
 * {@link Serializable} interface. It contains information about the
 * course's ID, name, and description.
 *
 * @author Serhii Bohdan
 */
public class Course implements Serializable {

    private static final long serialVersionUID = -7353839263354063173L;

    private int id;
    private String courseName;
    private String description;

    /**
     * Constructs a Course object with the specified name and description.
     *
     * @param courseName  the name of the course
     * @param description the description of the course
     */
    public Course(String courseName, String description) {
        this.courseName = courseName;
        this.description = description;
    }

    public Course() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Generates a hash code for the course.
     *
     * @return the hash code value for the course
     */
    @Override
    public int hashCode() {
        return Objects.hash(courseName, description, id);
    }

    /**
     * Checks if this course is equal to another object. Two courses are considered
     * equal if they have the same ID, name, and description.
     *
     * @param obj the object to compare to
     * @return true if the courses are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Course other = (Course) obj;
        return Objects.equals(courseName, other.courseName) && Objects.equals(description, other.description)
                && id == other.id;
    }

    /**
     * Returns a string representation of the course.
     *
     * @return a string representation of the course
     */
    @Override
    public String toString() {
        return "Course [id=" + id + ", courseName=" + courseName + ", description=" + description + "]";
    }

}
