package colesico.framework.rpc.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpResponse;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.ioc.production.Supplier;
import colesico.framework.rpc.Error;
import colesico.framework.rpc.teleapi.RpcDataPort;
import colesico.framework.rpc.teleapi.RpcSerializer;
import colesico.framework.rpc.teleapi.RpcTDRContext;
import colesico.framework.rpc.teleapi.RpcTDWContext;
import colesico.framework.rpc.teleapi.reader.RpcTeleReader;
import colesico.framework.rpc.teleapi.reader.RpcTeleWriter;
import colesico.framework.teleapi.TeleReader;
import colesico.framework.teleapi.TeleWriter;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Singleton
public class RpcDataProtImpl implements RpcDataPort {

    protected final Ioc ioc;
    private final RpcSerializer serializer;
    protected final Provider<HttpContext> httpContextProv;

    public RpcDataProtImpl(Ioc ioc, RpcSerializer serializer, Provider<HttpContext> httpContextProv) {
        this.ioc = ioc;
        this.serializer = serializer;
        this.httpContextProv = httpContextProv;
    }

    protected String typeToClassName(Type valueType) {
        if (valueType instanceof Class) {
            return ((Class) valueType).getCanonicalName();
        } else {
            return valueType.getTypeName();
        }
    }

    @Override
    public <V> V read(Type valueType, RpcTDRContext context) {
        // Try to get accurate reader
        final Supplier<RpcTeleReader> supplier
                = ioc.supplierOrNull(new ClassedKey<>(RpcTeleReader.class.getCanonicalName(), typeToClassName(valueType)));
        if (supplier != null) {
            final TeleReader<V, RpcTDRContext> reader = supplier.get(null);
            return reader.read(context);
        }

        // Common reader
        HttpContext httpContext = httpContextProv.get();
        try (InputStream is = httpContext.getRequest().getInputStream()) {
            return serializer.deserialize(is, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <V> void write(Type valueType, V value, RpcTDWContext context) {
        final Supplier<RpcTeleWriter> supplier
                = ioc.supplierOrNull(new ClassedKey<>(RpcTeleWriter.class.getCanonicalName(), typeToClassName(valueType)));
        if (supplier != null) {
            final TeleWriter<V, RpcTDWContext> writer = supplier.get(null);
            writer.write(value, context);
            return;
        }

        // Common writer
        HttpContext httpContext = httpContextProv.get();
        HttpResponse httpResponse = httpContext.getResponse();
        String ser = serializer.serialize(value);
        if (ser == null) {
            httpResponse.sendText("", RESPONSE_CONTENT_TYPE, 204);
        } else {
            httpResponse.sendData(ByteBuffer.wrap(ser.getBytes(StandardCharsets.UTF_8)), RESPONSE_CONTENT_TYPE, 200);
        }
    }

    @Override
    public void writeError(Error error) {
        HttpContext httpContext = httpContextProv.get();
        HttpResponse httpResponse = httpContext.getResponse();
        String ser = serializer.serialize(error);
        httpResponse.sendData(ByteBuffer.wrap(ser.getBytes(StandardCharsets.UTF_8)), RESPONSE_CONTENT_TYPE, 500);
    }
}
