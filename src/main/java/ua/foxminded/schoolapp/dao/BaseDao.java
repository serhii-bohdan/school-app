package ua.foxminded.schoolapp.dao;

public interface DAO<T> {

    int save(T entity);
}
