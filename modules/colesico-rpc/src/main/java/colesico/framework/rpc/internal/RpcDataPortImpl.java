package colesico.framework.rpc.internal;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.ioc.production.Supplier;
import colesico.framework.rpc.teleapi.*;
import colesico.framework.rpc.teleapi.reader.RpcTeleReader;
import colesico.framework.rpc.teleapi.reader.RpcTeleWriter;
import colesico.framework.teleapi.TeleReader;
import colesico.framework.teleapi.TeleWriter;

import java.lang.reflect.Type;

public class RpcDataPortImpl implements RpcDataPort {

    protected final RpcRequest request;
    protected final RpcResponse response;

    protected final Ioc ioc;

    public RpcDataPortImpl(RpcRequest request, RpcResponse response, Ioc ioc) {
        this.request = request;
        this.response = response;
        this.ioc = ioc;
    }

    @Override
    public <V> V read(Type valueType, RpcTRContext context) {
        // Try to get accurate reader
        final ClassedKey readerKey = new ClassedKey<>(RpcTeleReader.class.getCanonicalName(), typeToClassName(valueType));
        final Supplier<RpcTeleReader> supplier = ioc.supplierOrNull(readerKey);
        if (supplier != null) {
            final TeleReader<V, RpcTRContext> reader = supplier.get(null);
           // context.setRequest(request);
            return reader.read(context);
        }

        // Common read
        //return (V) request.getParams().get(context.getValueSupplier());
        return null;
    }

    @Override
    public <V> void write(Type valueType, V value, RpcTWContext context) {
        // Try to get accurate writer
        final ClassedKey writerKey = new ClassedKey<>(RpcTeleWriter.class.getCanonicalName(), typeToClassName(valueType));
        final Supplier<RpcTeleWriter> supplier = ioc.supplierOrNull(writerKey);
        if (supplier != null) {
            final TeleWriter<V, RpcTWContext> writer = supplier.get(null);
            context.setResponse(response);
            writer.write(value, context);
            return;
        }

        // Common write
        //response.setResult(value);
    }

    protected String typeToClassName(Type valueType) {
        if (valueType instanceof Class) {
            return ((Class) valueType).getCanonicalName();
        } else {
            return valueType.getTypeName();
        }
    }
}
