package ua.foxminded.schoolapp.dao;

/**
 * The ExecutorDao interface defines the contract for executing SQL scripts.
 *
 * @author Serhii Bohdan
 */
public interface ExecutorDao {

    /**
     * Executes the provided SQL script.
     *
     * @param sqlScript the SQL script to execute
     */
    void executeSqlScript(String sqlScript);

}
