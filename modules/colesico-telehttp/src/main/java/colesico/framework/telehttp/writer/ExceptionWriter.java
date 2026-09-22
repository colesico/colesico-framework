package colesico.framework.telehttp.writer;

import colesico.framework.assist.StringUtils;
import colesico.framework.http.HttpResponse;
import colesico.framework.teleapi.TeleError;
import colesico.framework.telehttp.*;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Default exception writer
 */
@Singleton
public class ExceptionWriter<E extends Exception, O extends HttpWriteOptions>
        extends AbstractHttpWriter<E, O> {

    public ExceptionWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

      @Override
    protected Integer status(E exception, O options, Integer defaultStatus) {
        return super.status(exception, options, 500);
    }


    protected String errorData(E exception, O options) {

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

    @Override
    protected ContentType contentType(E exception, O options, ContentType defaultContentType) {
        return ContentType.TEXT_PLAIN;
    }

    @Override
    protected void write(OutputStream outputStream, E exception, O options) throws IOException {

    }


}
