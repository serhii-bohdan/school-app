package ua.foxminded.schoolapp.model;

import java.io.Serializable;
import java.util.Objects;

public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String courseName;
    private String description;

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

    @Override
    public int hashCode() {
        return Objects.hash(courseName, description);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Course other = (Course) obj;
        return Objects.equals(courseName, other.courseName) && Objects.equals(description, other.description);
    }

    @Override
    public String toString() {
        return "Course [id=" + id + ", courseName=" + courseName + ", description=" + description + "]";
    }

}
