package ua.foxminded.schoolapp.datasetup.impl;

import java.util.List;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.CourseDao;
import ua.foxminded.schoolapp.dao.ExecutorDao;
import ua.foxminded.schoolapp.dao.GroupDao;
import ua.foxminded.schoolapp.dao.StudentDao;
import ua.foxminded.schoolapp.dao.impl.CourseDaoImpl;
import ua.foxminded.schoolapp.dao.impl.GroupDaoImpl;
import ua.foxminded.schoolapp.dao.impl.StudentDaoImpl;
import ua.foxminded.schoolapp.datasetup.Generatable;
import ua.foxminded.schoolapp.datasetup.Initializable;
import ua.foxminded.schoolapp.datasetup.Reader;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

public class DatabaseTableInitializer implements Initializable {

    private Connectable connector;
    private Reader reader;
    private ExecutorDao executor;
    private Generatable<Group> groupsGenerator;
    private Generatable<Student> studentsGenerator;
    private Generatable<Course> coursesGenerator;

    public DatabaseTableInitializer(Connectable connector, Reader reader, ExecutorDao executor,
            Generatable<Group> groupsGenerator, Generatable<Student> studentsGenerator,
            Generatable<Course> coursesGenerator) {
        this.connector = connector;
        this.reader = reader;
        this.executor = executor;
        this.groupsGenerator = groupsGenerator;
        this.studentsGenerator = studentsGenerator;
        this.coursesGenerator = coursesGenerator;
    }

    public void initialize() {
        String tablesCreationScript = reader.readAllFileToString("sql/tables_creation.sql");
        String studentsCoursesFillingScript = reader.readAllFileToString("sql/students_courses_filling.sql");
        executor.executeSqlScript(tablesCreationScript);
        fillGroupsTable();
        fillStudentsTable();
        fillCoursesTable();
        executor.executeSqlScript(studentsCoursesFillingScript);
    }

    private void fillGroupsTable() {
        GroupDao groupDao = new GroupDaoImpl(connector);
        List<Group> groups = groupsGenerator.toGenerate();

        for (Group group : groups) {
            groupDao.save(group);
        }
    }

    private void fillStudentsTable() {
        StudentDao studentDao = new StudentDaoImpl(connector);
        List<Student> students = studentsGenerator.toGenerate();

        for (Student student : students) {
            studentDao.save(student);
        }
    }

    private void fillCoursesTable() {
        CourseDao courseDao = new CourseDaoImpl(connector);
        List<Course> courses = coursesGenerator.toGenerate();

        for (Course course : courses) {
            courseDao.save(course);
        }
    }

}
