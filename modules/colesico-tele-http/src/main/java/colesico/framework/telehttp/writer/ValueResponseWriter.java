package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;

import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.telehttp.MediaType;
import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.response.ValueResponse;
import jakarta.inject.Provider;

/**
 * General {@link ValueSerializer} based object writer.
 * Appropriate serializer is selected based on the {@link MediaType#mimeType()} type.
 */
abstract public class ValueResponseWriter<R extends ValueResponse<?>, O extends TeleHttpWriteOptions>
        extends TeleHttpResponseWriter<R, O> {


    public ValueResponseWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected boolean isEmptyResponse(R response) {
        return super.isEmptyResponse(response) || (response.value() == null);
    }
}
