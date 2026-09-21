package colesico.framework.telehttp;

import colesico.framework.teleapi.TeleError;

/**
 * Representing errors that occur
 * over HTTP communication within the Tele API.
 *
 * <p>This interface provides a unified contract for HTTP-specific error responses,
 * combining an HTTP status code and a serializable
 * error data payload.</p>
 */
public interface HttpTeleError extends TeleError {

    /**
     * Returns the HTTP response status code associated with this error.
     *
     * <p>This code should correspond to standard HTTP status codes (e.g., 400 for
     * Bad Request, 404 for Not Found, 500 for Internal Server Error) that are
     * returned to the client in the HTTP header.</p>
     *
     * @return the HTTP status code as an {@link Integer}
     */
    Integer status();
}
