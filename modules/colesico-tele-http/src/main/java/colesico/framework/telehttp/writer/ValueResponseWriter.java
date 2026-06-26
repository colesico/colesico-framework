package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.ioc.message.IocMessage;
import colesico.framework.ioc.production.Supplier;

import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.telehttp.MediaType;
import colesico.framework.telehttp.TeleHttpException;
import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.response.ValueResponse;
import jakarta.inject.Provider;

import java.io.OutputStream;

/**
 * General {@link ValueSerializer} based object writer.
 * Appropriate serializer is selected based on the {@link MediaType#mimeType()} type.
 */
@Unscoped
public class ValueResponseWriter<R extends ValueResponse<?>, O extends TeleHttpWriteOptions>
        extends TeleHttpResponseWriter<R, O> {

    protected final Supplier<ValueSerializer> serializerFactory;
    protected final WriterOptions writerOptions;

    public ValueResponseWriter(Provider<HttpResponse> httpResponse,
                               Supplier<ValueSerializer> serializerFactory,
                               @IocMessage WriterOptions writerOptions) {

        super(httpResponse);
        this.serializerFactory = serializerFactory;
        this.writerOptions = writerOptions;
    }

    @Override
    protected MediaType defaultMediaType() {
        return writerOptions.defaultMediaType;
    }

    @Override
    protected Integer defaultStatusCode() {
        return writerOptions.defaultStatusCode;
    }

    @Override
    protected Integer emptyStatusCode() {
        return writerOptions.emptyStatusCode;
    }

    protected ValueSerializer serializer(String mimeType) {
        return serializerFactory.get(mimeType);
    }

    @Override
    protected void writeResponse(HttpResponse protocol,
                                 R response,
                                 O options,
                                 Integer statusCode,
                                 MediaType mediaType) {

        if (response.value() == null) {
            protocol.setStatus(emptyStatusCode()).close();
            return;
        }

        var serializer = serializer(mediaType.mimeType());

        protocol.setStatus(statusCode).setContentType(toContentType(mediaType));

        try (OutputStream os = protocol.outputStream()) {
            serializer.serialize(response.value(), mediaType.parameters(), os);
            os.flush();
        } catch (Exception e) {
            throw TeleHttpException.of(e, 500);
        }
    }

    /**
     * Writer options
     *
     * @param defaultStatusCode default status code
     * @param emptyStatusCode   empty result status code
     * @param defaultMediaType  default media type
     */
    public record WriterOptions(
            Integer defaultStatusCode,
            Integer emptyStatusCode,
            MediaType defaultMediaType
    ) {
        public static WriterOptions of(Integer statusCode, MediaType mediaType) {
            return new WriterOptions(statusCode, 204, mediaType);
        }
    }
}
