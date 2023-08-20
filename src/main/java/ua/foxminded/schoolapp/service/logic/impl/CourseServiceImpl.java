package ua.foxminded.schoolapp.service.logic.impl;

import java.util.List;
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
        coursesGenerator.toGenerate().forEach(courseDao::save);
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
