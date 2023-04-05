package ua.foxminded.schoolapp;

import ua.foxminded.schoolapp.datageneration.TablesPlaceholder;

public class Main {

    public static void main(String[] args) {
        TablesPlaceholder holder = new TablesPlaceholder();
        holder.createTables();
        holder.fillTables();
    }

}
