package ua.foxminded.schoolapp.entity;

public class Group {

    private String name;

    public Group(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Group [name=" + name + "]";
    }

}
