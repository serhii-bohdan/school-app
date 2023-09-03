package ua.foxminded.schoolapp.service.logic.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.foxminded.schoolapp.dao.CourseDao;
import ua.foxminded.schoolapp.model.Course;
import ua.foxminded.schoolapp.model.Student;
import ua.foxminded.schoolapp.service.generate.Generatable;
import ua.foxminded.schoolapp.service.logic.CourseService;

/**
 * The CourseServiceImpl class is an implementation of the {@link CourseService}
 * interface. It provides operations for managing courses, including
 * initializing courses with a generator, retrieving all courses, and getting
 * courses associated with a specific student.
 * <p>
 * This class is annotated with {@code @Service} to indicate that it is a Spring
 * service, and it can be automatically discovered and registered as a bean in
 * the Spring context. The CourseServiceImpl requires a
 * {@link Generatable<Course>} object to generate courses and a
 * {@link CourseDao} object to access the course data.
 * 
 * @author Serhii Bohdan
 */
@Service
public class CourseServiceImpl implements CourseService {

    /**
     * The logger for logging events and messages in the {@link CourseServiceImpl}
     * class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final Generatable<Course> coursesGenerator;
    private final CourseDao courseDao;

    /**
     * Constructs a new CourseServiceImpl with the specified courses generator and
     * course Dao.
     *
     * @param coursesGenerator the generator for creating courses
     * @param courseDao        the data access object for coursesl
     */
    public CourseServiceImpl(Generatable<Course> coursesGenerator, CourseDao courseDao) {
        this.coursesGenerator = coursesGenerator;
        this.courseDao = courseDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initCourses() {
        LOGGER.info("Filling with generated courses");
        coursesGenerator.toGenerate().forEach(courseDao::save);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Course> getAllCourses() {
        List<Course> allCourses = courseDao.findAll();
        LOGGER.debug("All received courses: {}", allCourses);

        return allCourses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Course> getCoursesForStudent(Student student) {
        List<Course> coursesForStudent = courseDao.findCoursesForStudent(student);
        LOGGER.debug("Received courses for the student {} ==> {}", student, coursesForStudent);

        return coursesForStudent;
    }

}
