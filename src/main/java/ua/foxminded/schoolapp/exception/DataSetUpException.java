package ua.foxminded.schoolapp.exception;

/**
 * Exception thrown when there is an error during the data setup process.
 * Extends the {@link RuntimeException} class.
 *
 * @author Serhii Bohdan
 */
public class DataSetUpException extends RuntimeException {

    private static final long serialVersionUID = 903096812614037512L;

    /**
     * Constructs a new DataSetUpException with the specified error message.
     *
     * @param message the detail message
     */
    public DataSetUpException(String message) {
        super(message);
    }

}
