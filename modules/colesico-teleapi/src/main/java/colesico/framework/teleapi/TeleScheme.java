package colesico.framework.teleapi;

/**
 * Tele-scheme is the API scheme implementation builder.
 * Tele-schema is an API formalized description of invocation rules,
 * data structures, endpoints. Examples of an API schema are OpenApi,
 * GRPC IDL, and others. A tele-schema implements the API schema model in Java classes.
 *
 * @param <S> scheme implementation type
 */
public interface TeleScheme<S> {

    String SCHEME_IMPL_SUFFIX = "Scheme";
    String BUILD_METHOD = "build";

    /**
     * Build concrete scheme implementation
     */
    S build();
}
