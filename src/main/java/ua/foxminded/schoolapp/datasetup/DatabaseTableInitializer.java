package ua.foxminded.schoolapp.datasetup;

import java.util.List;
import ua.foxminded.schoolapp.dao.CourseDAO;
import ua.foxminded.schoolapp.dao.CourseDAOImpl;
import ua.foxminded.schoolapp.dao.ExecutorDAO;
import ua.foxminded.schoolapp.dao.GroupDAO;
import ua.foxminded.schoolapp.dao.GroupDAOImpl;
import ua.foxminded.schoolapp.dao.StudentDAO;
import ua.foxminded.schoolapp.dao.StudentDAOImpl;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

public class DatabaseTableInitializer implements Initializable {

    private ExecutorDAO executor;
    private Generatable<Group> groupsGenerator;
    private Generatable<Student> studentsGenerator;
    private Generatable<Course> coursesGenerator;

    public DatabaseTableInitializer(ExecutorDAO executor, Generatable<Group> groupsGenerator,
            Generatable<Student> studentsGenerator, Generatable<Course> coursesGenerator) {
        this.executor = executor;
        this.groupsGenerator = groupsGenerator;
        this.studentsGenerator = studentsGenerator;
        this.coursesGenerator = coursesGenerator;
    }

    public void initialize() {
        executor.executeSqlScriptFrom("sql/tables_creation.sql");
        fillGroupsTable();
        fillStudentsTable();
        fillCoursesTable();
        executor.executeSqlScriptFrom("sql/students_courses_filling.sql");
    }

    private void fillGroupsTable() {
        GroupDAO groupDao = new GroupDAOImpl();
        List<Group> groups = groupsGenerator.toGenerate();

        for (Group group : groups) {
            groupDao.save(group);
        }
    }

    private void fillStudentsTable() {
        StudentDAO studentDao = new StudentDAOImpl();
        List<Student> students = studentsGenerator.toGenerate();

        for (Student student : students) {
            studentDao.save(student);
        }
    }

    private void fillCoursesTable() {
        CourseDAO courseDao = new CourseDAOImpl();
        List<Course> courses = coursesGenerator.toGenerate();

        for (Course course : courses) {
            courseDao.save(course);
        }
    }

}
