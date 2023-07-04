package ua.foxminded.schoolapp.exception;

/**
 * A custom exception class for representing errors that occur in the DAO layer.
 * Extends the {@link RuntimeException} class.
 *
 * @author Serhii Bohdan
 */
public class DaoException extends RuntimeException {

    private static final long serialVersionUID = 1333831680412199304L;

    /**
     * Constructs a new DaoException with the specified error message.
     *
     * @param message the error message
     */
    public DaoException(String message) {
        super(message);
    }

}
