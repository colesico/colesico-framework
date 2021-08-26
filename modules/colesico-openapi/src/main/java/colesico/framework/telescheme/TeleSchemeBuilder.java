package colesico.framework.telescheme;

/**
 * Base scheme class
 */
abstract public class TeleSchemeBuilder<S> {

    public static final String SCHEME_BUILDER_SUFFIX = "Builder";
    public static final String BUILD_METHOD = "build";

    /**
     * Build scheme
     */
    abstract public S build();
}
