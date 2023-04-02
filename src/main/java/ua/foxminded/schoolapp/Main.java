package ua.foxminded.schoolapp;

import ua.foxminded.schoolapp.dao.TablesSetupDAO;

public class Main {

    public static void main(String[] args) {
        TablesSetupDAO setUp  = new TablesSetupDAO();
        setUp.createTables();
    }

}
