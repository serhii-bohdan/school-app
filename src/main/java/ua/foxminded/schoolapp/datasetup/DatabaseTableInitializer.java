package ua.foxminded.schoolapp.datasetup;

import java.util.List;
import ua.foxminded.schoolapp.dao.implement.SQLScriptsExecutorDAO;
import ua.foxminded.schoolapp.dao.GroupDAO;
import ua.foxminded.schoolapp.dao.implement.GroupDAOImpl;
import ua.foxminded.schoolapp.dao.StudentDAO;
import ua.foxminded.schoolapp.dao.implement.StudentDAOImpl;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.dao.implement.CourseDAOImpl;
import ua.foxminded.schoolapp.dao.CourseDAO;

public class DatabaseTableInitializer {

    private SQLScriptsExecutorDAO executor = new SQLScriptsExecutorDAO();
    private GroupsGenerator groupsGenerator = new GroupsGenerator();
    private StudentsGenerator studentsGenerator = new StudentsGenerator();
    private CoursesGenerator courseGenerator = new CoursesGenerator();

    public void initialize() {
        executor.executeSqlScriptFrom("sql/tables_creation.sql");
        fillGroupsTable();
        fillStudentsTable();
        fillCoursesTable();
        executor.executeSqlScriptFrom("sql/students_courses_filling.sql");
    }

    private void fillGroupsTable() {
        GroupDAO groupDao = new GroupDAOImpl();
        List<Group> groups = groupsGenerator.getGroups();

        for (Group group : groups) {
            groupDao.save(group);
        }
    }

    private void fillStudentsTable() {
        StudentDAO studentDao = new StudentDAOImpl();
        List<Student> students = studentsGenerator.getStudents();

        for (Student student : students) {
            studentDao.save(student);
        }
    }

    private void fillCoursesTable() {
        CourseDAO courseDao = new CourseDAOImpl();
        List<Course> courses = courseGenerator.getCourses();

        for (Course course : courses) {
            courseDao.save(course);
        }
    }

}
