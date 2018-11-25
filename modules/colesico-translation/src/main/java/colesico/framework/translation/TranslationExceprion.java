package colesico.framework.translation;

public class TranslationExceprion extends RuntimeException {

    public TranslationExceprion(String message) {
        super(message);
    }

    public TranslationExceprion(String message, Throwable cause) {
        super(message, cause);
    }

    public TranslationExceprion(Throwable cause) {
        super(cause);
    }

    public TranslationExceprion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
