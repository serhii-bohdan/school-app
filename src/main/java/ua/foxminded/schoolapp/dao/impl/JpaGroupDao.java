package ua.foxminded.schoolapp.dao.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import ua.foxminded.schoolapp.dao.GroupDao;
import ua.foxminded.schoolapp.model.Group;

/**
 * The JpaGroupDao class is an implementation of the {@link GroupDao} interface.
 * It provides methods for accessing and manipulating Group entities in the
 * database.
 * <p>
 * This class is annotated with {@link Repository}, marking it as a Spring
 * repository component, which enables automatic dependency injection and
 * exception translation for database access.
 * <p>
 * The JpaGroupDao uses the {@link EntityManager} to interact with the database
 * and provides methods to save, find, update, and delete Group entities.
 * <p>
 * Note: This class should be used in conjunction with the Spring context to
 * configure the application and enable database access for Group entities.
 *
 * @author Serhii Bohdan
 */
@Repository
public class JpaGroupDao implements GroupDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Group save(Group group) {
        entityManager.persist(group);
        return group;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Group find(Integer groupId) {
        return entityManager.find(Group.class, groupId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Group> findGroupByName(String groupName) {
        String jpql = "SELECT g FROM Group g WHERE g.groupName = :groupName";

        try {
            Group group = entityManager.createQuery(jpql, Group.class)
                    .setParameter("groupName", groupName)
                    .getSingleResult();
            return Optional.of(group);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> findAll() {
        String jpql = "SELECT g FROM Group g";

        return entityManager.createQuery(jpql, Group.class)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Group update(Group group) {
        return entityManager.merge(group);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Integer groupId) {
        Group group = find(groupId);

        if (group != null) {
            entityManager.remove(group);
        }
    }

}
