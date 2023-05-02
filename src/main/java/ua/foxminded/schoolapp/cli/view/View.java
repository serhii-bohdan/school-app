package ua.foxminded.schoolapp.cli.view;

import java.util.List;
import ua.foxminded.schoolapp.model.*;

public interface View {

    void showMenu();

    int getChoise();

    void printMessage(String message);

    int getNumberOfStuentsFromUser();

    String getCourseNameFromUser();

    String getStudentFirstNameFromUser();

    String getStudentLastNameFromUser();

    int getGroupIdFromUser();

    int getStudentIdFromUser();

    String getConfirmationFromUserAboutDeletingStudent(Student student);

    void displayGroups(List<Group> groups);

    void displayStudents(List<Student> students);

}
