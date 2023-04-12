package ua.foxminded.schoolapp;

import ua.foxminded.schoolapp.cli.ConsoleManager;
import ua.foxminded.schoolapp.datageneration.DatabaseTableInitializer;

public class Main {

    private static DatabaseTableInitializer initializer = new DatabaseTableInitializer();
    private static ConsoleManager manager = new ConsoleManager();

    public static void main(String[] args) {
        initializer.initialize();
        System.out.println(manager.getGroupsWithGivenNumberStudents(30));
        System.out.println(manager.getStudentsRelatedToCourse("Art"));
        manager.addNewStudent("Mark", "Ostin", "GX-48");
    }

}
