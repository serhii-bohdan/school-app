package ua.foxminded.schoolapp.dao.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import ua.foxminded.schoolapp.dao.CourseDao;
import ua.foxminded.schoolapp.model.Course;

/**
 * The JpaStudentDao class is an implementation of the {@link CourseDao}
 * interface. It provides methods for accessing and manipulating Student
 * entities in the database using {@link EntityManager}.
 * <p>
 * This class is annotated with {@link Repository}, marking it as a Spring
 * repository component, which enables automatic dependency injection and
 * exception translation for database access.
 * <p>
 * The JpaStudentDao uses the {@link EntityManager} to interact with the
 * database and provides methods to save, find, update, and delete Student
 * entities.
 * <p>
 * Note: This class should be used in conjunction with the Spring context to
 * configure the application and enable database access for Student entities.
 *
 * @author Serhii Bohdan
 */
@Repository
public class JpaCourseDao implements CourseDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Course save(Course course) {
        entityManager.persist(course);
        return course;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course find(Integer courseId) {
        return entityManager.find(Course.class, courseId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Course> findCourseByName(String courseName) {
        String jpql = "SELECT c FROM Course c WHERE c.courseName = :courseName";

        try {
            Course course = entityManager.createQuery(jpql, Course.class)
                    .setParameter("courseName", courseName)
                    .getSingleResult();
            return Optional.of(course);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Course> findAll() {
        String jpql = "SELECT c FROM Course c";

        return entityManager.createQuery(jpql, Course.class)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course update(Course course) {
        return entityManager.merge(course);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Integer courseId) {
        Course course = find(courseId);

        if (course != null) {
            entityManager.remove(course);
        }
    }

}
