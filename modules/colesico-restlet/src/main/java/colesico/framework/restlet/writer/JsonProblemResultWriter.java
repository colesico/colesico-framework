package colesico.framework.restlet.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.restlet.JsonSerializer;
import colesico.framework.restlet.RestletWriteOptions;
import colesico.framework.restlet.RestletWriter;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.result.ProblemHttpResult;
import colesico.framework.telehttp.writer.ProblemResultWriter;
import jakarta.inject.Provider;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class JsonProblemResultWriter
        extends ProblemResultWriter<ProblemHttpResult<?>, RestletWriteOptions>
        implements RestletWriter<ProblemHttpResult<?>> {

    protected final JsonSerializer serializer;

    public JsonProblemResultWriter(Provider<HttpResponse> httpResponse, JsonSerializer serializer) {
        super(httpResponse);
        this.serializer = serializer;
    }

    @Override
    protected ContentType contentType(ProblemHttpResult<?> result, RestletWriteOptions options, ContentType defaultContentType) {
        return super.contentType(result, options, ContentType.APPLICATION_JSON);
    }

    @Override
    protected void write(OutputStream outputStream, ProblemHttpResult<?> result, RestletWriteOptions options) throws IOException {
        var contentType = contentType(result, options, null);
        serializer.serialize(result.problemDetails(),
                options.baseType(),
                contentType.charset().orElse(StandardCharsets.UTF_8),
                outputStream);
    }
}
