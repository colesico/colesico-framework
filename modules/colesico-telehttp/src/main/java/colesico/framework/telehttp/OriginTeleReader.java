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
        Origin origin = originFactory.getOrigin(context.getOriginName());
        return origin.getString(context.getParamName());
    }

    protected final String readString(String originName, String paramName) {
        Origin origin = originFactory.getOrigin(originName);
        return origin.getString(paramName);
    }

}
