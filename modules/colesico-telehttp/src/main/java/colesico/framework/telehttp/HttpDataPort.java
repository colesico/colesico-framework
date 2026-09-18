package colesico.framework.telehttp;

import colesico.framework.assist.ExceptionUtils;
import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.teleapi.dataport.TeleFactory;

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

        // Check for a custom writer specified in options
        if (options.customWriter() != null) {
            HttpWriter writer = (HttpWriter) teleFactory.writer(options.customWriter());
            writer.write(value, options);
            return;
        }

        // Find writer by the exact runtime class of the value
        HttpWriter<Object, HttpWriteOptions> writer = null;
        if (value != null) {
            writer = teleFactory.findWriter(value.getClass(), writerBaseClass(), HttpWriter.class);
        }

        // Find by baseType
        if (writer == null) {
            writer = teleFactory.findWriter(options.baseType(), writerBaseClass(), HttpWriter.class);
        }

        if (writer == null && value instanceof Exception) {
            writer = teleFactory.provideWriter(Exception.class, writerBaseClass(), HttpWriter.class);
        }

        // Final fallback to the default object writer
        if (writer == null) {
            writer = teleFactory.provideWriter(Object.class, writerBaseClass(), HttpWriter.class);
        }

        writer.write(value, options);

    }

}
