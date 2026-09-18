package colesico.framework.restlet.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.restlet.JsonSerializer;
import colesico.framework.restlet.RestletWriter;
import colesico.framework.restlet.RestletWriteOptions;
import colesico.framework.security.authentication.UnauthenticatedException;
import colesico.framework.security.authorization.UnauthorizedException;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.HttpTeleError;
import colesico.framework.telehttp.response.ExceptionResponse;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
public class JsonExceptionResponseWriter
        extends colesico.framework.telehttp.writer.ExceptionResponseWriter<RestletWriteOptions>
        implements RestletWriter<ExceptionResponse> {

    private final JsonSerializer serializer;

    public JsonExceptionResponseWriter(Provider<HttpResponse> httpResponse, JsonSerializer serializer) {
        super(httpResponse);
        this.serializer = serializer;
    }

    @Override
    protected Integer statusCode(ExceptionResponse response, RestletWriteOptions options, Integer defaultValue) {
        return super.statusCode(response, options, defaultValue);
    }

    @Override
    protected ContentType contentType(ExceptionResponse response, RestletWriteOptions options, ContentType defaultValue) {
        return super.contentType(response, options, defaultValue);
    }

    @Override
    protected String errorData(ExceptionResponse response) {
        var exception = response.value();
        return switch (exception) {
            case UnauthenticatedException e -> "Unauthenticated";
            case UnauthorizedException e -> "Unauthorized";
            case HttpTeleError e -> {
                if (e.details() != null) {
                    yield serializer.serialize(e.details(), e.details().getClass());
                }
                yield "Error";
            }
            default -> "Error";
        };
    }
}
