package colesico.framework.telehttp.writer;

import colesico.framework.assist.StringUtils;
import colesico.framework.http.HttpResponse;
import colesico.framework.teleapi.TeleError;
import colesico.framework.telehttp.*;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

/**
 * Default exception writer
 */
@Singleton
public class ExceptionWriter implements HttpWriter<Exception, HttpWriteOptions> {

    protected Provider<HttpResponse> httpResponse;

    public ExceptionWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    protected Integer errorStatus(Exception exception, HttpWriteOptions options) {

        if (exception instanceof HttpTeleError hte) {
            if (hte.status() != null) {
                return hte.status();
            }
        }

        if (options.status() != null) {
            return options.status();
        }

        return 500;
    }

    protected String errorData(Exception exception) {

        String data = "Exception: " + exception.getClass().getCanonicalName();
        if (!StringUtils.isBlank(exception.getMessage())) {
            data = data + "; message: " + exception.getMessage();
        }

        if (exception instanceof TeleError hte) {

            if (hte.errorData() != null) {
                data = data + "; details: " + hte.errorData().toString();
            }

        }

        return data;
    }

    @Override
    public void write(Exception exception, HttpWriteOptions options) {
        var httpResponse = this.httpResponse.get();

        if (httpResponse.isCommitted()) {
            throw new HttpTeleException("HTTP Response is committed while writing response");
        }

        if (exception == null) {
            httpResponse.setStatus(500).close();
            return;
        }

        httpResponse.setStatus(errorStatus(exception, options))
                .send(errorData(exception));

    }


}
