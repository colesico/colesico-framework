package colesico.framework.rpc.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpResponse;
import colesico.framework.rpc.Fault;
import colesico.framework.rpc.teleapi.RpcDataPort;
import colesico.framework.rpc.teleapi.RpcSerializer;
import colesico.framework.rpc.teleapi.RpcTDRContext;
import colesico.framework.rpc.teleapi.RpcTDWContext;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Singleton
public class RpcDataProtImpl implements RpcDataPort {

    private final RpcSerializer serializer;
    protected final Provider<HttpContext> httpContextProv;

    public RpcDataProtImpl(RpcSerializer serializer, Provider<HttpContext> httpContextProv) {
        this.serializer = serializer;
        this.httpContextProv = httpContextProv;
    }

    @Override
    public <V> V read(Type valueType, RpcTDRContext context) {
        HttpContext httpContext = httpContextProv.get();
        try (InputStream is = httpContext.getRequest().getInputStream()) {
            return serializer.deserialize(is, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <V> void write(Type valueType, V value, RpcTDWContext context) {
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
    public void writeFault(Fault fault) {
        HttpContext httpContext = httpContextProv.get();
        HttpResponse httpResponse = httpContext.getResponse();
        String ser = serializer.serialize(fault);
        httpResponse.sendData(ByteBuffer.wrap(ser.getBytes(StandardCharsets.UTF_8)), RESPONSE_CONTENT_TYPE, 500);
    }
}
