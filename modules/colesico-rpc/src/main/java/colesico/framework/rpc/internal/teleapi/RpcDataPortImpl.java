package colesico.framework.rpc.internal.teleapi;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.rpc.RpcError;
import colesico.framework.rpc.RpcException;
import colesico.framework.rpc.teleapi.*;
import colesico.framework.rpc.teleapi.RpcTeleReader;
import colesico.framework.rpc.teleapi.RpcTeleWriter;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.Type;

public class RpcDataPortImpl implements RpcDataPort {

    protected final Ioc ioc;

    protected final RpcRequest request;
    protected final RpcResponse response;

    public RpcDataPortImpl(Ioc ioc, RpcRequest request, RpcResponse response) {
        this.request = request;
        this.response = response;
        this.ioc = ioc;
    }

    @Override
    public <V> V read(Type valueType, RpcTRContext context) {

        // Try to get accurate reader
        RpcTeleReader<V> reader = ioc.instanceOrNull(new ClassedKey<>(RpcTeleReader.class.getCanonicalName(), typeToClassName(valueType)), null);
        if (reader != null) {
            // Ctx can be null for reading by type  (Principal reading, etc.)
            if (context == null) {
                context = RpcTRContext.withRequest(request);
            } else {
                context.setRequest(request);
            }
            return reader.read(context);
        }

        // Common read

        if (context == null) {
            throw new RpcException("RPC value reading context required for reading type: " + valueType.getTypeName());
        }

        RpcTRContext.ValueGetter<RpcRequest, V> valueGetter = context.getValueGetter();

        if (valueGetter == null) {
            throw new RpcException("RPC value getter required reading type: " + valueType.getTypeName());
        }

        return valueGetter.get(request);
    }

    @Override
    public <V> void write(Type valueType, V value, RpcTWContext context) {

        // Try to get accurate writer
        RpcTeleWriter<V> writer = ioc.instanceOrNull(new ClassedKey<>(RpcTeleWriter.class.getCanonicalName(), typeToClassName(valueType)), null);
        if (writer != null) {
            if (context == null) {
                context = RpcTWContext.withResponse(response);
            } else {
                context.setResponse(response);
            }
            writer.write(value, context);
            return;
        }

        // Common write
        response.setResult(value);
    }

    @Override
    public <T extends Throwable> void writeError(T throwable) {

        // Create default writing context
        RpcTWContext context = RpcTWContext.withResponse(response);

        RpcTeleWriter<T> throwableWriter = ioc.instanceOrNull(new ClassedKey<>(RpcTeleWriter.class.getCanonicalName(), typeToClassName(throwable.getClass())), null);

        if (throwableWriter != null) {
            throwableWriter.write(throwable, context);
            return;
        }

        // If no specific writer try to get root exception
        // and determine writer for it
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        if (rootCause != null) {
            RpcTeleWriter rootCauseWriter = ioc.instanceOrNull(new ClassedKey<>(RpcTeleWriter.class.getCanonicalName(), typeToClassName(rootCause.getClass())), null);
            if (rootCauseWriter != null) {
                rootCauseWriter.write(rootCause, context);
                return;
            }
        }

        // No specific writer,
        // Perform default writing
        RpcError error = RpcError.of(throwable.getClass(), ExceptionUtils.getRootCauseMessage(throwable));

        context.getResponse().setError(error);
    }

    protected String typeToClassName(Type valueType) {
        if (valueType instanceof Class) {
            return ((Class) valueType).getCanonicalName();
        } else {
            return valueType.getTypeName();
        }
    }

}
