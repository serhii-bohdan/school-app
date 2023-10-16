package ua.foxminded.schoolapp.dto;

import java.util.Objects;
import java.util.Set;
import ua.foxminded.schoolapp.model.Student;

/**
 * The CourseDto class represents a course data transfer object. It contains
 * information about the course's ID, name, description, and associated
 * students.
 * </p>
 *
 * @author Serhii Bohdan
 */
public class CourseDto {

    private Integer id;
    private String courseName;
    private String description;
    private Set<Student> students;

    /**
     * Constructs a CourseDto object with the specified course name and description.
     *
     * @param courseName  the name of the course
     * @param description the description of the course
     */
    public CourseDto(String courseName, String description) {
        this.courseName = courseName;
        this.description = description;
    }

    public CourseDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseName, description, id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CourseDto other = (CourseDto) obj;
        return Objects.equals(courseName, other.courseName) && Objects.equals(description, other.description)
                && Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "CourseDto [id=" + id + ", courseName=" + courseName + ", description=" + description + "]";
    }

}
