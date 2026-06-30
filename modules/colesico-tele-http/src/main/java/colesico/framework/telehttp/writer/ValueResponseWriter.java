package colesico.framework.telehttp.writer;

import colesico.framework.config.Config;
import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
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
    protected final Config config;

    public ValueResponseWriter(@IocMessage Config config,
                               Provider<HttpResponse> httpResponse,
                               Supplier<ValueSerializer> serializerFactory) {

        super(httpResponse);
        this.serializerFactory = serializerFactory;
        if (config != null) {
            this.config = config;
        } else {
            this.config = new Config();
        }
    }

    @Override
    protected MediaType defaultMediaType() {
        return config.defaultMediaType();
    }

    @Override
    protected Integer defaultStatusCode() {
        return config.defaultStatusCode();
    }

    @Override
    protected Integer emptyStatusCode() {
        return config.emptyStatusCode();
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
     * Writer config
     */
    @ConfigPrototype(model = ConfigModel.MESSAGE, target = ValueResponseWriter.class)
    public static class Config {

        public Integer defaultStatusCode() {
            return 200;
        }

        public Integer emptyStatusCode() {
            return 204;
        }

        public MediaType defaultMediaType() {
            return MediaType.TEXT_PLAIN;
        }

    }

}
