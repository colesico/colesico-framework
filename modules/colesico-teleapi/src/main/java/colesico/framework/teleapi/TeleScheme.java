package colesico.framework.teleapi;

/**
 * Base tele-scheme API.
 * Tele-scheme is the scheme implementation builder.
 *
 * @param <S> scheme implementation type
 */
public interface TeleScheme<S> {

    String SCHEME_BUILDER_SUFFIX = "Scheme";
    String BUILD_METHOD = "build";

    /**
     * Build concrete scheme implementation
     */
    S build();
}
