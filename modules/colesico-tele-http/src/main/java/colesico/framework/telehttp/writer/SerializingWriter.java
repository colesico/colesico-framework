package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.ioc.message.IocMessage;
import colesico.framework.ioc.production.Supplier;

import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.telehttp.MediaType;
import colesico.framework.telehttp.TeleHttpException;
import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.response.ValueResponse;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

import java.io.OutputStream;

/**
 * {@link ValueSerializer} based writer
 */
@Unscoped
public class SerializingWriter<R extends ValueResponse<?>, O extends TeleHttpWriteOptions>
        extends TeleHttpResponseWriter<R, O> {

    protected final Supplier<ValueSerializer> serializerFactory;

    @Inject
    public SerializingWriter(Provider<HttpResponse> httpResponse,
                             Supplier<ValueSerializer> serializerFactory,
                             @IocMessage WriterOptions writerOptions) {
        super(httpResponse, writerOptions);
        this.serializerFactory = serializerFactory;
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
            protocol.setStatus(204).close();
            return;
        }

        try (OutputStream os = protocol.outputStream()) {
            var serializer = serializer(mediaType.mimeType());
            serializer.serialize(response.value(), mediaType, os);
            os.flush();
        } catch (Exception e) {
            throw TeleHttpException.of(e, 500);
        }
    }
}
