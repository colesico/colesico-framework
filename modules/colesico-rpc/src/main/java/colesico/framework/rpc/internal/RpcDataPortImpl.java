package colesico.framework.rpc.internal;

import colesico.framework.rpc.RpcError;
import colesico.framework.rpc.RpcException;
import colesico.framework.rpc.teleapi.*;
import colesico.framework.teleapi.TeleFactory;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.Type;

public class RpcDataPortImpl implements RpcDataPort {

    protected final TeleFactory teleFactory;

    protected final RpcRequest request;
    protected final RpcResponse response;

    public RpcDataPortImpl(TeleFactory teleFactory, RpcRequest request, RpcResponse response) {
        this.request = request;
        this.response = response;
        this.teleFactory = teleFactory;
    }

    @Override
    public <V> V read(Type valueType) {
        return read(RpcTRContext.of(valueType));
    }

    @Override
    public <V> V read(RpcTRContext context) {

        if (context == null) {
            throw new RpcException("RPC value reading context required for reading value ");
        }

        // Try to get accurate reader
        RpcTeleReader<V> reader = teleFactory.findReader(RpcTeleReader.class, context.getValueType());
        if (reader != null) {
            // Ctx can be null for reading by type  (Principal reading, etc.)
            context.setRequest(request);
            return reader.read(context);
        }

        // Common read

        RpcTRContext.ValueGetter<RpcRequest, V> valueGetter = context.getValueGetter();

        if (valueGetter == null) {
            throw new RpcException("RPC value getter required reading type: " + context.getValueType().getTypeName());
        }

        return valueGetter.get(request);
    }

    @Override
    public <V> void write(V value, Type valueType) {
        write(value, RpcTWContext.of(valueType));
    }

    @Override
    public <V> void write(V value, RpcTWContext context) {

        // Try to get accurate writer
        RpcTeleWriter<V> writer = teleFactory.findWriter(RpcTeleWriter.class, context.getValueType());
        if (writer != null) {
            context.setResponse(response);
            writer.write(value, context);
            return;
        }

        // Common write
        response.setResult(value);
    }

    @Override
    public <T extends Throwable> void writeError(T throwable) {

        // Create default writing context
        RpcTWContext context = RpcTWContext.of(throwable.getClass(), response);

        RpcTeleWriter<T> throwableWriter = teleFactory.findWriter(RpcTeleWriter.class, throwable.getClass());

        if (throwableWriter != null) {
            throwableWriter.write(throwable, context);
            return;
        }

        // If no specific writer try to get root exception
        // and determine writer for it
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        if (rootCause != null) {
            RpcTeleWriter rootCauseWriter = teleFactory.findWriter(RpcTeleWriter.class, rootCause.getClass());
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

}
