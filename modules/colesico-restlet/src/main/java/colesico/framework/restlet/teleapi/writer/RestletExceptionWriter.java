package colesico.framework.restlet.teleapi.writer;

import colesico.framework.http.HttpContext;
import colesico.framework.restlet.RestletException;
import colesico.framework.restlet.teleapi.RestletWriteOptions;
import colesico.framework.restlet.teleapi.RestletTeleWriter;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
public class RestletExceptionWriter implements RestletTeleWriter<RestletException> {

    private final RestletResponseWriter writer;

    public RestletExceptionWriter(Provider<HttpContext> httpContextProv, RestletResponseWriter writer) {
        super(httpContextProv);
        this.writer = writer;
    }

    @Override
    public void write(RestletException value, Class<RestletException> valueType, RestletWriteOptions options) {
        if (options.statusCode() == null) {
            options.setStatusCode(value.getHttpStatus());
        }
        writer.write(value.getError(), context);
    }
}
