package colesico.framework.example.restlet.customerror;

import colesico.framework.http.HttpResponse;
import colesico.framework.restlet.RestletWriteOptions;
import colesico.framework.restlet.RestletWriter;

import colesico.framework.telehttp.ContentType;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

/**
 * Writer to  send custom exception to  client
 */
@Singleton
public class CustomExceptionWriter implements RestletWriter<CustomException> {

    private final Provider<HttpResponse> httpResponse;

    public CustomExceptionWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    @Override
    public void write(CustomException value, RestletWriteOptions options) {
        var httpResponse = this.httpResponse.get();

        httpResponse
                .setStatus(520)
                .setContentType(ContentType.TEXT_PLAIN.headerValue())
                .send(value.payload().toString());
    }
}
