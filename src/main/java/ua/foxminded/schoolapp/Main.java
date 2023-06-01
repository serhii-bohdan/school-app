package ua.foxminded.schoolapp;

import ua.foxminded.schoolapp.model.*;
import ua.foxminded.schoolapp.dao.*;
import ua.foxminded.schoolapp.dao.impl.Connector;
import ua.foxminded.schoolapp.dao.impl.CourseDaoImpl;
import ua.foxminded.schoolapp.dao.impl.GroupDaoImpl;
import ua.foxminded.schoolapp.dao.impl.SqlScriptsExecutorDao;
import ua.foxminded.schoolapp.dao.impl.StudentDaoImpl;
import ua.foxminded.schoolapp.datasetup.Generatable;
import ua.foxminded.schoolapp.datasetup.Initializable;
import ua.foxminded.schoolapp.datasetup.Reader;
import ua.foxminded.schoolapp.datasetup.impl.*;
import ua.foxminded.schoolapp.cli.controller.*;
import ua.foxminded.schoolapp.cli.view.*;

public class Main {

    private static Connectable connector = new Connector("application.properties");
    private static Reader reader = new ReaderImpl();
    private static ExecutorDao executor = new SqlScriptsExecutorDao(connector);
    private static Generatable<Group> groupsGenerator = new GroupsGenerator();
    private static Generatable<Student> studentsGenerator = new StudentsGenerator(reader);
    private static Generatable<Course> coursesGenerator = new CoursesGenerator(reader);
    private static Initializable initializer = new DatabaseTableInitializer(connector, groupsGenerator,
            studentsGenerator, coursesGenerator);

    private static StudentDao studentDao = new StudentDaoImpl(connector);
    private static GroupDao groupDao = new GroupDaoImpl(connector);
    private static CourseDao courseDao = new CourseDaoImpl(connector);
    private static Validator validator = new InputValidator(studentDao, groupDao, courseDao);
    private static View view = new ConsoleView();
    private static Controller controller = new QueryController(validator, view);

    public static void main(String[] args) {
        String tablesCreationScript = reader.readAllFileToString("sql/tables_creation.sql");
        executor.executeSqlScript(tablesCreationScript);
        initializer.initialize();
        controller.run();
    }

}
