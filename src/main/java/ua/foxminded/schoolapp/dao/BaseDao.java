package ua.foxminded.schoolapp.dao;

public interface BaseDao<T> {

    int save(T entity);

}
