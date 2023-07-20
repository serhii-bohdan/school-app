package ua.foxminded.schoolapp;

import java.util.Scanner;
import ua.foxminded.schoolapp.model.*;
import ua.foxminded.schoolapp.service.CourseService;
import ua.foxminded.schoolapp.service.GroupService;
import ua.foxminded.schoolapp.service.ServiceFacade;
import ua.foxminded.schoolapp.service.StudentService;
import ua.foxminded.schoolapp.service.UserInputValidator;
import ua.foxminded.schoolapp.service.impl.CourseServiceImpl;
import ua.foxminded.schoolapp.service.impl.GroupServiceImpl;
import ua.foxminded.schoolapp.service.impl.ServiceFacadeImpl;
import ua.foxminded.schoolapp.service.impl.StudentServiceImpl;
import ua.foxminded.schoolapp.service.impl.UserInputValidatorImpl;
import ua.foxminded.schoolapp.dao.*;
import ua.foxminded.schoolapp.dao.impl.Connector;
import ua.foxminded.schoolapp.dao.impl.CourseDaoImpl;
import ua.foxminded.schoolapp.dao.impl.GroupDaoImpl;
import ua.foxminded.schoolapp.dao.impl.SqlScriptsExecutorDao;
import ua.foxminded.schoolapp.dao.impl.StudentDaoImpl;
import ua.foxminded.schoolapp.datasetup.Generatable;
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
    private static GroupDao groupDao = new GroupDaoImpl(connector);
    private static StudentDao studentDao = new StudentDaoImpl(connector);
    private static CourseDao courseDao = new CourseDaoImpl(connector);
    private static UserInputValidator validator = new UserInputValidatorImpl(groupDao, studentDao, courseDao);
    private static GroupService groupService = new GroupServiceImpl(groupsGenerator, groupDao);
    private static StudentService studentService = new StudentServiceImpl(studentsGenerator, studentDao);
    private static CourseService courseService = new CourseServiceImpl(coursesGenerator, courseDao);
    private static ServiceFacade serviceFacade = new ServiceFacadeImpl(groupService, studentService, courseService, validator);
    private static View view = new SchoolView(scanner);
    private static Controller controller = new SchoolController(serviceFacade, view);

    public static void main(String[] args) {
        String tablesCreationScript = reader.readAllFileToString("sql/tables_creation.sql");
        executorDao.executeSqlScript(tablesCreationScript);
        groupService.initGroups();
        studentService.initStudents();
        courseService.initCourses();
        studentService.initStudentsCoursesTable();
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

    public static void setGroupService(GroupService groupService) {
        Main.groupService = groupService;
    }

    public static void setStudentService(StudentService studentService) {
        Main.studentService = studentService;
    }

    public static void setCourseService(CourseService courseService) {
        Main.courseService = courseService;
    }

    public static void setController(Controller controller) {
        Main.controller = controller;
    }

}
