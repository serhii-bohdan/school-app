package ua.foxminded.schoolapp.cli.view;

import java.util.List;
import ua.foxminded.schoolapp.model.*;

public interface View {

    void showMenu();

    void printMessage(String message);

    int getChoise();

    int getNumberOfStuentsFromUser();

    String getCourseNameFromUser();

    String getStudentFirstNameFromUser();

    String getStudentLastNameFromUser();

    int getGroupIdFromUser();

    int getStudentIdForDeletingFromUser();

    String getConfirmationFromUserAboutDeletingStudent(Student student);

    void displayGroups(List<Group> groups);

    void displayStudents(List<Student> students);

}
