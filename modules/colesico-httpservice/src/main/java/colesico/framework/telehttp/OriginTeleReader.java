package colesico.framework.telehttp;

/**
 * Basic origin based reader
 *
 * @param <V> type of value to  be read
 * @param <C> reading context
 */
abstract public class OriginTeleReader<V, C extends HttpTeleContext<?, ?>> extends HttpTeleReader<V, C> {

    protected final OriginFactory originFactory;

    public OriginTeleReader(OriginFactory originFactory) {
        this.originFactory = originFactory;
    }

    /**
     * Return param string value from origin defined in the context
     */
    protected final Iterable<String> readStrings(C trContext) {
        Origin origin = originFactory.getOrigin(trContext.originName());
        return origin.getStrings(trContext.paramName());
    }

    protected final Iterable<String> readStrings(String originName, String paramName) {
        Origin origin = originFactory.getOrigin(originName);
        return origin.getStrings(paramName);
    }

    protected final String readString(C context) {
        var it = readStrings(context).iterator();
        return it.hasNext() ? it.next() : null;
    }

    protected final String readString(String originName, String paramName) {
        var it = readStrings(originName, paramName).iterator();
        return it.hasNext() ? it.next() : null;
    }

}
