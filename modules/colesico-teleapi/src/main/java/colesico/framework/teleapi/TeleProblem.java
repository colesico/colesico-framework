package colesico.framework.teleapi;

/**
 * Represents an error contract for exceptions that can be returned to a client
 * during Tele API calls.
 *
 * <p>Implementations of this interface provide detailed, serializable context
 * about the error to help the client understand and handle the failure.</p>
 */
public interface TeleProblem<D> {

    /**
     * Retrieves additional, serializable details about the error.
     *
     * <p>The returned object should contain structured information (such as
     * error codes, field validation messages, or localized descriptions)
     * that is safe and ready to be serialized and sent across the network.</p>
     *
     * @return a serializable {@link Object} containing specific error data,
     * or {@code null} if no additional data are available
     */
    D problemDetails();

    /**
     * Default problem details
     */
    record ProblemDetails(String type, String message) {
        public static ProblemDetails of(String type, String message) {
            return new ProblemDetails(type, message);
        }

        public static ProblemDetails of(Class clazz, String message) {
            return new ProblemDetails(clazz.getName(), message);
        }

        public static ProblemDetails of(Class clazz) {
            return new ProblemDetails(clazz.getName(), null);
        }

        public static ProblemDetails of(Exception exception) {
            return new ProblemDetails(exception.getClass().getName(), exception.getMessage());
        }
    }
}