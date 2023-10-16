package ua.foxminded.schoolapp.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/**
 * The Course class represents a course in a school.
 * <p>
 * This class is annotated with {@link Entity} to mark it as a JPA entity, and
 * it is mapped to the "courses" table in the database. It implements the
 * {@link Serializable} interface to allow for serialization. It contains
 * information about the course's ID, name, description, and associated
 * students.
 * </p>
 *
 * @author Serhii Bohdan
 */
@Entity
@Table(name = "courses")
public class Course implements Serializable {

    private static final long serialVersionUID = -7353839263354063173L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Integer id;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "course_description")
    private String description;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private Set<Student> students = new HashSet<>();

    /**
     * Constructs a Course object with the specified course name and description.
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

    /**
     * Adds a student to the course.
     *
     * @param student the student to add to the course
     */
    public void addStudent(Student student) {
        this.students.add(student);
    }

    /**
     * Deletes a student from the course.
     *
     * @param student the student to delete from the course
     */
    public void deleteStudent(Student student) {
        this.students.remove(student);
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
        return Collections.unmodifiableSet(students);
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
        Course other = (Course) obj;
        return Objects.equals(courseName, other.courseName) && Objects.equals(description, other.description)
                && Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Course [id=" + id + ", courseName=" + courseName + ", description=" + description + "]";
    }

}
