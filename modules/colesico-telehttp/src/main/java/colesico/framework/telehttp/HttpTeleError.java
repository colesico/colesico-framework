package colesico.framework.telehttp;

/**
 * General tele http error api.
 * Provide  http status code, details payload object, error code
 */
public interface HttpTeleError {

    /**
     * Http response status code
     */
    Integer statusCode();

    /**
     * Error code to identify error
     */
    String errorCode();

    /**
     * Exception details object
     */
    Object details();

}
