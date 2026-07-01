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

    @Override
    public <V> V read(R options) {
        if (options.customReader() != null) {
            // Obtain specified reader
            TeleHttpReader<V, R> reader = (TeleHttpReader) teleFactory.reader(options.customReader());
            return reader.read(options);
        }

        // Find reader by baseType
        TeleHttpReader<V, TeleHttpReadOptions> reader = teleFactory.findReader(options.baseType(), readerBaseClass(), TeleHttpReader.class);
        if (reader == null) {
            // No accurate reader here so are reading data as object - obtain object reader
            reader = teleFactory.provideReader(Object.class, readerBaseClass(), TeleHttpReader.class);
        }
        return reader.read(options);
    }

    @Override
    public <V> void write(V value, W options) {

        // Handle dynamic actualResponse
        Object targetValue;
        if (value instanceof DynamicResponse(Object resp)) {
            targetValue = resp;
        } else {
            targetValue = value;
        }

        // Check for a custom writer specified in options
        if (options.customWriter() != null) {
            TeleHttpWriter writer = (TeleHttpWriter) teleFactory.writer(options.customWriter());
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
            writer = teleFactory.findWriter(options.baseType(), writerBaseClass(), TeleHttpWriter.class);
            // Final fallback to the default object writer
            if (writer == null) {
                writer = teleFactory.provideWriter(Object.class, writerBaseClass(), TeleHttpWriter.class);
            }
        }

        writer.write(targetValue, options);

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
