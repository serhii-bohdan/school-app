package ua.foxminded.schoolapp;

import ua.foxminded.schoolapp.datasetup.*;
import ua.foxminded.schoolapp.datasetup.impl.*;
import ua.foxminded.schoolapp.model.*;
import ua.foxminded.schoolapp.dao.*;
import ua.foxminded.schoolapp.dao.impl.*;
import ua.foxminded.schoolapp.cli.controller.*;
import ua.foxminded.schoolapp.cli.view.*;

public class Main {

    private static Reader reader = new ReaderImpl();
    private static ExecutorDAO executor = new SQLScriptsExecutorDAO(reader);
    private static Generatable<Group> groupsGenerator = new GroupsGenerator();
    private static Generatable<Student> studentsGenerator = new StudentsGenerator(reader);
    private static Generatable<Course> coursesGenerator = new CoursesGenerator(reader);
    private static Initializable initializer = new DatabaseTableInitializer(executor, groupsGenerator,
            studentsGenerator, coursesGenerator);

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
