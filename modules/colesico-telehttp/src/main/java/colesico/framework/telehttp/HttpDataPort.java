package colesico.framework.telehttp;

import colesico.framework.assist.ExceptionUtils;
import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.teleapi.dataport.TeleFactory;
import colesico.framework.telehttp.response.DynamicResponse;

abstract public class HttpDataPort<R extends HttpReadOptions, W extends HttpWriteOptions>
        implements DataPort<R, W> {

    protected final TeleFactory teleFactory;

    public HttpDataPort(TeleFactory teleFactory) {
        this.teleFactory = teleFactory;
    }

    abstract protected Class<? extends HttpReader> readerBaseClass();

    abstract protected Class<? extends HttpWriter> writerBaseClass();

    @Override
    public <V> V read(R options) {
        if (options.customReader() != null) {
            // Obtain specified reader
            HttpReader<V, R> reader = (HttpReader) teleFactory.reader(options.customReader());
            return reader.read(options);
        }

        // Find reader by baseType
        HttpReader<V, HttpReadOptions> reader = teleFactory.findReader(options.baseType(), readerBaseClass(), HttpReader.class);
        if (reader == null) {
            // No accurate reader here so are reading data as object - obtain object reader
            reader = teleFactory.provideReader(Object.class, readerBaseClass(), HttpReader.class);
        }
        return reader.read(options);
    }

    @Override
    public <V> void write(V value, W options) {

        // Handle dynamic value
        Object targetValue;
        if (value instanceof DynamicResponse(Object resp)) {
            targetValue = resp;
        } else {
            targetValue = value;
        }

        // Check for a custom writer specified in options
        if (options.customWriter() != null) {
            HttpWriter writer = (HttpWriter) teleFactory.writer(options.customWriter());
            writer.write(targetValue, options);
            return;
        }

        // Find writer by the exact runtime class of the value
        HttpWriter<Object, HttpWriteOptions> writer;
        if (targetValue instanceof Throwable throwable) {
            writer = findExceptionWriter(throwable);
        } else {
            writer = teleFactory.findWriter(targetValue.getClass(), writerBaseClass(), HttpWriter.class);
        }

        // Find by baseType
        if (writer == null) {
            writer = teleFactory.findWriter(options.baseType(), writerBaseClass(), HttpWriter.class);
            // Final fallback to the default object writer
            if (writer == null) {
                writer = teleFactory.provideWriter(Object.class, writerBaseClass(), HttpWriter.class);
            }
        }

        writer.write(targetValue, options);

    }

    protected HttpWriter<Object, HttpWriteOptions> findExceptionWriter(final Throwable throwable) {

        // Find by exact runtime class
        HttpWriter<Object, HttpWriteOptions> writer = teleFactory.findWriter(throwable.getClass(), writerBaseClass(), HttpWriter.class);
        if (writer != null) {
            return writer;
        }

        // Find by runtime class of root cause
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        if (rootCause != throwable) {
            writer = teleFactory.findWriter(rootCause.getClass(), writerBaseClass(), HttpWriter.class);
            if (writer != null) {
                return writer;
            }
        }

        if (throwable instanceof HttpTeleException) {
            writer = teleFactory.findWriter(HttpTeleException.class, writerBaseClass(), HttpWriter.class);
            if (writer != null) {
                return writer;
            }
        }

        return teleFactory.findWriter(Exception.class, writerBaseClass(), HttpWriter.class);

    }
}
