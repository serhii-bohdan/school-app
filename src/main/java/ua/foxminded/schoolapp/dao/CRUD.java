package ua.foxminded.schoolapp.dao;

public interface CRUD<T> {

    int save(T entity);
}
