package ua.foxminded.schoolapp.cli;

import ua.foxminded.schoolapp.dao.ConsoleQueryDAO;
import ua.foxminded.schoolapp.entity.Group;
import ua.foxminded.schoolapp.exception.InputException;
import java.util.List;

public class ConsoleManager {

    ConsoleQueryDAO dao = new ConsoleQueryDAO();

    public String getGroupsWithGivenNumberStudents(int amountOfStudents) {
        List<Group> groups;
        StringBuilder result = new StringBuilder();

        if (amountOfStudents >= 0 && amountOfStudents <= 30) {
            groups = dao.findGroupsWithGivenNumberStudents(amountOfStudents);
        } else {
            throw new InputException("The entered number of students is not correct."
                    + "The number of students should be between 0 and 30 inclusive.");
        }

        for (Group group : groups) {
            result.append(group.getName() + "\n");
        }
        return result.toString();
    }

}
