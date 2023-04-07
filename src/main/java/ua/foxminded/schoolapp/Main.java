package ua.foxminded.schoolapp;

import ua.foxminded.schoolapp.datageneration.DatabaseTableInitializer;

public class Main {

    private static DatabaseTableInitializer initializer = new DatabaseTableInitializer();
    
    public static void main(String[] args) {
        initializer.initialize();
    }

}
