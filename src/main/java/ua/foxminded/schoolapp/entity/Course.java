package ua.foxminded.schoolapp.entity;

public class Course {

    private int courseId;
    private String name;
    private String description;

    public Course(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Course [courseId=" + courseId + ", name=" + name + ", description=" + description + "]";
    }

}
