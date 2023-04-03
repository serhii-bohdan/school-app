package ua.foxminded.schoolapp;

import ua.foxminded.schoolapp.dao.TablesSetupDAO;
import ua.foxminded.schoolapp.datageneration.CoursesReader;
import ua.foxminded.schoolapp.datageneration.GroupsGenerator;
import ua.foxminded.schoolapp.datageneration.StudentsGeneration;

public class Main {

    public static void main(String[] args) {
        TablesSetupDAO setUp = new TablesSetupDAO();
        setUp.createTables();

        GroupsGenerator generator = new GroupsGenerator();
        System.out.println(generator.generateUniqueGroups());

        CoursesReader reader = new CoursesReader();
        System.out.println(reader.readCoursesFrom("courses.txt"));
        
        StudentsGeneration studGen = new StudentsGeneration();
        studGen.generateStudents().stream().forEach(System.out::println);
        System.out.println(studGen.generateStudents().size());
    }

}
