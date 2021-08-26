package colesico.framework.telescheme;

abstract public class TeleScheme<S> {

    public static final String TELE_SCHEME_SUFFIX = "Scheme";
    public static final String BUILD_METHOD = "build";

    /**
     * Build scheme
     */
    abstract public S build();
}
