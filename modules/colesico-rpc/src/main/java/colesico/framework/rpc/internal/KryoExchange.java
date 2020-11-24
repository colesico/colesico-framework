package colesico.framework.rpc.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.rpc.teleapi.RpcExchange;
import colesico.framework.rpc.teleapi.RpcRequest;
import colesico.framework.rpc.teleapi.RpcResponse;
import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;
import com.esotericsoftware.kryo.kryo5.util.Pool;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

@Singleton
public class KryoExchange implements RpcExchange {

    String RESPONSE_CONTENT_TYPE = "application/octet-stream";

    private Provider<HttpContext> httpContextProv;

    private Pool<Kryo> kryoPool = new Pool<Kryo>(true, false, 8) {
        protected Kryo create() {
            Kryo kryo = new Kryo();
            // Configure the Kryo instance.
            return kryo;
        }
    };

    public KryoExchange(Provider<HttpContext> httpContextProv) {
        this.httpContextProv = httpContextProv;
    }

    //@Override
    public RpcRequest readRequest() {
        HttpContext ctx = httpContextProv.get();
        RpcRequest request = deserialize(ctx.getRequest().getInputStream());
        return request;
    }

    @Override
    public void writeResponse(RpcResponse response) {
        HttpContext ctx = httpContextProv.get();
        ctx.getResponse().setContenType(RESPONSE_CONTENT_TYPE);
        serialize(response, ctx.getResponse().getOutputStream());
    }

    private <T> void serialize(T obj, OutputStream os) {
        Kryo kryo = kryoPool.obtain();
        try (Output output = new Output(os)) {
            kryo.writeClassAndObject(output, obj);
        } finally {
            kryoPool.free(kryo);
        }
    }

    private <T> T deserialize(InputStream is) {
        Kryo kryo = kryoPool.obtain();
        try (Input input = new Input(is)) {
            Object res = kryo.readClassAndObject(input);
            return (T) res;
        } finally {
            kryoPool.free(kryo);
        }
    }

    @Override
    public RequestResolution resolveRequest() {
        return null;
    }

    @Override
    public <Q extends RpcRequest> Q readRequest(Type requestType) {
        return null;
    }
}
