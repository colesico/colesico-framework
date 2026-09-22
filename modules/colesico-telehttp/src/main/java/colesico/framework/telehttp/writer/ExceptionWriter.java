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
public class ExceptionWriter<O extends HttpWriteOptions> implements HttpWriter<Exception, O> {

    protected Provider<HttpResponse> httpResponse;

    public ExceptionWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    protected Integer status(Exception exception, O options, Integer defaultStatus) {

        if (exception instanceof HttpTeleError hte) {
            if (hte.status() != null) {
                return hte.status();
            }
        }

        if (options.status() != null) {
            return options.status();
        }

        return defaultStatus;
    }

    protected String errorData(Exception exception, O options) {

        String data = "Exception: " + exception.getClass().getCanonicalName();
        if (!StringUtils.isBlank(exception.getMessage())) {
            data = data + "; message: " + exception.getMessage();
        }

        if (exception instanceof TeleError hte) {
            if (hte.errorDetails() != null) {
                data = data + "; details: " + hte.errorDetails().toString();
            }
        }

        return data;
    }

    protected ContentType contentType(Exception exception, O options, ContentType defaultContentType){
        return ContentType.TEXT_PLAIN;
    }

    @Override
    public void write(Exception exception, O options) {
        var httpResponse = this.httpResponse.get();

        if (httpResponse.isCommitted()) {
            throw new HttpTeleException("HTTP Response is committed while writing response");
        }

        if (exception == null) {
            httpResponse.setStatus(500).close();
            return;
        }

        httpResponse.setStatus(status(exception, options, 500))
                .setContentType(contentType(exception,options,ContentType.TEXT_PLAIN).mimeType())
                .send(errorData(exception, options));

    }


}
