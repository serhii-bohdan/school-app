package ua.foxminded.schoolapp.datageneration;

import java.util.List;
import ua.foxminded.schoolapp.dao.TablesSetupDAO;
import ua.foxminded.schoolapp.dao.GroupDAO;
import ua.foxminded.schoolapp.dao.StudentDAO;
import ua.foxminded.schoolapp.dao.CourseDAO;
import ua.foxminded.schoolapp.dto.Course;
import ua.foxminded.schoolapp.dto.Group;
import ua.foxminded.schoolapp.dto.Student;

public class TablesPlaceholder {

    private TablesSetupDAO setUp = new TablesSetupDAO();
    private GroupsGenerator groupsGenerator = new GroupsGenerator();
    private StudentsGenerator studGen = new StudentsGenerator();
    private CoursesGenerator courseGenerator = new CoursesGenerator();

    public void fillTables() {
        fillGroupsTable();
        fillStudentsTable();
        fillCoursesTable();
    }

    public void createTables() {
        setUp.createTables();
    }

    private void fillGroupsTable() {
        GroupDAO groupDao = new GroupDAO();
        List<Group> groups = groupsGenerator.getGroups();

        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            groupDao.saveGroup(group.getGroupId(), group.getName());
        }
    }

    private void fillStudentsTable() {
        StudentDAO studentDao = new StudentDAO();
        List<Student> students = studGen.getStudents();

        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            studentDao.saveStudent(student.getStudentId(), student.getGroupId(), student.getFirstName(),
                    student.getLastName());
        }
    }

    private void fillCoursesTable() {
        CourseDAO courseDao = new CourseDAO();
        List<Course> courses = courseGenerator.getCourses();

        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            courseDao.saveCourse(course.getCourseId(), course.getName(), course.getDescription());
        }
    }
}
