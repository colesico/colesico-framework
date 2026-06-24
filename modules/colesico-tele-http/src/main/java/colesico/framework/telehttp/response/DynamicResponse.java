package colesico.framework.telehttp.response;

/**
 * Response wrapper. Used to  return dynamically produced responses of different types
 */
public final class DynamicResponse {

    /**
     * Actual response
     */
    private final Object response;

    private DynamicResponse(Object response) {
        this.response = response;
    }

    /**
     * Return actual response
     */
    public Object unwrap() {
        return response;
    }

    public static DynamicResponse of(Object response) {
        return new DynamicResponse(response);
    }

}
