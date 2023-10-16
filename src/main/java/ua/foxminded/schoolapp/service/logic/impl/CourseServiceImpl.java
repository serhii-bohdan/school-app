package ua.foxminded.schoolapp.service.logic.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import ua.foxminded.schoolapp.dao.CourseDao;
import ua.foxminded.schoolapp.dto.CourseDto;
import ua.foxminded.schoolapp.dto.mapper.CourseMapper;
import ua.foxminded.schoolapp.model.Course;
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
 * {@link Generatable<CourseDto>} object to generate courses Dto and a
 * {@link CourseDao} object to access the course data.
 * 
 * @author Serhii Bohdan
 */
@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    /**
     * The logger for logging events and messages in the {@link CourseServiceImpl}
     * class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final Generatable<CourseDto> coursesGenerator;
    private final CourseDao courseDao;

    /**
     * Constructs a new CourseServiceImpl with the specified courses generator and
     * course Dao.
     *
     * @param coursesGenerator the generator for creating courses
     * @param courseDao        the data access object for coursesl
     */
    public CourseServiceImpl(Generatable<CourseDto> coursesGenerator, CourseDao courseDao) {
        this.coursesGenerator = coursesGenerator;
        this.courseDao = courseDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initCourses() {
        LOGGER.info("Filling with generated courses");
        coursesGenerator.toGenerate().stream()
                .map(CourseMapper::mapDtoToCourse)
                .forEach(courseDao::save);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Course> addCourse(String courseName, String description) {
        Course newCourse = new Course(courseName, description);
        LOGGER.debug("Adding a new course: {}", newCourse);

        return Optional.ofNullable(courseDao.save(newCourse));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Course> getCourseById(Integer courseId) {
        Course course = courseDao.find(courseId);
        LOGGER.debug("Search course by ID {}: {}", courseId, course);

        return Optional.ofNullable(course);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Course> getCourseByName(String courseName) {
        Optional<Course> course = courseDao.findCourseByName(courseName);
        LOGGER.debug("Search course by name {}: {}", courseName, course);

        return course;
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
    public Optional<Course> updateCourse(Course updatedCourse) {
        Course course = courseDao.update(updatedCourse);
        LOGGER.debug("Updating course data: {}", course);

        return Optional.ofNullable(course);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCourseByName(String courseName) {
        Optional<Course> course = getCourseByName(courseName);

        if (course.isPresent()) {
            LOGGER.debug("Deleting course: {}", course);
            courseDao.delete(course.get().getId());
        }
    }

}
