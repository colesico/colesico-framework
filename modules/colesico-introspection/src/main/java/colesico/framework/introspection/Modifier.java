package colesico.framework.introspection;

/**
 * Introspected element modifier
 */
public enum Modifier {
    PUBLIC,
    PROTECTED,
    PRIVATE,
    ABSTRACT,
    DEFAULT,
    STATIC,
    FINAL,
    TRANSIENT,
    VOLATILE,
    SYNCHRONIZED,
    NATIVE,
    STRICTFP;

    private Modifier() {
    }

    public String toString() {
        return this.name().toLowerCase();
    }
}

