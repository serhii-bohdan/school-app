package ua.foxminded.schoolapp.cli;

import java.util.List;
import ua.foxminded.schoolapp.model.*;

public interface View {

    void showMenu();

    void printMessage(String message);

    int getIntNumberFromUser(String message);

    String getWordFromUser(String message);

    String getConfirmationFromUserAboutDeletingStudent(Student student);

    void displayGroups(List<Group> groups);

    void displayStudents(List<Student> students);

}
