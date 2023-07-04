package ua.foxminded.schoolapp.dao;

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

}
