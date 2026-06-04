package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.HttpTeleWriter;
import colesico.framework.telehttp.HttpWriteOptions;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
public class ExceptionWriter implements HttpTeleWriter<Exception, HttpWriteOptions> {

    private final Provider<HttpResponse> httpResponse;

    public ExceptionWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    @Override
    public void write(Exception value, Class<Exception> valueType, HttpWriteOptions options) {
        httpResponse.get()
                .setContentType("text/plain")
                .setStatus(500)
                .sendText("Server error");
    }
}