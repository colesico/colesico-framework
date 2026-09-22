package colesico.framework.telehttp.writer;

import colesico.framework.assist.StringUtils;
import colesico.framework.http.HttpResponse;
import colesico.framework.teleapi.TeleProblem;
import colesico.framework.telehttp.*;
import colesico.framework.telehttp.result.ValueResult;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Default exception writer
 */
@Singleton
public class ExceptionWriter<E extends Exception, O extends HttpWriteOptions>
        extends HttpResultWriter<E, O> {

    public ExceptionWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

      @Override
    protected Integer status(E exception, O options, Integer defaultStatus) {
        return super.status(exception, options, 500);
    }

    protected Integer emptyResult(E exception, Integer emptyStatus) {
        if (result instanceof ValueResult<?> vr) {
            return vr.value() == null ? emptyStatus : null;
        }
        return result == null ? emptyStatus : null;
    }

    protected String errorData(E exception, O options) {

        String data = "Exception: " + exception.getClass().getCanonicalName();
        if (!StringUtils.isBlank(exception.getMessage())) {
            data = data + "; message: " + exception.getMessage();
        }

        if (exception instanceof TeleProblem hte) {
            if (hte.problemDetail() != null) {
                data = data + "; details: " + hte.problemDetail().toString();
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
