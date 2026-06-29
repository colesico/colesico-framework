package colesico.framework.telehttp;

import colesico.framework.assist.ExceptionUtils;
import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.teleapi.dataport.TeleFactory;
import colesico.framework.telehttp.response.DynamicResponse;

abstract public class TeleHttpDataPort<R extends TeleHttpReadOptions, W extends TeleHttpWriteOptions>
        implements DataPort<R, W> {

    protected final TeleFactory teleFactory;

    public TeleHttpDataPort(TeleFactory teleFactory) {
        this.teleFactory = teleFactory;
    }

    abstract protected Class<? extends TeleHttpReader> readerBaseClass();

    abstract protected Class<? extends TeleHttpWriter> writerBaseClass();

    abstract protected R readOptions();

    abstract protected R readOptions(Object attachment);

    abstract protected W writeOptions();

    abstract protected W writeOptions(Object attachment);

    @Override
    public <V> V read(Class<V> baseType, R options) {

        if (options.readerClass() != null) {
            // Obtain specified reader
            TeleHttpReader<V, R> reader = (TeleHttpReader) teleFactory.provideReader(options.readerClass());
            return reader.read(baseType, options);
        }

        // Find reader by baseType
        TeleHttpReader<V, TeleHttpReadOptions> reader = teleFactory.findReader(baseType, readerBaseClass(), TeleHttpReader.class);
        if (reader == null) {
            // No accurate reader here so are reading data as object - obtain object reader
            reader = teleFactory.provideReader(Object.class, readerBaseClass(), TeleHttpReader.class);
        }
        return reader.read(baseType, options);
    }

    @Override
    public <V> V read(Class<V> baseType) {
        return read(baseType, readOptions());
    }

    @Override
    public <V> V read(Class<V> baseType, Object attachment) {
        return read(baseType, readOptions(attachment));
    }

    @Override
    public <V> void write(V value, Class<V> baseType, W options) {

        // Handle dynamic actualResponse
        Object targetValue;
        if (value instanceof DynamicResponse(Object resp)) {
            targetValue = resp;
        } else {
            targetValue = value;
        }

        // Check for a custom writer specified in options
        if (options.writerClass() != null) {
            TeleHttpWriter writer = teleFactory.provideWriter(options.writerClass());
            writer.write(targetValue, options);
            return;
        }

        // Find writer by the exact runtime class of the actualResponse
        TeleHttpWriter<Object, TeleHttpWriteOptions> writer;
        if (targetValue instanceof Throwable throwable) {
            writer = findExceptionWriter(throwable);
        } else {
            writer = teleFactory.findWriter(targetValue.getClass(), writerBaseClass(), TeleHttpWriter.class);
        }

        // Find by baseType
        if (writer == null) {
            writer = teleFactory.findWriter(baseType, writerBaseClass(), TeleHttpWriter.class);
            // Final fallback to the default object writer
            if (writer == null) {
                writer = teleFactory.provideWriter(Object.class, writerBaseClass(), TeleHttpWriter.class);
            }
        }

        writer.write(targetValue, options);

    }

    @Override
    public <V> void write(V value, Class<V> baseType) {
        write(value, baseType, writeOptions());
    }

    @Override
    public <V> void write(V value, Class<V> baseType, Object attachment) {
        write(value, baseType, writeOptions(attachment));
    }

    protected TeleHttpWriter<Object, TeleHttpWriteOptions> findExceptionWriter(final Throwable throwable) {

        // Find by exact runtime class
        TeleHttpWriter<Object, TeleHttpWriteOptions> writer = teleFactory.findWriter(throwable.getClass(), writerBaseClass(), TeleHttpWriter.class);
        if (writer != null) {
            return writer;
        }

        // Find by runtime class of root cause
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        if (rootCause != throwable) {
            writer = teleFactory.findWriter(rootCause.getClass(), writerBaseClass(), TeleHttpWriter.class);
            if (writer != null) {
                return writer;
            }
        }

        if (throwable instanceof TeleHttpException) {
            writer = teleFactory.findWriter(TeleHttpException.class, writerBaseClass(), TeleHttpWriter.class);
            if (writer != null) {
                return writer;
            }
        }

        return teleFactory.findWriter(Exception.class, writerBaseClass(), TeleHttpWriter.class);

    }
}
