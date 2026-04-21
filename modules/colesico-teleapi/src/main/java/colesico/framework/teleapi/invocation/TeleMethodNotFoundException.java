package colesico.framework.teleapi.invocation;

import colesico.framework.teleapi.TeleException;

public class TeleMethodNotFoundException extends TeleException {
    public TeleMethodNotFoundException(String message) {
        super(message);
    }
}
