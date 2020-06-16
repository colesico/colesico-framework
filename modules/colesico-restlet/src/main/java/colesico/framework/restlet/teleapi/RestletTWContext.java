package colesico.framework.restlet.teleapi;

import colesico.framework.telehttp.HttpTWContext;

public final class RestletTWContext extends HttpTWContext {

    /**
     * Custom writer class or null.
     * If null - default writer will be used
     */
    private final Class<? extends RestletTeleWriter> writerClass;

    /**
     * Http code to return to client
     */
    private Integer httpCode;

    public RestletTWContext(Class<? extends RestletTeleWriter> writerClass) {
        this.writerClass = writerClass;
    }

    public RestletTWContext() {
        writerClass = null;
    }

    public Class<? extends RestletTeleWriter> getWriterClass() {
        return writerClass;
    }

    public Integer getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }
}
