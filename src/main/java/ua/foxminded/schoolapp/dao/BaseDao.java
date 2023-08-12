package ua.foxminded.schoolapp.dao;

import java.util.List;

/**
 * The BaseDao interface defines the common operations for data access objects.
 *
 * @param <T> the type of the entity
 * @author Serhii Bohdan
 */
public interface BaseDao<T> {

    /**
     * Saves the entity to the database and returns the number of affected rows.
     *
     * @param entity the entity to save
     * @return the number of affected rows
     */
    int save(T entity);

    /**
     * Finds an entity in the database by its ID.
     *
     * @param entityId the ID of the entity to find
     * @return the found entity, or null if not found
     */
    T find(int entityId);

    /**
     * Updates the entity in the database and returns the number of affected rows.
     *
     * @param entity the entity to update
     * @return the number of affected rows
     */
    int update(T entity);

    /**
     * Deletes the entity from the database and returns the number of affected rows.
     *
     * @param entityId the ID of the entity to delete
     * @return the number of affected rows
     */
    int delete(int entityId);

    /**
     * Retrieves all entities of the specified type from the database.
     *
     * @return a list of all entities
     */
    List<T> findAll();

}
