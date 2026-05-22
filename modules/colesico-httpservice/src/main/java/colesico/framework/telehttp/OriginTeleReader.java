package colesico.framework.telehttp;

/**
 * Basic origin based reader
 *
 * @param <V> type of value to  be read
 * @param <R> reading context
 */
abstract public class OriginTeleReader<V, R extends HttpReadOptions<?>> implements HttpTeleReader<V, R> {

    protected final OriginFactory originFactory;

    public OriginTeleReader(OriginFactory originFactory) {
        this.originFactory = originFactory;
    }

    /**
     * Return param string value from origin defined in the context
     */
    protected final Iterable<String> readStrings(R options, HttpTeleReader.Channel channel) {
        Origin origin = originFactory.getOrigin(options.originName());
        return origin.getStrings(options.paramName(), channel);
    }

    protected final Iterable<String> readStrings(String originName, String paramName, HttpTeleReader.Channel channel) {
        Origin origin = originFactory.getOrigin(originName);
        return origin.getStrings(paramName, channel);
    }

    protected final String readString(R options, HttpTeleReader.Channel channel) {
        var it = readStrings(options, channel).iterator();
        return it.hasNext() ? it.next() : null;
    }

    protected final String readString(String originName, String paramName, HttpTeleReader.Channel channel) {
        var it = readStrings(originName, paramName, channel).iterator();
        return it.hasNext() ? it.next() : null;
    }

}
