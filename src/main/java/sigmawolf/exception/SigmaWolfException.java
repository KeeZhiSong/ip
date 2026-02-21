package sigmawolf.exception;

/**
 * Represents exceptions specific to SigmaWolf application.
 */
public class SigmaWolfException extends Exception {
    /**
     * Creates a new SigmaWolfException with the specified error message.
     *
     * @param message The error message.
     */
    public SigmaWolfException(String message) {
        super(message);
    }
}
