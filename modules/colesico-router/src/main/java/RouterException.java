import colesico.framework.teleapi.TeleException;

public class RouterException extends TeleException {
    public RouterException(String message) {
        super(message);
    }

    public RouterException(String message, Throwable cause) {
        super(message, cause);
    }

    public RouterException(Throwable cause) {
        super(cause);
    }
}
