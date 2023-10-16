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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * The Student class represents a student in a school.
 * <p>
 * This class is annotated with {@link Entity} to mark it as a JPA entity, and
 * it is mapped to the "students" table in the database. It implements the
 * {@link Serializable} interface to allow for serialization. It contains
 * information about the student's ID, first name, last name, group, and
 * courses.
 * </p>
 *
 * @author Serhii Bohdan
 */
@Entity
@Table(name = "students")
public class Student implements Serializable {

    private static final long serialVersionUID = -4502594183161233658L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "students_courses",
         joinColumns = @JoinColumn(name = "student_id"),
         inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses = new HashSet<>();

    /**
     * Constructs a Student object with the specified first name, last name, and
     * group.
     *
     * @param firstName the first name of the student
     * @param lastName  the last name of the student
     * @param group     the group the student belongs to
     */
    public Student(String firstName, String lastName, Group group) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
    }

    public Student() {
    }

    /**
     * Adds a course to the student's courses.
     *
     * @param course the course to add
     */
    public void addCourse(Course course) {
        this.courses.add(course);
        course.addStudent(this);
    }

    /**
     * Deletes a course from the student's courses.
     *
     * @param course the course to delete
     */
    public void deleteCourse(Course course) {
        this.courses.remove(course);
        course.deleteStudent(this);
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
        return Collections.unmodifiableSet(courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, group, id, lastName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        return Objects.equals(firstName, other.firstName) && Objects.equals(group, other.group)
                && Objects.equals(id, other.id) && Objects.equals(lastName, other.lastName);
    }

    @Override
    public String toString() {
        return "Student [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", group=" + group + "]";
    }

}
