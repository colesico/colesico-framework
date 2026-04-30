package colesico.framework.telehttp;

/**
 * Basic origin based reader
 *
 * @param <V> type of value to  be read
 * @param <C> reading context
 */
abstract public class OriginTeleReader<V, C extends HttpTRContext<?, ?>> implements HttpTeleReader<V, C> {

    protected final OriginFactory originFactory;

    public OriginTeleReader(OriginFactory originFactory) {
        this.originFactory = originFactory;
    }

    /**
     * Return param string value from origin defined in the context
     */
    protected final Iterable<String> readStrings(C context) {
        Origin origin = originFactory.getOrigin(context.originName());
        return origin.getStrings(context.paramName());
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
