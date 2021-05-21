package colesico.framework.telehttp;

/**
 * Basic origin based reader
 *
 * @param <V> type of value to be read
 * @param <C> reading context
 */
abstract public class OriginTeleReader<V, C extends HttpTRContext> implements HttpTeleReader<V, C> {

    protected final OriginFactory originFactory;

    public OriginTeleReader(OriginFactory originFactory) {
        this.originFactory = originFactory;
    }

    /**
     * Return param string value from origin defined in the context
     */
    protected final String readString(C context) {
        Origin<String, String> origin = originFactory.getOrigin(context.getOriginName());
        return origin.getValue(context.getParamName());
    }

    protected final <K> V readValue(String originName, K key) {
        Origin<K, V> origin = originFactory.getOrigin(originName);
        return origin.getValue(key);
    }

}