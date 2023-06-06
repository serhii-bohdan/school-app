package ua.foxminded.schoolapp.datasetup.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Random;
import ua.foxminded.schoolapp.dao.Connectable;
import ua.foxminded.schoolapp.dao.CourseDao;
import ua.foxminded.schoolapp.dao.GroupDao;
import ua.foxminded.schoolapp.dao.StudentDao;
import ua.foxminded.schoolapp.dao.impl.CourseDaoImpl;
import ua.foxminded.schoolapp.dao.impl.GroupDaoImpl;
import ua.foxminded.schoolapp.dao.impl.StudentDaoImpl;
import ua.foxminded.schoolapp.datasetup.Generatable;
import ua.foxminded.schoolapp.datasetup.Initializable;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Group;
import ua.foxminded.schoolapp.model.Student;

public class DatabaseTableInitializer implements Initializable {

    private final Connectable connector;
    private final Generatable<Group> groupsGenerator;
    private final Generatable<Student> studentsGenerator;
    private final Generatable<Course> coursesGenerator;

    public DatabaseTableInitializer(Connectable connector, Generatable<Group> groupsGenerator,
            Generatable<Student> studentsGenerator, Generatable<Course> coursesGenerator) {
        Objects.requireNonNull(connector);
        Objects.requireNonNull(groupsGenerator);
        Objects.requireNonNull(studentsGenerator);
        Objects.requireNonNull(coursesGenerator);
        this.connector = connector;
        this.groupsGenerator = groupsGenerator;
        this.studentsGenerator = studentsGenerator;
        this.coursesGenerator = coursesGenerator;
    }

    public void initialize() {
        fillGroupsTable();
        fillStudentsTable();
        fillCoursesTable();
        fillStudentsCoursesTable();
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

    private void fillStudentsCoursesTable() {
        StudentDao studentDao = new StudentDaoImpl(connector);
        int studentsNumberPresentInDatabase = studentDao.findAllStudents().size();
        Random random = new Random();

        for (int studentId = 1; studentId <= studentsNumberPresentInDatabase; studentId++) {
            int coursesNumberForStudent = random.nextInt(3) + 1;
            Set<Integer> coursesForStudent = new HashSet<>();

            while (coursesForStudent.size() < coursesNumberForStudent) {
                int courseId = random.nextInt(1, 11);
                coursesForStudent.add(courseId);
            }

            for (Integer courseId : coursesForStudent) {
                studentDao.addStudentToCourse(studentId, courseId);
            }
        }
    }

}
