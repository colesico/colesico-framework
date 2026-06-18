package colesico.framework.restlet.teleapi.writer;

import colesico.framework.http.HttpContext;
import colesico.framework.restlet.RestletError;
import colesico.framework.restlet.teleapi.RestletJsonConverter;
import colesico.framework.restlet.teleapi.RestletWriteOptions;
import colesico.framework.restlet.teleapi.RestletTeleWriter;
import jakarta.inject.Provider;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Exception writer helper
 */
abstract public class AbstractExceptionWriter<T extends Throwable> implements RestletTeleWriter<T> {

    public static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";

    private final Provider<HttpContext> httpContext;
    private final RestletJsonConverter jsonConverter;

    public AbstractExceptionWriter(Provider<HttpContext> httpContext, RestletJsonConverter jsonConverter) {
        this.httpContext = httpContext;
        this.jsonConverter = jsonConverter;
    }

    abstract protected Object getDetails(T value, RestletWriteOptions options);

    /**
     * HTTP response status code to  sent to  client
     */
    protected int getHttpStatus(T value, RestletWriteOptions options) {
        return 500;
    }

    protected String getErrorCode(T value, RestletWriteOptions options) {
        return value.getClass().getCanonicalName();
    }

    protected String getMessage(T value, RestletWriteOptions options) {
        return value.getMessage();
    }

    @Override
    public void write(T value, Class<T> valueType, RestletWriteOptions options) {
        RestletError error = new RestletError();
        error.setErrorCode(getErrorCode(value, options));
        error.setMessage(getMessage(value, options));
        error.setDetails(getDetails(value, options));

        var response = httpContext.get().response();

        String json = jsonConverter.toJson(value);
        response.setContentType(JSON_CONTENT_TYPE)
                .setStatus(getHttpStatus(value, options))
                .sendData(ByteBuffer.wrap(json.getBytes(StandardCharsets.UTF_8)));
    }
}
