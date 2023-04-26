package ua.foxminded.schoolapp.cli;

import java.util.List;
import ua.foxminded.schoolapp.entity.Group;
import ua.foxminded.schoolapp.entity.Student;

public class Formatter {

    public String formatGroups(List<Group> groups) {
        if (groups.isEmpty()) {
            return "The list of groups is empty.";
        }

        StringBuilder formattedGroups = new StringBuilder("Groups:");

        for (Group group : groups) {
            formattedGroups.append(String.format("\n%-6s %s", "", group.getGroupName()));
        }
        return formattedGroups.toString();
    }

    public String formatStudentsRelatedToCourse(List<Student> students) {
        if (students.isEmpty()) {
            return "The list of students is empty.";
        }

        StringBuilder sformatedStuentes = new StringBuilder("Students:");

        for (Student student : students) {
            sformatedStuentes.append(String.format("\n%-8s %s %s", "", student.getFirstName(), student.getLastName()));
        }
        return sformatedStuentes.toString();
    }
}
