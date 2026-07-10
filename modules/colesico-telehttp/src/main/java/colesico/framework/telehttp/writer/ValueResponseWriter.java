package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;

import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.response.ValueResponse;
import jakarta.inject.Provider;

/**
 * General {@link ValueResponse}  writer.
 */
abstract public class ValueResponseWriter<V extends ValueResponse<?>, O extends HttpWriteOptions>
        extends TeleHttpResponseWriter<V, O> {


    public ValueResponseWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected boolean isEmptyResponse(V response) {
        return super.isEmptyResponse(response) || (response.value() == null);
    }
}
