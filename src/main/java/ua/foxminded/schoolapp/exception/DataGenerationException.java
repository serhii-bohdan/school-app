package ua.foxminded.schoolapp.exception;

/**
 * Exception thrown when there is an error during the data generation process.
 * Extends the {@link RuntimeException} class.
 *
 * @author Serhii Bohdan
 */
public class DataGenerationException extends RuntimeException {

    private static final long serialVersionUID = 903096812614037512L;

    /**
     * Constructs a new DataGenerationException with the specified error message.
     *
     * @param message the detail message
     */
    public DataGenerationException(String message) {
        super(message);
    }

}
