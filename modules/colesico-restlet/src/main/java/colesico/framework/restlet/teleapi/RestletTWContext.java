package colesico.framework.restlet.teleapi;

public final class RestletTWContext {

    /**
     * Custom writer class or null.
     * If null - default writer will be used
     */
    private final Class<? extends RestletTeleWriter> writerClass;

    public RestletTWContext(Class<? extends RestletTeleWriter> writerClass) {
        this.writerClass = writerClass;
    }

    public Class<? extends RestletTeleWriter> getWriterClass() {
        return writerClass;
    }
}
