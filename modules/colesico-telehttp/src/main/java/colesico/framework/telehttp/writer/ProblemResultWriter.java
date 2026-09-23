package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.result.ProblemHttpResult;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Singleton
public class ProblemResultWriter<R extends ProblemHttpResult<?>, O extends HttpWriteOptions>
        extends HttpResultWriter<R, O> {

    public ProblemResultWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected Integer status(R result, O options, Integer defaultStatus) {
        return super.status(result, options, 500);
    }

    @Override
    protected Integer emptyStatus(R result, Integer emptyStatus) {
        if (result == null || result.problemDetails() == null) {
            return 500;
        }
        return null;
    }

    /**
     * Default value serialization implementation with {@link Object#toString()}
     * Override this method to implement custom serialization
     */
    @Override
    protected void write(OutputStream outputStream, R result, O options) throws IOException {
        var contentType = contentType(result, options, ContentType.TEXT_PLAIN);
        outputStream.write(result.problemDetails().toString().getBytes(contentType.charset().orElse(StandardCharsets.UTF_8)));
    }
}
