package ua.foxminded.schoolapp;

import java.util.Scanner;
import ua.foxminded.schoolapp.model.*;
import ua.foxminded.schoolapp.service.SchoolService;
import ua.foxminded.schoolapp.service.Service;
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
import ua.foxminded.schoolapp.cli.SchoolController;
import ua.foxminded.schoolapp.cli.SchoolView;
import ua.foxminded.schoolapp.cli.Controller;
import ua.foxminded.schoolapp.cli.View;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static Connectable connector = new Connector("application.properties");
    private static Reader reader = new ReaderImpl();
    private static ExecutorDao executorDao = new SqlScriptsExecutorDao(connector);
    private static Generatable<Group> groupsGenerator = new GroupsGenerator();
    private static Generatable<Student> studentsGenerator = new StudentsGenerator(reader);
    private static Generatable<Course> coursesGenerator = new CoursesGenerator(reader);
    private static Initializable initializer = new DatabaseTableInitializer(connector, groupsGenerator,
            studentsGenerator, coursesGenerator);

    private static StudentDao studentDao = new StudentDaoImpl(connector);
    private static GroupDao groupDao = new GroupDaoImpl(connector);
    private static CourseDao courseDao = new CourseDaoImpl(connector);
    private static Service service = new SchoolService(studentDao, groupDao, courseDao);
    private static View view = new SchoolView(scanner);
    private static Controller controller = new SchoolController(service, view);

    public static void main(String[] args) {
        String tablesCreationScript = reader.readAllFileToString("sql/tables_creation.sql");
        executorDao.executeSqlScript(tablesCreationScript);
        initializer.initialize();
        controller.runSchoolApp();
        scanner.close();
    }

    public static void setScanner(Scanner scanner) {
        Main.scanner = scanner;
    }

    public static void setReader(Reader reader) {
        Main.reader = reader;
    }

    public static void setExecutorDao(ExecutorDao executorDao) {
        Main.executorDao = executorDao;
    }

    public static void setInitializer(Initializable initializer) {
        Main.initializer = initializer;
    }

    public static void setController(Controller controller) {
        Main.controller = controller;
    }

}
