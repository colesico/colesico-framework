package colesico.framework.example.restlet.customexception;

/**
 * Custom exception
 */
public class CustomException extends RuntimeException{
    private final Object payload;

    public CustomException(String message, Object payload) {
        super(message);
        this.payload = payload;
    }

    public Object getPayload() {
        return payload;
    }
}
