package colesico.framework.telescheme;

/**
 * Base scheme builder API
 */
public interface TeleSchemeBuilder<S> {

    String SCHEME_BUILDER_SUFFIX = "Builder";
    String BUILD_METHOD = "build";

    /**
     * Build scheme
     */
    S build();
}
