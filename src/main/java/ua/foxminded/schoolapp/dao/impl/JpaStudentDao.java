package ua.foxminded.schoolapp.dao.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import ua.foxminded.schoolapp.dao.StudentDao;
import ua.foxminded.schoolapp.model.Student;

/**
 * The JpaStudentDao class is an implementation of the {@link StudentDao}
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
public class JpaStudentDao implements StudentDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Student save(Student student) {
        entityManager.persist(student);
        return student;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Student find(Integer studentId) {
        return entityManager.find(Student.class, studentId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Student> findStudentByFullName(String firstName, String lastName) {
        String jpql = "SELECT s FROM Student s WHERE s.firstName = :firstName AND s.lastName = :lastName";

        try {
            Student student = entityManager.createQuery(jpql, Student.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName).getSingleResult();
            return Optional.of(student);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Student> findAll() {
        String jpql = "SELECT s FROM Student s";

        return entityManager.createQuery(jpql, Student.class)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Student update(Student student) {
        return entityManager.merge(student);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Integer studentId) {
        Student student = find(studentId);

        if (student != null) {
            entityManager.remove(student);
        }
    }

}
