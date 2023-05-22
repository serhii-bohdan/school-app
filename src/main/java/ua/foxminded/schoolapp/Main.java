package ua.foxminded.schoolapp;

import ua.foxminded.schoolapp.model.*;
import ua.foxminded.schoolapp.dao.*;
import ua.foxminded.schoolapp.dao.impl.Connector;
import ua.foxminded.schoolapp.dao.impl.CourseDAOImpl;
import ua.foxminded.schoolapp.dao.impl.GroupDAOImpl;
import ua.foxminded.schoolapp.dao.impl.SQLScriptsExecutorDAO;
import ua.foxminded.schoolapp.dao.impl.StudentDAOImpl;
import ua.foxminded.schoolapp.datasetup.Generatable;
import ua.foxminded.schoolapp.datasetup.Initializable;
import ua.foxminded.schoolapp.datasetup.Reader;
import ua.foxminded.schoolapp.datasetup.impl.*;
import ua.foxminded.schoolapp.cli.controller.*;
import ua.foxminded.schoolapp.cli.view.*;

public class Main {

    private static Connectable connector = new Connector("application.properties");
    private static Reader reader = new ReaderImpl();
    private static ExecutorDAO executor = new SQLScriptsExecutorDAO(connector, reader);
    private static Generatable<Group> groupsGenerator = new GroupsGenerator();
    private static Generatable<Student> studentsGenerator = new StudentsGenerator(reader);
    private static Generatable<Course> coursesGenerator = new CoursesGenerator(reader);
    private static Initializable initializer = new DatabaseTableInitializer(connector, executor, groupsGenerator,
            studentsGenerator, coursesGenerator);

    private static StudentDAO studentDao = new StudentDAOImpl(connector);
    private static GroupDAO groupDao = new GroupDAOImpl(connector);
    private static CourseDAO courseDao = new CourseDAOImpl(connector);
    private static Validator validator = new InputValidator(studentDao, groupDao, courseDao);
    private static View view = new ConsoleView();
    private static Controller controller = new QueryController(validator, view);

    public static void main(String[] args) {
        initializer.initialize();
        controller.run();
    }

}
