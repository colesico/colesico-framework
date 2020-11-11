package colesico.framework.weblet;

/**
 * Response wrapper. Used to return dynamically produced responses of different types
 */
public final class WebletResponse {

    /**
     * Actual response
     */
    private final Object response;

    private WebletResponse(Object response) {
        this.response = response;
    }

    /**
     * Return actual response
     */
    public Object unwrap() {
        return response;
    }

    public static WebletResponse of(Object response) {
        return new WebletResponse(response);
    }

}
