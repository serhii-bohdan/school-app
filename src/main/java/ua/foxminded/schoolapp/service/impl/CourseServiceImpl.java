package ua.foxminded.schoolapp.service.impl;

import java.util.List;
import java.util.Objects;
import ua.foxminded.schoolapp.dao.CourseDao;
import ua.foxminded.schoolapp.datasetup.Generatable;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.CourseService;

/**
 * The CourseServiceImpl class is an implementation of the {@link CourseService}
 * interface. It provides operations for managing courses.
 *
 * @author Serhii Bohdan
 */
public class CourseServiceImpl implements CourseService {

    private Generatable<Course> coursesGenerator;
    private CourseDao courseDao;

    /**
     * Constructs a new CourseServiceImpl with the specified courses generator and
     * course Dao.
     *
     * @param coursesGenerator the generator for creating courses
     * @param courseDao        the data access object for courses
     * @throws NullPointerException if either coursesGenerator or courseDao is null
     */
    public CourseServiceImpl(Generatable<Course> coursesGenerator, CourseDao courseDao) {
        Objects.requireNonNull(coursesGenerator, "coursesGenerator must not be null");
        Objects.requireNonNull(courseDao, "courseDao must not be null");
        this.coursesGenerator = coursesGenerator;
        this.courseDao = courseDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initCourses() {
        coursesGenerator.toGenerate().stream()
                                     .forEach(courseDao::save);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Course> getAllCourses() {
        return courseDao.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Course> getCoursesForStudent(Student student) {
        return courseDao.findCoursesForStudent(student);
    }

}
