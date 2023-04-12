package ua.foxminded.schoolapp.dao;

public interface CrudDAO<T> {

    void save(T entity);
}
