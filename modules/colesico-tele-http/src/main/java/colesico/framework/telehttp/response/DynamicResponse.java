package colesico.framework.telehttp.response;

/**
 * Response wrapper. Used to  return dynamically produced responses of different types
 *
 * @param value Actual value
 */
public record DynamicResponse(Object value) {

    public static DynamicResponse of(Object response) {
        return new DynamicResponse(response);
    }

}
