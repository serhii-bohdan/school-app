package ua.foxminded.schoolapp.exception;

/**
 * Exception thrown when there is an error reading a file.
 * Extends the {@link RuntimeException} class.
 *
 * @author Serhii Bohdan
 */
public class FileReadingException extends RuntimeException {

    private static final long serialVersionUID = 9139393289623292368L;

    /**
     * Constructs a new FileReadingException with the specified error message.
     *
     * @param message the detail message
     */
    public FileReadingException(String message) {
        super(message);
    }

}
