package colesico.framework.restlet.teleapi;

import colesico.framework.telehttp.HttpTRContext;

public final class RestletTRContext extends HttpTRContext {

    /**
     * Custom reader class or null.
     * If null - default reader will be used to read the parameter
     */
    private final Class<? extends RestletTeleReader> readerClass;

    public RestletTRContext(String name, String originName, Class<? extends RestletTeleReader> readerClass) {
        super(name, originName);
        this.readerClass = readerClass;
    }

    public RestletTRContext(String name, String originName) {
        super(name, originName);
        this.readerClass = null;
    }

    public RestletTRContext() {
        super(null, null);
        this.readerClass = null;
    }

    public Class<? extends RestletTeleReader> getReaderClass() {
        return readerClass;
    }

}
