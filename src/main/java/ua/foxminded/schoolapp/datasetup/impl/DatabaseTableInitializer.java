package ua.foxminded.schoolapp.datasetup.impl;

import java.util.HashSet;
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

/**
 * The DatabaseTableInitializer class is responsible for initializing the
 * database tables by populating them with groups, students, courses, and their
 * relationships. DatabaseTableInitializer implements {@link Initializable}
 * interface.
 *
 * @author Serhii Bohdan
 */
public class DatabaseTableInitializer implements Initializable {

    private Connectable connector;
    private Generatable<Group> groupsGenerator;
    private Generatable<Student> studentsGenerator;
    private Generatable<Course> coursesGenerator;

    /**
     * Constructs a new DatabaseTableInitializer with the specified dependencies.
     *
     * @param connector         the database connector
     * @param groupsGenerator   the generator for creating groups
     * @param studentsGenerator the generator for creating students
     * @param coursesGenerator  the generator for creating courses
     */
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

    /**
     * Initializes the database tables by populating them with groups, students,
     * courses, and their relationships.
     */
    @Override
    public void initialize() {
        fillGroupsTable();
        fillStudentsTable();
        fillCoursesTable();
        fillStudentsCoursesTable();
    }

    /**
     * Fills the groups table in the database with the generated groups.
     */
    private void fillGroupsTable() {
        GroupDao groupDao = new GroupDaoImpl(connector);
        groupsGenerator.toGenerate().stream()
                                    .forEach(groupDao::save);
    }

    /**
     * Fills the students table in the database with the generated students.
     */
    private void fillStudentsTable() {
        StudentDao studentDao = new StudentDaoImpl(connector);
        studentsGenerator.toGenerate().stream()
                                      .forEach(studentDao::save);
    }

    /**
     * Fills the courses table in the database with the generated courses.
     */
    private void fillCoursesTable() {
        CourseDao courseDao = new CourseDaoImpl(connector);
        coursesGenerator.toGenerate().stream()
                                     .forEach(courseDao::save);
    }

    /**
     * Fills the students_courses table in the database with the relationships
     * between students and courses.
     */
    private void fillStudentsCoursesTable() {
        Random random = new Random();
        StudentDao studentDao = new StudentDaoImpl(connector);
        int studentsNumberPresentInDatabase = studentDao.findAllStudents().size();

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
