package colesico.framework.telehttp.response;

/**
 * Response wrapper. Used to  return dynamically produced responses of different types
 */
public record DynamicResponse(Object actualResponse) {

    public static DynamicResponse of(Object response) {
        return new DynamicResponse(response);
    }

}
