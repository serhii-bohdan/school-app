package ua.foxminded.schoolapp;

import ua.foxminded.schoolapp.datasetup.DatabaseTableInitializer;
import ua.foxminded.schoolapp.cli.controller.Validator;
import ua.foxminded.schoolapp.dao.*;
import ua.foxminded.schoolapp.dao.implement.*;
import ua.foxminded.schoolapp.dao.implement.StudentDAOImpl;
import ua.foxminded.schoolapp.cli.controller.Controller;
import ua.foxminded.schoolapp.cli.controller.InputValidator;
import ua.foxminded.schoolapp.cli.controller.QueryController;
import ua.foxminded.schoolapp.cli.view.ConsoleView;
import ua.foxminded.schoolapp.cli.view.View;

public class Main {

    private static DatabaseTableInitializer initializer = new DatabaseTableInitializer();
    private static StudentDAO studentDao = new StudentDAOImpl(); 
    private static GroupDAO groupDao = new GroupDAOImpl(); 
    private static CourseDAO courseDao = new CourseDAOImpl(); 
    private static Validator validator = new InputValidator(studentDao, groupDao, courseDao);
    private static View view = new ConsoleView();
    private static Controller controller = new QueryController(validator, view);

    public static void main(String[] args) {
        initializer.initialize();
        
        controller.run();
    }

}
